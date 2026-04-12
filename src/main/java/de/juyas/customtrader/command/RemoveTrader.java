package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.customtrader.CustomTraderPlugin;
import org.bukkit.entity.Player;

public class RemoveTrader extends AbstractSelectionCommand {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        // Entity aus der Welt entfernen
        if (handler.getEntity() != null) {
            handler.getEntity().remove();
        }

        // Wir nutzen den Namen zum Löschen, da getUuid() im Model fehlt
        String traderName = handler.trader().getName();
        CustomTraderPlugin.getInstance().getManager().getTraders().removeIf(t -> t.getName().equals(traderName));

        // Config speichern, damit er nach Restart weg bleibt
        CustomTraderPlugin.getInstance().getManager().save();

        player.sendMessage("§aTrader '" + traderName + "' erfolgreich gelöscht.");
    }
}