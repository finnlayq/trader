package de.juyas.customtrader.citizens;

import de.juyas.customtrader.TraderManager;
import de.juyas.customtrader.api.TraderNPCHandler;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CitizensHandler implements Listener {

    private final TraderManager manager;

    public CitizensHandler(TraderManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onNPCClick(NPCRightClickEvent event) {
        // Sicherstellen, dass das Entity des NPCs existiert
        if (event.getNPC().getEntity() != null) {

            // FIX: .getUniqueId() hinzugefügt, um die UUID statt des Entity-Objekts zu nutzen
            Object trader = manager.getTrader(event.getNPC().getEntity().getUniqueId());

            // Hier folgt normalerweise die Logik zum Öffnen des Trader-Inventars
            if (trader instanceof TraderNPCHandler) {
                TraderNPCHandler handler = (TraderNPCHandler) trader;
                // handler.openInventory(event.getClicker());
            }
        }
    }
}