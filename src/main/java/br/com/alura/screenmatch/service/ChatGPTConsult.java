package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ChatGPTConsult {

    public static String getTraductor(String text) {

        OpenAiService service = new OpenAiService(System.getenv("OPENAI_API_KEY"));

        CompletionRequest request = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduza para o português o texto: " + text)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var result = service.createCompletion(request);

        return result.getChoices().get(0).getText();
    }
}
