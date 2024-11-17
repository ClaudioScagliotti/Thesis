package com.claudioscagliotti.thesis.utility;

import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import com.claudioscagliotti.thesis.model.QuizEntity;

import java.util.List;

public class PercentageCalculatorUtil {
    private PercentageCalculatorUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Calculates the percentage of quizzes that have succeeded.
     *
     * @param quizzes A list of {@link QuizEntity} objects representing the quizzes.
     * @return The percentage of quizzes that have succeeded. If the list is null or empty, returns 100.0.
     */
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

