package de.juyas.customtrader.listener;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.model.TraderEntry;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class DeletionListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        TraderEntry entry = CustomTraderPlugin.getInstance().getManager().getTrader(entity.getUniqueId());

        if (entry != null) {
            CustomTraderPlugin.getInstance().getManager().removeTrader(entry.getId());
        }
    }
}