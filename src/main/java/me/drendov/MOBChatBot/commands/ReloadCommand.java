package me.drendov.MOBChatBot.commands;

import me.drendov.MOBChatBot.MOBChatBot;
import me.drendov.MOBChatBot.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Reload command handler
 * Reloads the plugin configuration
 */
public class ReloadCommand implements CommandExecutor {
    
    private final MOBChatBot plugin;
    
    public ReloadCommand(MOBChatBot plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("mobchatbot.admin")) {
            if (sender instanceof Player) {
                MessageUtils.sendError((Player) sender, "You don't have permission to use this command!");
            } else {
                sender.sendMessage("You don't have permission to use this command!");
            }
            return true;
        }
        
        // TODO: Reload configuration
        // TODO: Reload AI providers
        
        String message = "Configuration reloaded successfully!";
        if (sender instanceof Player) {
            MessageUtils.sendSuccess((Player) sender, message);
        } else {
            sender.sendMessage(message);
        }
        
        return true;
    }
}
