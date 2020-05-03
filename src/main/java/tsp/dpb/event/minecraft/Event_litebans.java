package tsp.dpb.event.minecraft;

import litebans.api.Entry;
import litebans.api.Events;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import tsp.dpb.api.PunishmentEvent;
import tsp.dpb.util.Utils;

public class Event_litebans {

    public void registerEvents() {
        Events.get().register(new Events.Listener() {
            @Override
            public void entryAdded(Entry entry) {
                new BukkitRunnable() {
                    public void run() {
                        Bukkit.getPluginManager().callEvent(new PunishmentEvent(entry.getExecutorName(), entry.getUuid(),  entry.getType(), entry.getReason(), entry.getDurationString(), entry.isIpban()));
                    }
                }.runTask(Utils.getInstance());
            }
        });
    }

}
