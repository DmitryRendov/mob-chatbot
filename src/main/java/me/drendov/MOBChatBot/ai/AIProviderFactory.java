package me.drendov.MOBChatBot.ai;

import me.drendov.MOBChatBot.ai.providers.BedrockProvider;
import me.drendov.MOBChatBot.ai.providers.OpenAIProvider;
import me.drendov.MOBChatBot.config.ConfigManager;

import java.util.logging.Logger;

/**
 * Factory class for creating AI provider instances based on configuration
 */
public class AIProviderFactory {
    
    private final Logger logger;
    private final ConfigManager configManager;
    
    public AIProviderFactory(Logger logger, ConfigManager configManager) {
        this.logger = logger;
        this.configManager = configManager;
    }
    
    /**
     * Create and initialize the appropriate AI provider based on configuration
     * @return Initialized AIProvider instance, or null if no provider is enabled/configured
     */
    public AIProvider createProvider() {
        // Try OpenAI first
        if (configManager.isOpenAIEnabled()) {
            logger.info("Creating OpenAI provider...");
            OpenAIProvider provider = new OpenAIProvider(
                logger,
                configManager.getOpenAIApiKey(),
                configManager.getOpenAIModel(),
                configManager.getOpenAIMaxTokens()
            );
            
            if (provider.initialize()) {
                return provider;
            } else {
                logger.warning("OpenAI provider failed to initialize");
            }
        }
        
        // Try Bedrock
        if (configManager.isBedrockEnabled()) {
            logger.info("Creating Bedrock provider...");
            BedrockProvider provider = new BedrockProvider(
                logger,
                configManager.getBedrockRegion(),
                configManager.getBedrockAccessKey(),
                configManager.getBedrockSecretKey(),
                configManager.getBedrockModel()
            );
            
            if (provider.initialize()) {
                return provider;
            } else {
                logger.warning("Bedrock provider failed to initialize");
            }
        }
        
        // Try Ollama (future implementation)
        if (configManager.isOllamaEnabled()) {
            logger.warning("Ollama provider is not yet implemented");
        }
        
        logger.severe("No AI provider could be initialized!");
        return null;
    }
    
    /**
     * Get the name of the provider that would be created
     * @return Provider name or "None" if no provider is available
     */
    public String getAvailableProviderName() {
        if (configManager.isOpenAIEnabled()) {
            return "OpenAI";
        }
        if (configManager.isBedrockEnabled()) {
            return "Bedrock";
        }
        if (configManager.isOllamaEnabled()) {
            return "Ollama (not implemented)";
        }
        return "None";
    }
}
