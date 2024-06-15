package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.QuizDto;
import com.claudioscagliotti.thesis.mapper.QuizMapper;
import com.claudioscagliotti.thesis.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;

    public QuizService(QuizRepository quizRepository, QuizMapper quizMapper) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
    }
    public List<QuizDto> findAllByLessonId(Long lessonId){
        return quizMapper.toQuizDto(quizRepository.findAllByLessonId(lessonId));
    }
    public List<QuizDto> findAllByAdviceId(Long adviceId){
        return quizMapper.toQuizDto(quizRepository.findAllByAdviceId(adviceId));
    }
}
