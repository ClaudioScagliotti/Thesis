package com.claudioscagliotti.thesis.dto.request.openai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Message {

    private String role; // "system", "user", or "assistant"
    private String content;

}
