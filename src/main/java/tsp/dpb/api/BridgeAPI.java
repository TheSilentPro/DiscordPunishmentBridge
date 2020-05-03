package tsp.dpb.api;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import tsp.dpb.Core;
import tsp.dpb.util.FileUtils;
import tsp.dpb.util.LogLevel;
import tsp.dpb.util.Utils;

public class BridgeAPI {

    // TODO: Find a better way than this bullshit of a translation for each string
    public static void sendDiscordPunishmentWebHook(String executor, String victim, String type, String reason, String duration, boolean isIp) {
        if (Core.client != null) {
            WebhookEmbedBuilder embed = new WebhookEmbedBuilder();

            String desc = Utils.translate(FileUtils.getString("webhook.description"), executor, victim, type, reason, duration, isIp);
            embed.setDescription(desc);

            // [Dev Note] NAME;VALUE;INLINE
            for (String field : FileUtils.getConfig().getStringList("webhook.fields")) {
                if (field != null && !field.isEmpty()) {
                    field = Utils.translate(field, executor, victim, type, reason, duration, isIp);
                    String[] args = field.split(";");
                    embed.addField(new WebhookEmbed.EmbedField(Boolean.parseBoolean(args[2]), args[0], args[1]));
                }
            }

            Core.client.send(embed.build()).thenAccept((result) -> Utils.debug("[WebHook | " + result.getId() + "] Message delivered to channel: " + result.getChannelId()));
            return;
        }
        Utils.log(LogLevel.ERROR, "WebHookClient is invalid, check your configuration!");
    }

    /**
     * Send embed punishment message
     */
    public static void sendDiscordPunishmentEmbed(String executor, String victim, String type, String reason, String duration, boolean isIp) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(FileUtils.getConfig().getInt("embed.color"));
        builder.setAuthor(Utils.translate(FileUtils.getString("embed.author"), executor, victim, type, reason, duration, isIp));
        builder.setTitle(Utils.translate(FileUtils.getString("embed.title"), executor, victim, type, reason, duration, isIp));
        builder.setFooter(Utils.translate(FileUtils.getString("embed.footer"), executor, victim, type, reason, duration, isIp));
        builder.setDescription(Utils.translate(FileUtils.getString("embed.description"), executor, victim, type, reason, duration, isIp));

        // [Dev Note] NAME;VALUE;INLINE
        for (String field : FileUtils.getConfig().getStringList("embed.fields")) {
            if (field != null && !field.isEmpty()) {
                field = Utils.translate(field, executor, victim, type, reason, duration, isIp);
                String[] args = field.split(";");
                builder.addField(args[0], args[1], Boolean.parseBoolean(args[2]));
            }
        }

        for (String keys : FileUtils.config.getConfigurationSection("channels").getKeys(false)) {
            TextChannel channel = Core.jda.getGuildById(FileUtils.getString("channels." + keys + ".guildId")).getTextChannelById(FileUtils.getString("channels." + keys + ".channelId"));
            if (channel == null) {
                Utils.log(LogLevel.ERROR, "Invalid guildId or channelId for '" + keys + "'");
                return;
            }
            channel.sendMessage(builder.build()).queue();
        }
    }

    /**
     * Send punishment message
     *
     * @param message The message
     */
    public static void sendDiscordPunishmentMessage(String message) {
        for (String keys : FileUtils.config.getConfigurationSection("channels").getKeys(false)) {
            TextChannel channel = Core.jda.getGuildById(FileUtils.getString("channels." + keys + ".guildId")).getTextChannelById(FileUtils.getString("channels." + keys + ".channelId"));
            if (channel == null) {
                Utils.log(LogLevel.ERROR, "Invalid guildId or channelId for '" + keys + "'");
                return;
            }
            channel.sendMessage(message).queue();
        }
    }

}
