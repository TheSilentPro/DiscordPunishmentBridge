package tsp.dpb;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.shanerx.mojang.Mojang;
import tsp.dpb.command.Command_dpb;
import tsp.dpb.event.minecraft.Event_advancedban;
import tsp.dpb.event.minecraft.Event_litebans;
import tsp.dpb.event.minecraft.Event_punishevent;
import tsp.dpb.util.FileUtils;
import tsp.dpb.util.LogLevel;
import tsp.dpb.util.Metrics;
import tsp.dpb.util.Utils;

import javax.security.auth.login.LoginException;

public class Core extends JavaPlugin {

    public static JDA jda;
    public static WebhookClient client;

    @Override
    public void onEnable() {
        Utils.log(LogLevel.INFO, "Loading " + getDescription().getName() + " - " + getDescription().getVersion());

        Utils.debug("Loading config files...");
        saveDefaultConfig();
        updateFiles();

        if (!FileUtils.getBoolean("enabled")) {
            Utils.log(LogLevel.WARNING, "** Plugin is disabled by config.yml **");
            this.setEnabled(false);
            return;
        }

        if (!FileUtils.getBoolean("webhook.enabled")) {
            Utils.debug("Attempting to login to the bot...");
            try {
                jda = new JDABuilder(FileUtils.getString("bot.token"))
                        .setActivity(Activity.of(Activity.ActivityType.valueOf(FileUtils.getString("bot.activityType")), FileUtils.getString("bot.activityName")))
                        .setStatus(OnlineStatus.valueOf(FileUtils.getString("bot.onlineStatus")))
                        .build();

                Utils.debug("Logged in! Bot ID: " + jda.getSelfUser().getId());
                Utils.log(LogLevel.BOT_INFO, "Successfully logged in as " + jda.getSelfUser().getName() + "#" + jda.getSelfUser().getDiscriminator());
            } catch (LoginException e) {
                Utils.log(LogLevel.BOT_ERROR, "Failed to login as bot | Stack Trace: ");
                e.printStackTrace();
                this.setEnabled(false);
                return;
            }
        }
        if (FileUtils.getBoolean("webhook.enabled")) {
            Utils.debug("Building webhook client...");
            WebhookClientBuilder builder = new WebhookClientBuilder(FileUtils.getString("webhook.url"));
            builder.setThreadFactory((job) -> {
                Thread thread = new Thread(job);
                thread.setName(FileUtils.getString("webhook.name"));
                thread.setDaemon(true);
                return thread;
            });
            builder.setWait(true);
            client = builder.build();
            Utils.log(LogLevel.INFO, "WebHookClient initialized!");
        }

        Utils.debug("Starting metrics...");
        Metrics metrics = new Metrics(this, Utils.METRICS_ID); // Please do not remove metrics related code, it helps out the plugin <3

        Utils.debug("Registering commands & listeners...");
        registerMinecraftCommands();
        registerEvents();
        Utils.mAPI = new Mojang().connect();

        Utils.debug("[JDAInfo] VERSION: " + JDAInfo.VERSION + " | REST: " + JDAInfo.DISCORD_REST_VERSION);
        Utils.log(LogLevel.INFO, "Done!");
    }

    @Override
    public void onDisable() {
        Utils.log(LogLevel.INFO, "Shutting down DPB...");
        if (jda != null) {
            Utils.log(LogLevel.BOT_INFO, "Shutting down jda...");
            jda.shutdown();
        }
        if (client != null) {
            Utils.log(LogLevel.INFO, "Shutting down WebHookClient...");
            client.close();
        }
        Utils.log(LogLevel.INFO, "Shutdown Finished!");
    }

    private void registerMinecraftCommands() {
        getCommand("discordpunishmentbridge").setExecutor(new Command_dpb());
    }

    private void registerEvents() {
        String pPlugin = FileUtils.getString("punishmentPlugin");
        if (pPlugin.equalsIgnoreCase("detect")) {
            pPlugin = Utils.detect();
            Utils.debug("Detected '" + pPlugin + "'");
        }
        if (pPlugin == null) {
            Utils.log(LogLevel.ERROR, "Failed to detect plugin '" + FileUtils.getString("punishmentPlugin") + "', Please make sure it matches the available plugins!");
            this.setEnabled(false);
            return;
        }
        if (pPlugin.equalsIgnoreCase("litebans")) {
            new Event_litebans().registerEvents();
        }
        if (pPlugin.equalsIgnoreCase("advancedban")) {
            Bukkit.getPluginManager().registerEvents(new Event_advancedban(), this);
        }

        Bukkit.getPluginManager().registerEvents(new Event_punishevent(), this);
    }

    private void updateFiles() {
        boolean update = false;

        if (!getConfig().isSet("config-version") || !getConfig().getString("config-version").equals(Utils.CONFIG_VERSION)) {
            saveResource("config.yml", true);
            update = true;
        }

        if (update) Utils.debug("Updating configuration files...");
    }

}
