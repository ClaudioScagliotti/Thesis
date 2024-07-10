package com.claudioscagliotti.thesis.dto.openai;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Choice {
    private int index;
    private Message message;

    public Choice(Message message, List<Choice> existingChoices) {
        this.message = message;
        if(existingChoices==null){
            existingChoices = new ArrayList<>();
        }
        this.index = calculateLastIndex(existingChoices)+1;
    }

    public Choice(int index, Message message) {
        this.index = index;
        this.message = message;
    }

    public static int calculateLastIndex(List<Choice> existingChoices) {
        return existingChoices.stream()
                .mapToInt(Choice::getIndex)
                .max()
                .orElse(-1);
    }
}
