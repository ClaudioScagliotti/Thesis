package com.claudioscagliotti.thesis.dto.request.openai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Choice {
    private int index;
    private Message message;
}
