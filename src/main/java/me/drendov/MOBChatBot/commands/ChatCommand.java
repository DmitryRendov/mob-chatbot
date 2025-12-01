package me.drendov.MOBChatBot.commands;

import me.drendov.MOBChatBot.MOBChatBot;
import me.drendov.MOBChatBot.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        
        // TODO: Combine args into message
        // TODO: Send to AI provider
        // TODO: Display response
        
        MessageUtils.sendMessage(player, "AI chat functionality coming soon!");
        return true;
    }
}
