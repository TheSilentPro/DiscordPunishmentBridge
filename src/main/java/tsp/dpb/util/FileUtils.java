package tsp.dpb.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    private static final File configFile = new File(Utils.getInstance().getDataFolder(), "config.yml");
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

    public static String getString(String identifier) {
        return getConfig().getString(identifier);
    }

    public static String getString(String identifier, String executor, String victim, String type, String reason, String duration, boolean isIp) {
        return Utils.translate(identifier, executor, victim, type, reason, duration, isIp);
    }

    public static boolean getBoolean(String identifier) {
        return getConfig().getBoolean(identifier);
    }

    public static File getConfigFile() {
        return configFile;
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static void createFiles() {
        if (configFile.exists()) {
            try {
                configFile.createNewFile();
                Utils.debug("Created config.yml");
            } catch (IOException e) {
                Utils.log(LogLevel.ERROR, "Could not create config.yml | Stack Trace: ");
                e.printStackTrace();
            }
        }
    }

    public static void saveFiles() {
        try {
            config.save(configFile);
            Utils.debug("Saved config.yml");
        } catch (IOException e) {
            Utils.log(LogLevel.ERROR, "Failed to save config.yml | Stack Trace: ");
            e.printStackTrace();
        }
    }

}
