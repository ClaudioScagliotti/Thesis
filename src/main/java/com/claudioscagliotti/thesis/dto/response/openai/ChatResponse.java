package com.claudioscagliotti.thesis.dto.response.openai;

import com.claudioscagliotti.thesis.dto.request.openai.Choice;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ChatResponse {
    private List<Choice> choices;

}
