package de.juyas.customtrader.citizens;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.TraderManager;
import de.juyas.customtrader.model.TraderEntry;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CitizensHandler implements Listener {

    @EventHandler
    public void onNPCClick(NPCRightClickEvent event) {
        // Wir holen uns den bereits existierenden Manager
        TraderManager manager = CustomTraderPlugin.getInstance().getManager();

        // Falls der NPC eine Entity hat, suchen wir den Trader dazu
        if (event.getNPC().getEntity() != null) {
            TraderEntry trader = manager.getTrader(event.getNPC().getEntity().getUniqueId());

            if (trader != null) {
                // Hier kannst du Logik hinzufügen, falls beim Klick auf einen
                // Citizens-NPC etwas Spezielles passieren soll.
            }
        }
    }
}