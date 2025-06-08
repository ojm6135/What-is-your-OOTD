package com.school.what_is_your_ootd.util;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;
import java.util.Optional;

public class OpenAIFetcher {
    private static final String API_KEY = Dotenv.load().get("OPENAI_API_KEY");

    public static Optional<String> getResponse(String prompt) {
        OpenAIClient client = OpenAIOkHttpClient.builder().apiKey(API_KEY).build();

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model(ChatModel.GPT_4_1_MINI)
                .temperature(0.8)
                .build();
        ChatCompletion chatCompletion = client.chat().completions().create(params);
        List<ChatCompletion.Choice> choices = chatCompletion.choices();
        ChatCompletion.Choice choice = choices.get(0);

        return choice.message().content();
    }
}
