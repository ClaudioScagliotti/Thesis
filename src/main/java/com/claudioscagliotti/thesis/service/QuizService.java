package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.request.QuizRequest;
import com.claudioscagliotti.thesis.dto.response.QuizDto;
import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import com.claudioscagliotti.thesis.enumeration.QuizTypeEnum;
import com.claudioscagliotti.thesis.exception.SubscriptionUserException;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.mapper.QuizMapper;
import com.claudioscagliotti.thesis.model.AdviceEntity;
import com.claudioscagliotti.thesis.model.LessonEntity;
import com.claudioscagliotti.thesis.model.QuizEntity;
import com.claudioscagliotti.thesis.repository.QuizRepository;
import com.claudioscagliotti.thesis.utility.JsonComparator;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;
    private final AdviceService adviceService;
    private final LessonService lessonService;
    private final CourseService courseService;



    public QuizService(QuizRepository quizRepository, QuizMapper quizMapper, AdviceService adviceService, LessonService lessonService, CourseService courseService) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
        this.adviceService = adviceService;
        this.lessonService = lessonService;
        this.courseService = courseService;
    }


    public List<QuizDto> findAllByAdviceId(Long adviceId, String username) {
        AdviceEntity adviceEntity = adviceService.getById(adviceId);
        if(!adviceEntity.getUserEntity().getUsername().equals(username)){
            throw new UnauthorizedUserException("The user with username: "+username+" does not have the advice with the id:"+adviceId);
        }
        List<QuizEntity> quizEntities = quizRepository.findAllByAdviceId(adviceId);
        return quizMapper.toQuizDto(quizEntities);
    }

    public List<QuizDto> findAllByLessonId(Long lessonId, String username) {
        LessonEntity lessonEntity = lessonService.getById(lessonId);
        if(!courseService.checkSubscription(username, lessonEntity.getCourseEntity().getId())){
            throw new SubscriptionUserException("The user "+username+" is not subscribed to the course with title: "+lessonEntity.getCourseEntity().getTitle());
        }else{
            List<QuizEntity> quizEntities = quizRepository.findAllByLessonId(lessonId);
            return quizMapper.toQuizDto(quizEntities);
        }
    }

    public List<QuizDto> completeQuiz(List<QuizRequest> requestList) throws JsonProcessingException {
        List<QuizEntity> entities=findQuizById(requestList);

        return checkQuizSolutions(requestList,entities);
    }
    private List<QuizEntity> findQuizById(List<QuizRequest> requestList){
        List<QuizEntity> quizEntities = new ArrayList<>();
        for(QuizRequest request: requestList){
            Optional<QuizEntity> entity = quizRepository.findById(request.getQuizId());
            entity.ifPresent(quizEntities::add);
        }
        return quizEntities;
    }

    private List<QuizDto> checkQuizSolutions(List<QuizRequest> requestList, List<QuizEntity> solutionsList) throws JsonProcessingException {
        List<QuizEntity> resultList = new ArrayList<>();

        for (QuizRequest request : requestList) {
            QuizEntity solutionEntity = solutionsList.stream()
                    .filter(q -> q.getId().equals(request.getQuizId()))
                    .findFirst()
                    .orElse(null);

            if (solutionEntity == null) {
                throw new EntityNotFoundException("Not found solution for the quiz request with id: "+request.getQuizId());
            }
            boolean isCorrect = false;

            if (solutionEntity.getType().equals(QuizTypeEnum.CHRONOLOGICAL_ORDER)) {
                isCorrect = checkChronologicalOrderAnswer(solutionEntity, request.getCorrectOrder());
            } else if (solutionEntity.getType().equals(QuizTypeEnum.MULTIPLE_CHOICE)) {
                isCorrect = checkMultipleChoiceAnswer(solutionEntity, request.getCorrectOption());
            } else if (solutionEntity.getType().equals(QuizTypeEnum.TRUE_FALSE)) {
                isCorrect = checkTrueFalseAnswer(solutionEntity, request.getCorrectAnswer());
            }

            if(isCorrect){
                updateQuizStatus(solutionEntity.getId(), QuizResultEnum.SUCCEEDED);
            }
            else {
                updateQuizStatus(solutionEntity.getId(), QuizResultEnum.FAILED);
            }

            Optional<QuizEntity> updatedQuizEntity = quizRepository.findById(solutionEntity.getId());
            updatedQuizEntity.ifPresent(resultList::add);
        }//TODO possible feature: update the quiz result in the advice or in the lesson when the quiz is completed and not when the advice or lesson is completed

        return quizMapper.toQuizDto(resultList);
    }
    private void updateQuizStatus(Long quizId, QuizResultEnum resultEnum){
        quizRepository.updateStatusById(quizId, resultEnum);

    }

    private boolean checkTrueFalseAnswer(QuizEntity quiz, Boolean userAnswer) {
        return userAnswer == quiz.getCorrectAnswer();
    }
    private boolean checkMultipleChoiceAnswer(QuizEntity quiz, Integer userAnswer) {
            return Objects.equals(quiz.getCorrectOption(), userAnswer);
    }
    private boolean checkChronologicalOrderAnswer(QuizEntity quiz, String userAnswer) throws JsonProcessingException {
        return JsonComparator.compareJson(userAnswer, quiz.getCorrectOrder());
    }


}
