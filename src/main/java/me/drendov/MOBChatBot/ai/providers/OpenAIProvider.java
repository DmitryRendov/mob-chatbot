package me.drendov.MOBChatBot.ai.providers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.drendov.MOBChatBot.ai.AIProvider;
import me.drendov.MOBChatBot.ai.AIResponse;
import me.drendov.MOBChatBot.ai.ConversationMessage;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * OpenAI provider implementation using Chat Completions API
 */
public class OpenAIProvider implements AIProvider {
    
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String SYSTEM_PROMPT = "You are a helpful Minecraft companion. Keep responses brief and Minecraft-focused.";
    
    private final Logger logger;
    private final OkHttpClient httpClient;
    private final String apiKey;
    private final String model;
    private final int maxTokens;
    
    private boolean initialized = false;
    
    public OpenAIProvider(Logger logger, String apiKey, String model, int maxTokens) {
        this.logger = logger;
        this.apiKey = apiKey;
        this.model = model;
        this.maxTokens = maxTokens;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();
    }
    
    @Override
    public boolean initialize() {
        if (!isConfigured()) {
            logger.warning("OpenAI provider is not properly configured!");
            return false;
        }
        
        initialized = true;
        logger.info("OpenAI provider initialized successfully with model: " + model);
        return true;
    }
    
    @Override
    public boolean isConfigured() {
        return apiKey != null && !apiKey.isEmpty() && 
               !apiKey.equals("your-api-key-here") &&
               model != null && !model.isEmpty() &&
               maxTokens > 0;
    }
    
    @Override
    public String getProviderName() {
        return "OpenAI";
    }
    
    @Override
    public CompletableFuture<AIResponse> sendMessage(String message, List<ConversationMessage> conversationHistory) {
        if (!initialized || !isConfigured()) {
            return CompletableFuture.completedFuture(
                AIResponse.failure("OpenAI provider is not properly configured")
            );
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Build the request JSON
                JsonObject requestJson = buildRequestJson(message, conversationHistory);
                
                // Create HTTP request
                RequestBody body = RequestBody.create(requestJson.toString(), JSON);
                Request request = new Request.Builder()
                        .url(API_URL)
                        .addHeader("Authorization", "Bearer " + apiKey)
                        .addHeader("Content-Type", "application/json")
                        .post(body)
                        .build();
                
                // Execute request
                try (Response response = httpClient.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        return handleErrorResponse(response);
                    }
                    
                    // Parse response
                    String responseBody = response.body().string();
                    return parseSuccessResponse(responseBody);
                }
                
            } catch (IOException e) {
                logger.severe("OpenAI API network error: " + e.getMessage());
                return AIResponse.failure("Network error: " + e.getMessage());
            } catch (Exception e) {
                logger.severe("OpenAI API unexpected error: " + e.getMessage());
                e.printStackTrace();
                return AIResponse.failure("Unexpected error: " + e.getMessage());
            }
        });
    }
    
    /**
     * Build the JSON request for OpenAI Chat Completions API
     */
    private JsonObject buildRequestJson(String message, List<ConversationMessage> conversationHistory) {
        JsonObject json = new JsonObject();
        json.addProperty("model", model);
        json.addProperty("max_tokens", maxTokens);
        json.addProperty("temperature", 0.7);
        
        // Build messages array
        JsonArray messages = new JsonArray();
        
        // Add system prompt
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", SYSTEM_PROMPT);
        messages.add(systemMessage);
        
        // Add conversation history
        if (conversationHistory != null) {
            for (ConversationMessage msg : conversationHistory) {
                JsonObject historyMessage = new JsonObject();
                historyMessage.addProperty("role", msg.getRole());
                historyMessage.addProperty("content", msg.getContent());
                messages.add(historyMessage);
            }
        }
        
        // Add current user message
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", message);
        messages.add(userMessage);
        
        json.add("messages", messages);
        
        return json;
    }
    
    /**
     * Parse successful response from OpenAI API
     */
    private AIResponse parseSuccessResponse(String responseBody) {
        try {
            JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
            
            // Extract content
            String content = json.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
            
            // Extract token usage
            int tokensUsed = 0;
            if (json.has("usage")) {
                tokensUsed = json.getAsJsonObject("usage")
                        .get("total_tokens").getAsInt();
            }
            
            return AIResponse.success(content, tokensUsed);
            
        } catch (Exception e) {
            logger.severe("Failed to parse OpenAI response: " + e.getMessage());
            return AIResponse.failure("Failed to parse response: " + e.getMessage());
        }
    }
    
    /**
     * Handle error response from OpenAI API
     */
    private AIResponse handleErrorResponse(Response response) throws IOException {
        int code = response.code();
        String errorBody = response.body() != null ? response.body().string() : "No error details";
        
        String errorMessage;
        switch (code) {
            case 401:
                errorMessage = "Invalid API key";
                logger.warning("OpenAI API: Invalid API key");
                break;
            case 429:
                errorMessage = "Rate limit exceeded";
                logger.warning("OpenAI API: Rate limit exceeded");
                break;
            case 500:
            case 502:
            case 503:
                errorMessage = "OpenAI service unavailable";
                logger.warning("OpenAI API: Service unavailable (code: " + code + ")");
                break;
            default:
                errorMessage = "API error (code: " + code + ")";
                logger.warning("OpenAI API error " + code + ": " + errorBody);
        }
        
        return AIResponse.failure(errorMessage);
    }
    
    @Override
    public void shutdown() {
        if (httpClient != null) {
            httpClient.dispatcher().executorService().shutdown();
            httpClient.connectionPool().evictAll();
        }
        initialized = false;
        logger.info("OpenAI provider shut down");
    }
}
