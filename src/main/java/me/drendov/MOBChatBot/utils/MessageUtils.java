package me.drendov.MOBChatBot.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Utility class for message formatting and sending
 */
public class MessageUtils {
    
    private static final String PREFIX = ChatColor.DARK_PURPLE + "[MOBChat] " + ChatColor.RESET;
    
    /**
     * Send a formatted message to a player
     * @param player The player to send the message to
     * @param message The message content
     */
    public static void sendMessage(Player player, String message) {
        player.sendMessage(PREFIX + message);
    }
    
    /**
     * Send an error message to a player
     * @param player The player to send the message to
     * @param message The error message
     */
    public static void sendError(Player player, String message) {
        player.sendMessage(PREFIX + ChatColor.RED + message);
    }
    
    /**
     * Send a success message to a player
     * @param player The player to send the message to
     * @param message The success message
     */
    public static void sendSuccess(Player player, String message) {
        player.sendMessage(PREFIX + ChatColor.GREEN + message);
    }
    
    /**
     * Format an AI response for display
     * @param response The AI's response
     * @return Formatted response
     */
    public static String formatAIResponse(String response) {
        return ChatColor.AQUA + response;
    }
}
