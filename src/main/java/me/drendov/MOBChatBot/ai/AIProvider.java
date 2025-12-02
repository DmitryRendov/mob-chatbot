package me.drendov.MOBChatBot.ai;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for AI providers
 * Supports multiple backends: OpenAI, AWS Bedrock
 */
public interface AIProvider {
    
    /**
     * Send a message to the AI and get a response asynchronously
     * @param message The user's message
     * @param conversationHistory Previous messages in the conversation
     * @return CompletableFuture containing the AI's response
     */
    CompletableFuture<AIResponse> sendMessage(String message, List<ConversationMessage> conversationHistory);
    
    /**
     * Check if the provider is properly configured
     * @return true if all required configuration is present and valid
     */
    boolean isConfigured();
    
    /**
     * Get the provider name
     * @return Provider identifier (e.g., "OpenAI", "Bedrock")
     */
    String getProviderName();
    
    /**
     * Initialize the provider with configuration
     * @return true if initialization was successful
     */
    boolean initialize();
    
    /**
     * Cleanup and close connections
     */
    void shutdown();
}
