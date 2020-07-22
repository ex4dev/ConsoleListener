package dev.ex4.consolelistener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


public final class ConsoleListener extends JavaPlugin {
    public static ConsoleListener INSTANCE;
    private ConsoleListenerCommand consoleListenerCommand;
    private static boolean handlerRegistered = false;
    @Override
    public void onEnable() {
        INSTANCE = this;

        saveDefaultConfig();
        reloadConfig();
        consoleListenerCommand = new ConsoleListenerCommand();
        this.getCommand("consolelistener").setExecutor(consoleListenerCommand);
        if (handlerRegistered) return;
        Handler handler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                for (Player p : consoleListenerCommand.consoleListeners) {
                    p.sendMessage(ConsoleListenerCommand.getConfigString("messages.message-format", "&8> [{logger}] {message}").replace("{logger}", record.getLoggerName()).replace("{message}", record.getMessage()));
                }
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {
                consoleListenerCommand.consoleListeners.clear();
            }
        };
        Logger logger = Logger.getLogger("");
        logger.addHandler(handler);
        handlerRegistered = true;
    }

    @Override
    public void onDisable() {
        consoleListenerCommand.consoleListeners.clear(); // Prevent messages from appearing twice after a /reload
    }
}
