package me.drendov.MOBChatBot;

import me.drendov.MOBChatBot.commands.ChatCommand;
import me.drendov.MOBChatBot.commands.ReloadCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for MOBChatBot
 * Provides AI-powered chat conversations for Minecraft players
 */
public class MOBChatBot extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("========================================");
        getLogger().info("MOBChatBot is starting...");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("========================================");
        
        // Initialize configuration
        saveDefaultConfig();
        
        // Register commands
        registerCommands();
        
        // TODO: Initialize AI providers
        // TODO: Register event listeners
        
        getLogger().info("MOBChatBot has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("========================================");
        getLogger().info("MOBChatBot is shutting down...");
        getLogger().info("========================================");
        
        // TODO: Save any pending data
        // TODO: Cleanup AI provider connections
        
        getLogger().info("MOBChatBot has been disabled successfully!");
    }
    
    /**
     * Register all plugin commands
     */
    private void registerCommands() {
        getCommand("mobchat").setExecutor(new ChatCommand(this));
        getCommand("mobchatreload").setExecutor(new ReloadCommand(this));
        getLogger().info("Commands registered successfully");
    }
}