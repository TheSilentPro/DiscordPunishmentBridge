package tsp.dpb.event.minecraft;

import me.leoko.advancedban.bukkit.event.PunishmentEvent;
import me.leoko.advancedban.utils.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Event_advancedban implements Listener {

    @EventHandler
    public void pevent(PunishmentEvent e) {
        Punishment p = e.getPunishment();
        Bukkit.getPluginManager().callEvent(new tsp.dpb.api.PunishmentEvent(p.getOperator(), p.getName(), p.getType().name(), p.getReason(), p.getDuration(true), p.getType().isIpOrientated()));
    }

}
