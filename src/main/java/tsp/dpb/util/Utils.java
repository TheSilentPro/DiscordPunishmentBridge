package tsp.dpb.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.shanerx.mojang.Mojang;

import java.util.Collections;
import java.util.List;

public class Utils {

    public final static int METRICS_ID = 7384; // Please do not remove metrics related code, it helps out the plugin <3
    public final static String PREFIX = "&7[&9DiscordPunishmentBridge&7] ";
    public final static List<String> DEVELOPERS = Collections.singletonList("Silent");
    public final static String CONFIG_VERSION = "1.0";
    public static Mojang mAPI;

    /**
     * Detect punishment plugin
     *
     * @return Detected punishment plugin
     */
    public static String detect() {
        String[] plugins = { "Litebans", "AdvancedBan", "MaxBansPlus" };
        for (String plugin : plugins) {
            if (Bukkit.getPluginManager().isPluginEnabled(plugin)) {
                return plugin;
            }
        }
        return null;
    }

    /**
     * Send a message to a minecraft CommandSender
     *
     * @param sender The command sender
     * @param message The message
     */
    public static void sendMessage(CommandSender sender, String message) {
        message = colorize(PREFIX + message.replace("%sender%", sender.getName()));
        if (isPAPIInstalled()) {
            if (sender instanceof Player) {
                message = PlaceholderAPI.setPlaceholders((Player) sender, message);
            }
        }
        sender.sendMessage(message);
    }

    /**
     * Check if PlaceholderAPI is installed
     *
     * @return If PlaceholderAPI is installed
     */
    public static boolean isPAPIInstalled() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    /**
     * Log a message to console
     *
     * @param level Severity of the log
     * @param message The log message
     */
    public static void log(LogLevel level, String message) {
        Bukkit.getConsoleSender().sendMessage(colorize(PREFIX + getColorByLevel(level) + "[" + level.name() + "] " + message));
    }

    /**
     * Send debug message to console
     *
     * @param message The message
     */
    public static void debug(String message) {
        if (FileUtils.getBoolean("debug")) {
            Bukkit.getConsoleSender().sendMessage(colorize(PREFIX + "&9[DEBUG] ") + message);
        }
    }

    /**
     * Get color by level of log
     *
     * @param level The log level
     * @return Level color
     */
    private static ChatColor getColorByLevel(LogLevel level) {
        switch (level) {
            case INFO:
            case BOT_INFO:
                return ChatColor.GREEN;
            case WARNING:
            case BOT_WARNING:
                return ChatColor.YELLOW;
            case ERROR:
            case BOT_ERROR:
                return ChatColor.DARK_RED;
            default:
                return ChatColor.WHITE;
        }
    }

    /**
     * Translate a punishment message
     * Used for 'message' in config.yml
     *
     * @param message The message
     * @param executor The executor
     * @param victim The victim
     * @param type The type of punishment
     * @param reason The reason of the punishment
     * @param duration The duraiton of the punishment
     * @param isIp If the punishment is an IP Punishment
     * @return The translated message
     */
    public static String translate(String message, String executor, String victim, String type, String reason, String duration, boolean isIp) {
        String username = victim;
        try {
            username = mAPI.getPlayerProfile(victim.replace("-", "")).getUsername();
        } catch (NullPointerException ex) {
            // ignored
        }
        message = colorize(message
                .replace("%executor%", executor)
                .replace("%victim%", username)
                .replace("%type%", type)
                .replace("%reason%", reason)
                .replace("%duration%", duration)
                .replace("%isIp%", String.valueOf(isIp)));

        return message;
    }

    /**
     * Translate color codes from message
     *
     * @param message The message to translate from
     * @return The translated message
     */
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Get an instance of this plugin
     *
     * @return Plugin Instance
     */
    public static Plugin getInstance() {
        return Bukkit.getPluginManager().getPlugin("DiscordPunishmentBridge");
    }

}
