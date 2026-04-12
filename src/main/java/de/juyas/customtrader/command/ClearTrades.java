package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.ArrayList;

public class ClearTrades extends AbstractSelectionCommand {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        // Interne Liste komplett leeren
        handler.trader().getOffers().clear();

        // Speichern
        CustomTraderPlugin.getInstance().getManager().save();

        // Villager leeren
        if (handler.getEntity() instanceof Villager villager) {
            villager.setRecipes(new ArrayList<>());
        }

        player.sendMessage("§aAlle Trades für diesen Trader wurden erfolgreich gelöscht!");
    }
}