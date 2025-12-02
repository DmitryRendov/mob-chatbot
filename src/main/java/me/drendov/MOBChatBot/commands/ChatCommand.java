package me.drendov.MOBChatBot.commands;

import me.drendov.MOBChatBot.MOBChatBot;
import me.drendov.MOBChatBot.ai.AIProvider;
import me.drendov.MOBChatBot.ai.AIResponse;
import me.drendov.MOBChatBot.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Main chat command handler
 * Processes player messages and sends them to the AI
 */
public class ChatCommand implements CommandExecutor {
    
    private final MOBChatBot plugin;
    
    public ChatCommand(MOBChatBot plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("mobchatbot.use")) {
            MessageUtils.sendError(player, "You don't have permission to use this command!");
            return true;
        }
        
        if (args.length == 0) {
            MessageUtils.sendError(player, "Usage: /mobchat <message>");
            return true;
        }
        
        // Check if AI provider is available
        AIProvider aiProvider = plugin.getAIProvider();
        if (aiProvider == null) {
            MessageUtils.sendError(player, plugin.getConfigManager().getNoProviderMessage());
            return true;
        }
        
        // Combine args into message
        String message = String.join(" ", args);
        
        // Send typing indicator
        MessageUtils.sendMessage(player, "Thinking...");
        
        // Send to AI provider asynchronously
        aiProvider.sendMessage(message, new ArrayList<>())
            .thenAccept(response -> {
                handleAIResponse(player, response);
            })
            .exceptionally(throwable -> {
                plugin.getLogger().severe("Error processing AI request: " + throwable.getMessage());
                MessageUtils.sendError(player, plugin.getConfigManager().getErrorMessage());
                return null;
            });
        
        return true;
    }
    
    /**
     * Handle the AI response and send it to the player
     */
    private void handleAIResponse(Player player, AIResponse response) {
        if (response.isSuccess()) {
            // Send the AI's response to the player
            String formattedResponse = MessageUtils.formatAIResponse(response.getContent());
            player.sendMessage(formattedResponse);
            
            // Log token usage
            plugin.getLogger().info(String.format(
                "Player %s - AI response delivered (%d tokens used)",
                player.getName(),
                response.getTokensUsed()
            ));
        } else {
            // Send error message
            MessageUtils.sendError(player, "AI Error: " + response.getErrorMessage());
            plugin.getLogger().warning(String.format(
                "AI request failed for player %s: %s",
                player.getName(),
                response.getErrorMessage()
            ));
        }
    }
}
