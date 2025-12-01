package me.drendov.MOBChatBot.config;

import me.drendov.MOBChatBot.MOBChatBot;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

/**
 * Manages plugin configuration
 * Handles loading, validating, and providing access to configuration values
 */
public class ConfigManager {
    
    private final MOBChatBot plugin;
    private FileConfiguration config;
    
    // AI Provider settings
    private boolean openAIEnabled;
    private String openAIApiKey;
    private String openAIModel;
    private int openAIMaxTokens;
    
    private boolean bedrockEnabled;
    private String bedrockRegion;
    private String bedrockAccessKey;
    private String bedrockSecretKey;
    private String bedrockModel;
    
    private boolean ollamaEnabled;
    private String ollamaBaseUrl;
    private String ollamaModel;
    
    // General settings
    private int maxMessagesPerPlayer;
    private int cooldownSeconds;
    private String botName;
    private String systemPrompt;
    
    // Messages
    private String messagePrefix;
    private String cooldownMessage;
    private String limitReachedMessage;
    private String errorMessage;
    private String noProviderMessage;
    
    public ConfigManager(MOBChatBot plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Load configuration from config.yml
     * Creates default config if it doesn't exist
     */
    public void loadConfig() {
        // Save default config if it doesn't exist
        plugin.saveDefaultConfig();
        
        // Reload config from disk
        plugin.reloadConfig();
        config = plugin.getConfig();
        
        plugin.getLogger().info("Loading configuration...");
        
        // Load AI provider settings
        loadAIProviderSettings();
        
        // Load general settings
        loadGeneralSettings();
        
        // Load messages
        loadMessages();
        
        // Validate configuration
        validateConfig();
        
        plugin.getLogger().info("Configuration loaded successfully!");
    }
    
    /**
     * Reload configuration from disk
     */
    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }
    
    /**
     * Load AI provider settings from config
     */
    private void loadAIProviderSettings() {
        // OpenAI
        openAIEnabled = config.getBoolean("ai-providers.openai.enabled", false);
        openAIApiKey = config.getString("ai-providers.openai.api-key", "");
        openAIModel = config.getString("ai-providers.openai.model", "gpt-3.5-turbo");
        openAIMaxTokens = config.getInt("ai-providers.openai.max-tokens", 150);
        
        // Bedrock
        bedrockEnabled = config.getBoolean("ai-providers.bedrock.enabled", false);
        bedrockRegion = config.getString("ai-providers.bedrock.region", "us-east-1");
        bedrockAccessKey = config.getString("ai-providers.bedrock.access-key", "");
        bedrockSecretKey = config.getString("ai-providers.bedrock.secret-key", "");
        bedrockModel = config.getString("ai-providers.bedrock.model", "claude-3-haiku");
        
        // Ollama
        ollamaEnabled = config.getBoolean("ai-providers.ollama.enabled", false);
        ollamaBaseUrl = config.getString("ai-providers.ollama.base-url", "http://localhost:11434");
        ollamaModel = config.getString("ai-providers.ollama.model", "llama2");
        
        plugin.getLogger().info("AI Providers - OpenAI: " + openAIEnabled + 
                              ", Bedrock: " + bedrockEnabled + 
                              ", Ollama: " + ollamaEnabled);
    }
    
    /**
     * Load general settings from config
     */
    private void loadGeneralSettings() {
        maxMessagesPerPlayer = config.getInt("general.max-messages-per-player", 10);
        cooldownSeconds = config.getInt("general.cooldown-seconds", 5);
        botName = config.getString("general.bot-name", "MOBChat");
        systemPrompt = config.getString("general.system-prompt", 
            "You are a helpful assistant in a Minecraft server. Keep responses concise and fun.");
        
        plugin.getLogger().info("General Settings - Max Messages: " + maxMessagesPerPlayer + 
                              ", Cooldown: " + cooldownSeconds + "s");
    }
    
    /**
     * Load message templates from config
     */
    private void loadMessages() {
        messagePrefix = config.getString("messages.prefix", "&5[MOBChat]&r");
        cooldownMessage = config.getString("messages.cooldown", 
            "Please wait {seconds} seconds before sending another message.");
        limitReachedMessage = config.getString("messages.limit-reached", 
            "You have reached your message limit ({limit} messages).");
        errorMessage = config.getString("messages.error", 
            "An error occurred while processing your message. Please try again.");
        noProviderMessage = config.getString("messages.no-provider", 
            "No AI provider is currently enabled. Contact an administrator.");
    }
    
    /**
     * Validate configuration values
     */
    private void validateConfig() {
        boolean hasEnabledProvider = openAIEnabled || bedrockEnabled || ollamaEnabled;
        
        if (!hasEnabledProvider) {
            plugin.getLogger().warning("No AI provider is enabled! The plugin will not function properly.");
            plugin.getLogger().warning("Please enable at least one provider in config.yml");
        }
        
        if (openAIEnabled && (openAIApiKey == null || openAIApiKey.isEmpty() || openAIApiKey.equals("your-api-key-here"))) {
            plugin.getLogger().warning("OpenAI is enabled but no API key is configured!");
        }
        
        if (bedrockEnabled && (bedrockAccessKey == null || bedrockAccessKey.isEmpty() || bedrockAccessKey.equals("your-access-key"))) {
            plugin.getLogger().warning("Bedrock is enabled but credentials are not configured!");
        }
        
        if (maxMessagesPerPlayer < 0) {
            plugin.getLogger().warning("max-messages-per-player cannot be negative! Setting to 10.");
            maxMessagesPerPlayer = 10;
        }
        
        if (cooldownSeconds < 0) {
            plugin.getLogger().warning("cooldown-seconds cannot be negative! Setting to 5.");
            cooldownSeconds = 5;
        }
        
        if (openAIMaxTokens <= 0) {
            plugin.getLogger().warning("OpenAI max-tokens must be positive! Setting to 150.");
            openAIMaxTokens = 150;
        }
    }
    
    // Getters for AI Provider settings
    
    public boolean isOpenAIEnabled() {
        return openAIEnabled;
    }
    
    public String getOpenAIApiKey() {
        return openAIApiKey;
    }
    
    public String getOpenAIModel() {
        return openAIModel;
    }
    
    public int getOpenAIMaxTokens() {
        return openAIMaxTokens;
    }
    
    public boolean isBedrockEnabled() {
        return bedrockEnabled;
    }
    
    public String getBedrockRegion() {
        return bedrockRegion;
    }
    
    public String getBedrockAccessKey() {
        return bedrockAccessKey;
    }
    
    public String getBedrockSecretKey() {
        return bedrockSecretKey;
    }
    
    public String getBedrockModel() {
        return bedrockModel;
    }
    
    public boolean isOllamaEnabled() {
        return ollamaEnabled;
    }
    
    public String getOllamaBaseUrl() {
        return ollamaBaseUrl;
    }
    
    public String getOllamaModel() {
        return ollamaModel;
    }
    
    // Getters for general settings
    
    public int getMaxMessagesPerPlayer() {
        return maxMessagesPerPlayer;
    }
    
    public int getCooldownSeconds() {
        return cooldownSeconds;
    }
    
    public String getBotName() {
        return botName;
    }
    
    public String getSystemPrompt() {
        return systemPrompt;
    }
    
    // Getters for messages
    
    public String getMessagePrefix() {
        return messagePrefix;
    }
    
    public String getCooldownMessage() {
        return cooldownMessage;
    }
    
    public String getLimitReachedMessage() {
        return limitReachedMessage;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public String getNoProviderMessage() {
        return noProviderMessage;
    }
    
    /**
     * Check if at least one AI provider is enabled
     */
    public boolean hasEnabledProvider() {
        return openAIEnabled || bedrockEnabled || ollamaEnabled;
    }
    
    /**
     * Get the name of the first enabled provider
     */
    public String getEnabledProviderName() {
        if (openAIEnabled) return "OpenAI";
        if (bedrockEnabled) return "Bedrock";
        if (ollamaEnabled) return "Ollama";
        return "None";
    }
}
