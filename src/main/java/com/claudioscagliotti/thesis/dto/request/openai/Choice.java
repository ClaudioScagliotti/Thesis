package com.claudioscagliotti.thesis.dto.request.openai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Choice {
    private int index;
    private Message message;
}
