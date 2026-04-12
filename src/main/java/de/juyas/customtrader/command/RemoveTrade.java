package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.customtrader.model.TradeOfferEntry;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class RemoveTrade extends AbstractSelectionCommand {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cNutze: /t removetrade <Nummer>");
            return;
        }
        try {
            int index = Integer.parseInt(args[0]) - 1;
            int currentSize = handler.trader().getOffers().size();

            if (index < 0 || index >= currentSize) {
                player.sendMessage("§cDieser Trade existiert nicht! (Gültig: 1 bis " + currentSize + ")");
                return;
            }

            // Trade aus der Liste entfernen
            handler.trader().getOffers().remove(index);

            // Die Datei-Änderung sofort speichern
            CustomTraderPlugin.getInstance().getManager().save();

            // Live Update am Villager erzwingen!
            if (handler.getEntity() instanceof Villager villager) {
                List<MerchantRecipe> recipes = new ArrayList<>();
                for (TradeOfferEntry toe : handler.trader().getOffers()) {
                    recipes.add(toe.getRecipe());
                }

                // Der wichtigste Teil: Den Villager komplett leeren, damit Bukkit das Update fressen MUSS
                villager.setRecipes(new ArrayList<>());

                // Neue Liste setzen
                if (!recipes.isEmpty()) {
                    villager.setRecipes(recipes);
                }
            }

            player.sendMessage("§aTrade #" + (index + 1) + " wurde erfolgreich gelöscht.");
        } catch (NumberFormatException e) {
            player.sendMessage("§cBitte eine gültige Zahl angeben!");
        } catch (Exception e) {
            player.sendMessage("§cEin unerwarteter Fehler ist aufgetreten.");
            e.printStackTrace();
        }
    }
}