package de.juyas.customtrader.listener;

import de.juyas.customtrader.CustomTraderPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class TraderDamageListener implements Listener {

    @EventHandler
    public void onTraderDamage(EntityDamageEvent event) {
        // Prüfen, ob die getroffene Entity ein CustomTrader ist
        if (CustomTraderPlugin.getInstance().getManager().getTrader(event.getEntity().getUniqueId()) != null) {
            // Schaden komplett abbrechen
            event.setCancelled(true);
        }
    }
}