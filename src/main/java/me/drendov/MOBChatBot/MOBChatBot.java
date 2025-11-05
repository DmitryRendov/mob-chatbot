package me.drendov.MOBChatBot;

import me.drendov.MOBChatBot.commands.*;
import me.drendov.MOBChatBot.event.MovementListener;
import org.bukkit.plugin.java.JavaPlugin;

public class MOBChatBot extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("enrich").setExecutor(new EnrichCommand());
        getLogger().info("Added the 'enrich' command.");
    }
}