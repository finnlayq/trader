package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.entity.Player;

public class AddTrade extends AbstractSelectionCommand {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        // Hier kommt die Logik zum Hinzufügen eines Trades hin.
        // Falls du eine GUI öffnest oder Items prüfst, passiert das hier.

        player.sendMessage("§a[CustomTrader] Du hast den Trader ausgewählt.");
        player.sendMessage("§7Nutze die entsprechenden Items, um einen neuen Handel zu erstellen.");

        // Beispiel: handler.trader().getOffers().add(...);
    }
}