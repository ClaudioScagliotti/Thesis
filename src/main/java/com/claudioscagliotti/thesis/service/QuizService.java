package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.QuizDto;
import com.claudioscagliotti.thesis.mapper.QuizMapper;
import com.claudioscagliotti.thesis.model.ChronologicalOrderQuizEntity;
import com.claudioscagliotti.thesis.model.MultipleChoiceQuizEntity;
import com.claudioscagliotti.thesis.model.QuizEntity;
import com.claudioscagliotti.thesis.model.TrueFalseQuizEntity;
import com.claudioscagliotti.thesis.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;

    public QuizService(QuizRepository quizRepository, QuizMapper quizMapper) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
    }


    public List<QuizDto> findAllByAdviceId(Long adviceId) {
        List<QuizEntity> quizEntities = quizRepository.findAllByAdviceIdWithFetch(adviceId);

        List<QuizDto> quizDtoList= new ArrayList<>();
        for (QuizEntity quiz : quizEntities) {
            if (quiz instanceof MultipleChoiceQuizEntity) {
                quizDtoList.add(quizMapper.toMultipleChoiceQuizDto((MultipleChoiceQuizEntity) quiz));
            } else if (quiz instanceof TrueFalseQuizEntity) {
                quizDtoList.add(quizMapper.toTrueFalseQuizDto((TrueFalseQuizEntity) quiz));
            } else if (quiz instanceof ChronologicalOrderQuizEntity) {
                quizDtoList.add(quizMapper.toChronologicalOrderQuizDto((ChronologicalOrderQuizEntity) quiz));
            } else {
                quizDtoList.add(quizMapper.toQuizDto(quiz));
            }
        }
        return quizDtoList;
    }

    public List<QuizDto> findAllByLessonId(Long lessonId) {
        List<QuizEntity> quizEntities = quizRepository.findAllByLessonIdWithFetch(lessonId);

        List<QuizDto> quizDtoList = new ArrayList<>();
        for (QuizEntity quiz : quizEntities) {
            if (quiz instanceof MultipleChoiceQuizEntity) {
                quizDtoList.add(quizMapper.toMultipleChoiceQuizDto((MultipleChoiceQuizEntity) quiz));
            } else if (quiz instanceof TrueFalseQuizEntity) {
                quizDtoList.add(quizMapper.toTrueFalseQuizDto((TrueFalseQuizEntity) quiz));
            } else if (quiz instanceof ChronologicalOrderQuizEntity) {
                quizDtoList.add(quizMapper.toChronologicalOrderQuizDto((ChronologicalOrderQuizEntity) quiz));
            } else {
                quizDtoList.add(quizMapper.toQuizDto(quiz));
            }
        }
        return quizDtoList;
    }

    /*
    private QuizDto convertToSpecificQuizDto(QuizEntity quiz) {
        if (quiz instanceof MultipleChoiceQuizEntity) {
            return quizMapper.toMultipleChoiceQuizDto((MultipleChoiceQuizEntity) quiz);
        } else if (quiz instanceof TrueFalseQuizEntity) {
            return quizMapper.toTrueFalseQuizDto((TrueFalseQuizEntity) quiz);
        } else if (quiz instanceof ChronologicalOrderQuizEntity) {
            return quizMapper.toChronologicalOrderQuizDto((ChronologicalOrderQuizEntity) quiz);
        } else {
            throw new IllegalArgumentException("Tipo di quiz non supportato: " + quiz.getClass().getSimpleName());
        }
    }

     */

}
