package dev.ex4.consolelistener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class ConsoleListenerCommand implements CommandExecutor {
    public List<Player> consoleListeners = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender.hasPermission("consolelistener.listener"))) {
            sender.sendMessage(getConfigString("messages.no-permission", "&cInsufficient permission."));
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            ConsoleListener.INSTANCE.saveDefaultConfig();
            ConsoleListener.INSTANCE.reloadConfig();
            sender.sendMessage(getConfigString("messages.config-reloaded", "&aConfiguration reloaded successfully."));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(getConfigString("messages.no-console", "&cYou must be a player to execute this command."));
            return true;
        }
        Player p = (Player) sender;
        if (consoleListeners.contains(p)) {
            p.sendMessage(getConfigString("messages.listen-off", "&aYou are no longer listening to console messages."));
            consoleListeners.remove(p);
            ConsoleListener.INSTANCE.getLogger().info(p.getName() + " is no longer listening to console messages.");
         }
        else {
            p.sendMessage(getConfigString("messages.listen-on", "&aYou are now listening to console messages."));
            consoleListeners.add(p);
            ConsoleListener.INSTANCE.getLogger().info(p.getName() + " is now listening to console messages.");
        }
        return true;
    }
    public static String getConfigString(String path, String def) {
        return ChatColor.translateAlternateColorCodes('&', ConsoleListener.INSTANCE.getConfig().getString(path, def));
    }
}
