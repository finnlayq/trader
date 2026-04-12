package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.customtrader.model.TradeOfferEntry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class AddRawTrade extends AbstractSelectionCommand {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        Material payMat = Material.EMERALD;
        int payAmount = 1;
        Material getMat = Material.APPLE;
        int getAmount = 1;

        try {
            if (args.length >= 1) payMat = Material.getMaterial(args[0].toUpperCase());
            if (args.length >= 2) payAmount = Integer.parseInt(args[1]);
            if (args.length >= 3) getMat = Material.getMaterial(args[2].toUpperCase());
            if (args.length >= 4) getAmount = Integer.parseInt(args[3]);

            if (payMat == null || getMat == null) throw new Exception();
        } catch (Exception e) {
            player.sendMessage("§cFehler: /t addrawtrade <BezahlItem> <Menge> <ErgebnisItem> <Menge>");
            return;
        }

        // 1. Neues Rezept erstellen
        ItemStack buy1 = new ItemStack(payMat, payAmount);
        ItemStack result = new ItemStack(getMat, getAmount);
        MerchantRecipe recipe = new MerchantRecipe(result, 999);
        recipe.addIngredient(buy1);
        recipe.setExperienceReward(false);
        recipe.setVillagerExperience(0);

        // 2. Im Daten-Modell speichern (für Neustarts)
        handler.trader().getOffers().add(new TradeOfferEntry(recipe));

        // 3. LIVE-UPDATE am Villager erzwingen
        // Wir holen das Entity direkt vom Handler (egal ob NPC oder normales Entity)
        org.bukkit.entity.Entity entity = handler.getEntity();
        if (entity == null) {
            // Falls Citizens NPC, probieren wir das Bukkit-Entity des NPCs zu holen
            if (handler.getNpc() != null && handler.getNpc().isSpawned()) {
                entity = handler.getNpc().getEntity();
            }
        }

        if (entity instanceof Villager villager) {
            // WICHTIG: Liste komplett neu aufbauen
            List<MerchantRecipe> recipes = new ArrayList<>();
            for (TradeOfferEntry offer : handler.trader().getOffers()) {
                recipes.add(offer.getRecipe());
            }

            // Den Villager zwingen die Rezepte zu übernehmen
            villager.setRecipes(recipes);

            // Kleiner Trick: Manche Villager brauchen einen "Anstoß", um das Inventar zu refreshen
            player.sendMessage("§aTrade live aktualisiert am Villager!");
        } else {
            player.sendMessage("§6Trade gespeichert, aber Entity ist kein Villager (Typ: " + (entity != null ? entity.getType() : "null") + ")");
        }

        player.sendMessage("§aHinzugefügt: §e" + payAmount + "x " + payMat.name() + " §agegen §e" + getAmount + "x " + getMat.name());
    }
}