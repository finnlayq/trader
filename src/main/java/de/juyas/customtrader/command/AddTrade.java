package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.customtrader.model.TradeOfferEntry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class AddTrade extends AbstractSelectionCommand {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        // Items aus den Händen lesen (Haupthand = Ware/Ergebnis, Offhand = Bezahlung)
        ItemStack result = player.getInventory().getItemInMainHand();
        ItemStack payment = player.getInventory().getItemInOffHand();

        if (result.getType() == Material.AIR || payment.getType() == Material.AIR) {
            player.sendMessage("§c§lFEHLER!");
            player.sendMessage("§e1. Nimm die §bBezahlung §ein die §bOffhand §e(F drücken).");
            player.sendMessage("§e2. Nimm die §bWare/das Ergebnis §ein die §bHaupthand§e.");
            player.sendMessage("§e3. Tippe §b/t addtrade §eerneut, während du den Villager ansiehst.");
            return;
        }

        // Rezept erstellen
        MerchantRecipe recipe = new MerchantRecipe(result.clone(), 999999);
        recipe.addIngredient(payment.clone());
        recipe.setExperienceReward(false);

        // Zum Trader hinzufügen
        handler.trader().getOffers().add(new TradeOfferEntry(recipe));
        CustomTraderPlugin.getInstance().getManager().save();

        // Absolut sicherer Live-Sync für den Villager
        if (handler.getEntity() instanceof Villager villager) {
            List<MerchantRecipe> recipes = new ArrayList<>();
            for (TradeOfferEntry toe : handler.trader().getOffers()) {
                recipes.add(toe.getRecipe());
            }
            // Zwingt Bukkit, die Rezeptliste zu überschreiben
            villager.setRecipes(recipes);
        }

        player.sendMessage("§a§lERFOLG! §aTrade wurde aus deinen Händen gelesen und direkt hinzugefügt.");
    }
}