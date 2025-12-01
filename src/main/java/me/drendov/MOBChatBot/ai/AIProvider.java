package me.drendov.MOBChatBot.ai;

/**
 * Interface for AI providers
 * Supports multiple backends: OpenAI, AWS Bedrock, Ollama
 */
public interface AIProvider {
    
    /**
     * Send a message to the AI and get a response
     * @param message The user's message
     * @return The AI's response
     */
    String sendMessage(String message);
    
    /**
     * Get the provider name
     * @return Provider identifier (e.g., "openai", "bedrock", "ollama")
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
