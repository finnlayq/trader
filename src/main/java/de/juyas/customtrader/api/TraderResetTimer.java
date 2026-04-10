package de.juyas.customtrader.api;

import de.juyas.customtrader.CustomTraderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public final class TraderResetTimer {

    private final TraderNPCHandler npcHandler;
    private BukkitTask task;

    public TraderResetTimer(TraderNPCHandler npcHandler) {
        this.npcHandler = npcHandler;
    }

    public long refreshRate() {
        Integer seconds = npcHandler.trader().getAttribute(TraderAttribute.REFRESH_SECONDS);
        return (seconds == null || seconds <= 0) ? 0L : seconds * 20L;
    }

    private void reset() {
        npcHandler.resetOffers();
        if (Boolean.TRUE.equals(npcHandler.trader().getAttribute(TraderAttribute.ANIMATION)))
            npcHandler.playAnimation();
    }

    public void start() {
        stop();
        long ticks = refreshRate();
        if (ticks <= 0) return;

        task = Bukkit.getScheduler().runTaskTimer(
                CustomTraderPlugin.getInstance(),
                this::reset,
                ticks,
                ticks
        );
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}