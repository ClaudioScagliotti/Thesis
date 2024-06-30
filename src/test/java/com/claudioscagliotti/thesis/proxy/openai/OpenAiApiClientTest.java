package com.claudioscagliotti.thesis.proxy.openai;

import com.claudioscagliotti.thesis.dto.request.openai.ChatRequest;
import com.claudioscagliotti.thesis.dto.response.openai.ChatResponse;
import com.claudioscagliotti.thesis.dto.request.openai.Choice;
import com.claudioscagliotti.thesis.dto.request.openai.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class OpenAiApiClientTest {

    @MockBean
    @Qualifier("openaiRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private OpenAiApiClient openAiApiClient;

    @Test
    public void testChatWithMockResponse() {
        ChatResponse mockResponse = new ChatResponse();
        List<Choice> choices = new ArrayList<>();
        Choice choice = new Choice();
        Message message = new Message("user","ciao come stai?");
        message.setContent("Questa è una risposta mockata dal modello GPT.");
        choice.setMessage(message);
        choices.add(choice);
        mockResponse.setChoices(choices);

        Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.eq(ChatResponse.class)))
                .thenReturn(mockResponse);


        ChatRequest chatRequest = new ChatRequest("Ciao", "system");


        ChatResponse response = openAiApiClient.chat(chatRequest);

        assertNotNull(response);
        assertFalse(response.getChoices().isEmpty());
        assertEquals("Questa è una risposta mockata dal modello GPT.", response.getChoices().get(0).getMessage().getContent());
    }
}
