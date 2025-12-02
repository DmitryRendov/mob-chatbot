package me.drendov.MOBChatBot;

import me.drendov.MOBChatBot.ai.AIProvider;
import me.drendov.MOBChatBot.ai.AIProviderFactory;
import me.drendov.MOBChatBot.commands.ChatCommand;
import me.drendov.MOBChatBot.commands.ReloadCommand;
import me.drendov.MOBChatBot.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for MOBChatBot
 * Provides AI-powered chat conversations for Minecraft players
 */
public class MOBChatBot extends JavaPlugin {
    
    private ConfigManager configManager;
    private AIProvider aiProvider;

    @Override
    public void onEnable() {
        getLogger().info("========================================");
        getLogger().info("MOBChatBot is starting...");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("========================================");
        
        // Initialize configuration
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        // Initialize AI provider
        initializeAIProvider();
        
        // Register commands
        registerCommands();
        
        getLogger().info("MOBChatBot has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("========================================");
        getLogger().info("MOBChatBot is shutting down...");
        getLogger().info("========================================");
        
        // Cleanup AI provider connections
        if (aiProvider != null) {
            aiProvider.shutdown();
            aiProvider = null;
        }
        
        getLogger().info("MOBChatBot has been disabled successfully!");
    }
    
    /**
     * Initialize the AI provider based on configuration
     */
    private void initializeAIProvider() {
        AIProviderFactory factory = new AIProviderFactory(getLogger(), configManager);
        aiProvider = factory.createProvider();
        
        if (aiProvider != null) {
            getLogger().info("AI Provider initialized: " + aiProvider.getProviderName());
        } else {
            getLogger().severe("Failed to initialize any AI provider!");
            getLogger().severe("Please check your configuration and enable at least one provider.");
        }
    }
    
    /**
     * Reinitialize the AI provider (used after config reload)
     */
    public void reinitializeAIProvider() {
        // Shutdown existing provider
        if (aiProvider != null) {
            aiProvider.shutdown();
            aiProvider = null;
        }
        
        // Initialize new provider
        initializeAIProvider();
    }
    
    /**
     * Register all plugin commands
     */
    private void registerCommands() {
        getCommand("mobchat").setExecutor(new ChatCommand(this));
        getCommand("mobchatreload").setExecutor(new ReloadCommand(this));
        getLogger().info("Commands registered successfully");
    }
    
    /**
     * Get the configuration manager
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    /**
     * Get the AI provider
     */
    public AIProvider getAIProvider() {
        return aiProvider;
    }
}