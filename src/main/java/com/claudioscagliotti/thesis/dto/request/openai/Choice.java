package com.claudioscagliotti.thesis.dto.request.openai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Choice {
    private int index;
    private Message message;

    public Choice(Message message, List<Choice> existingChoices) {
        this.message = message;
        this.index = calculateNextIndex(existingChoices);
    }

    private int calculateNextIndex(List<Choice> existingChoices) {
        return existingChoices.stream()
                .mapToInt(Choice::getIndex)
                .max()
                .orElse(-1) + 1;
    }
}
