package tsp.dpb.event.minecraft;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import tsp.dpb.api.BridgeAPI;
import tsp.dpb.api.PunishmentEvent;
import tsp.dpb.util.FileUtils;
import tsp.dpb.util.LogLevel;
import tsp.dpb.util.Utils;

public class Event_punishevent implements Listener {

    @EventHandler
    public void pevent(PunishmentEvent e) {
        if (!FileUtils.getBoolean("webhook.enabled")) {
            String messageType = FileUtils.getString("messageType");

            if (messageType.equalsIgnoreCase("message")) {
                String message = FileUtils.getString("message");
                BridgeAPI.sendDiscordPunishmentMessage(Utils.translate(message, e.getExecutor(), e.getVictim(), e.getType(), e.getReason(), e.getDuration(), e.isIp()));
                return;

            }
            if (messageType.equalsIgnoreCase("embed")) {
                BridgeAPI.sendDiscordPunishmentEmbed(e.getExecutor(), e.getVictim(), e.getType(), e.getReason(), e.getDuration(), e.isIp());
                return;
            }
        }
        if (FileUtils.getBoolean("webhook.enabled")) {
            BridgeAPI.sendDiscordPunishmentWebHook(e.getExecutor(), e.getVictim(), e.getType(), e.getReason(), e.getDuration(), e.isIp());
            return;
        }
        Utils.log(LogLevel.ERROR, "Invalid 'messageType' in config.yml!");
    }

}
