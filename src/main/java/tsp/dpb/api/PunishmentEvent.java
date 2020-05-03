package tsp.dpb.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PunishmentEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private String executor;
    private String victim;
    private String type;
    private String reason;
    private String duration;
    private boolean isIp;

    public PunishmentEvent(String executor, String victim, String type, String reason, String duration, boolean isIp) {
        this.executor = executor;
        this.victim = victim;
        this.type = type;
        this.reason = reason;
        this.duration = duration;
        this.isIp = isIp;
    }

    public String getExecutor() {
        return executor;
    }

    public String getVictim() {
        return victim;
    }

    public String getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public String getDuration() {
        return duration;
    }

    public boolean isIp() {
        return isIp;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
