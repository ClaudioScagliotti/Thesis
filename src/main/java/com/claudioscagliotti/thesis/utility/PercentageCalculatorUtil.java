package com.claudioscagliotti.thesis.utility;

import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import com.claudioscagliotti.thesis.model.QuizEntity;

import java.util.List;

public class PercentageCalculatorUtil {
    public static double calculateSucceededPercentage(List<QuizEntity> quizzes) {

        if (quizzes == null || quizzes.isEmpty()) {
            return 100.0;
        }
        long succeededCount = quizzes.stream()
                .filter(quiz -> QuizResultEnum.SUCCEEDED.equals(quiz.getStatus()))
                .count();

        return (double) succeededCount / quizzes.size() * 100;
    }
}
