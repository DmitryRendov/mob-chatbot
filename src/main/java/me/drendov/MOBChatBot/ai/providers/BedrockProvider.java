package me.drendov.MOBChatBot.ai.providers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.drendov.MOBChatBot.ai.AIProvider;
import me.drendov.MOBChatBot.ai.AIResponse;
import me.drendov.MOBChatBot.ai.ConversationMessage;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * AWS Bedrock provider implementation for Claude models
 */
public class BedrockProvider implements AIProvider {
    
    private static final String SYSTEM_PROMPT = "You are a helpful Minecraft companion. Keep responses brief and Minecraft-focused.";
    
    private final Logger logger;
    private final String region;
    private final String accessKey;
    private final String secretKey;
    private final String model;
    
    private BedrockRuntimeClient bedrockClient;
    private boolean initialized = false;
    
    public BedrockProvider(Logger logger, String region, String accessKey, String secretKey, String model) {
        this.logger = logger;
        this.region = region;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.model = model;
    }
    
    @Override
    public boolean initialize() {
        if (!isConfigured()) {
            logger.warning("Bedrock provider is not properly configured!");
            return false;
        }
        
        try {
            // Create AWS credentials
            AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
            
            // Build Bedrock Runtime client
            bedrockClient = BedrockRuntimeClient.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();
            
            initialized = true;
            logger.info("Bedrock provider initialized successfully with model: " + model);
            return true;
            
        } catch (Exception e) {
            logger.severe("Failed to initialize Bedrock provider: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isConfigured() {
        return region != null && !region.isEmpty() &&
               accessKey != null && !accessKey.isEmpty() && !accessKey.equals("your-access-key") &&
               secretKey != null && !secretKey.isEmpty() && !secretKey.equals("your-secret-key") &&
               model != null && !model.isEmpty();
    }
    
    @Override
    public String getProviderName() {
        return "Bedrock";
    }
    
    @Override
    public CompletableFuture<AIResponse> sendMessage(String message, List<ConversationMessage> conversationHistory) {
        if (!initialized || !isConfigured()) {
            return CompletableFuture.completedFuture(
                AIResponse.failure("Bedrock provider is not properly configured")
            );
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Build request JSON for Claude
                JsonObject requestJson = buildClaudeRequest(message, conversationHistory);
                
                // Create Bedrock request
                InvokeModelRequest request = InvokeModelRequest.builder()
                        .modelId(model)
                        .contentType("application/json")
                        .accept("application/json")
                        .body(SdkBytes.fromUtf8String(requestJson.toString()))
                        .build();
                
                // Invoke model
                InvokeModelResponse response = bedrockClient.invokeModel(request);
                
                // Parse response
                String responseBody = response.body().asUtf8String();
                return parseClaudeResponse(responseBody);
                
            } catch (software.amazon.awssdk.services.bedrockruntime.model.ThrottlingException e) {
                logger.warning("Bedrock API: Rate limit exceeded");
                return AIResponse.failure("Rate limit exceeded. Please try again later.");
                
            } catch (software.amazon.awssdk.services.bedrockruntime.model.ValidationException e) {
                logger.severe("Bedrock API: Validation error - " + e.getMessage());
                return AIResponse.failure("Invalid request: " + e.getMessage());
                
            } catch (software.amazon.awssdk.services.bedrockruntime.model.ServiceQuotaExceededException e) {
                logger.warning("Bedrock API: Service quota exceeded");
                return AIResponse.failure("Service quota exceeded. Contact administrator.");
                
            } catch (Exception e) {
                logger.severe("Bedrock API error: " + e.getMessage());
                e.printStackTrace();
                return AIResponse.failure("Unexpected error: " + e.getMessage());
            }
        });
    }
    
    /**
     * Build request JSON for Claude model
     */
    private JsonObject buildClaudeRequest(String message, List<ConversationMessage> conversationHistory) {
        JsonObject json = new JsonObject();
        
        // Add system prompt
        json.addProperty("system", SYSTEM_PROMPT);
        
        // Build messages array
        JsonArray messages = new JsonArray();
        
        // Add conversation history
        if (conversationHistory != null) {
            for (ConversationMessage msg : conversationHistory) {
                JsonObject historyMessage = new JsonObject();
                historyMessage.addProperty("role", msg.getRole());
                
                // Claude expects content as array
                JsonArray contentArray = new JsonArray();
                JsonObject textContent = new JsonObject();
                textContent.addProperty("type", "text");
                textContent.addProperty("text", msg.getContent());
                contentArray.add(textContent);
                
                historyMessage.add("content", contentArray);
                messages.add(historyMessage);
            }
        }
        
        // Add current user message
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        
        JsonArray contentArray = new JsonArray();
        JsonObject textContent = new JsonObject();
        textContent.addProperty("type", "text");
        textContent.addProperty("text", message);
        contentArray.add(textContent);
        
        userMessage.add("content", contentArray);
        messages.add(userMessage);
        
        json.add("messages", messages);
        json.addProperty("max_tokens", 512);
        json.addProperty("temperature", 0.7);
        json.addProperty("anthropic_version", "bedrock-2023-05-31");
        
        return json;
    }
    
    /**
     * Parse response from Claude model
     */
    private AIResponse parseClaudeResponse(String responseBody) {
        try {
            JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
            
            // Extract content from Claude response format
            JsonArray contentArray = json.getAsJsonArray("content");
            String content = contentArray.get(0).getAsJsonObject()
                    .get("text").getAsString();
            
            // Extract token usage
            int tokensUsed = 0;
            if (json.has("usage")) {
                JsonObject usage = json.getAsJsonObject("usage");
                int inputTokens = usage.has("input_tokens") ? usage.get("input_tokens").getAsInt() : 0;
                int outputTokens = usage.has("output_tokens") ? usage.get("output_tokens").getAsInt() : 0;
                tokensUsed = inputTokens + outputTokens;
            }
            
            return AIResponse.success(content, tokensUsed);
            
        } catch (Exception e) {
            logger.severe("Failed to parse Bedrock response: " + e.getMessage());
            return AIResponse.failure("Failed to parse response: " + e.getMessage());
        }
    }
    
    @Override
    public void shutdown() {
        if (bedrockClient != null) {
            bedrockClient.close();
        }
        initialized = false;
        logger.info("Bedrock provider shut down");
    }
}
