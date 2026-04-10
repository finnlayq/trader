package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.customtrader.model.TradeOfferEntry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChangePrice extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        if (args.length < 3) {
            player.sendMessage("§cBenutzung: /changeprice trade<Nr> <AnzahlKosten> <AnzahlErgebnis>");
            return;
        }

        try {
            // "trade1" zu "1" umwandeln
            String indexStr = args[0].toLowerCase().replace("trade", "");
            int index = Integer.parseInt(indexStr) - 1;

            int buyAmount = Integer.parseInt(args[1]);
            int sellAmount = Integer.parseInt(args[2]);

            if (index < 0 || index >= handler.trader().getOffers().size()) {
                player.sendMessage("§cDieser Trader hat keinen Trade mit der Nummer " + (index + 1));
                return;
            }

            // 1. Altes Rezept holen und neues mit angepassten Mengen erstellen
            TradeOfferEntry offerEntry = handler.trader().getOffers().get(index);
            MerchantRecipe oldRecipe = offerEntry.getRecipe();

            ItemStack newResult = oldRecipe.getResult().clone();
            newResult.setAmount(sellAmount);

            MerchantRecipe newRecipe = new MerchantRecipe(newResult, 999999);
            newRecipe.setExperienceReward(oldRecipe.hasExperienceReward());

            // Zutaten übernehmen und Menge der ersten Zutat anpassen
            List<ItemStack> ingredients = oldRecipe.getIngredients().stream()
                    .map(ItemStack::clone)
                    .collect(Collectors.toList());
            if (!ingredients.isEmpty()) {
                ingredients.get(0).setAmount(buyAmount);
            }
            newRecipe.setIngredients(ingredients);

            // In die Liste des Plugin-Speichers schreiben
            handler.trader().getOffers().set(index, new TradeOfferEntry(newRecipe));

            // 2. LIVE-UPDATE (Der "Reset-Trick")
            if (player.getTargetEntity(5) instanceof Villager villager) {
                // Erst komplett leeren (Reset)
                villager.setRecipes(new ArrayList<>());

                // Dann alle Rezepte aus unserem Speicher neu setzen
                List<MerchantRecipe> allRecipes = handler.trader().getOffers().stream()
                        .map(TradeOfferEntry::getRecipe)
                        .collect(Collectors.toList());

                villager.setRecipes(allRecipes);
            }

            player.sendMessage("§a[CustomTrader] Preis für §fTrade " + (index + 1) + " §aauf §f" + buyAmount + " -> " + sellAmount + " §aaktualisiert!");

            // 3. Speichern
            de.juyas.customtrader.CustomTraderPlugin.getInstance().getManager().save();

        } catch (Exception e) {
            player.sendMessage("§cFehler: Nutze das Format /changeprice trade1 10 5");
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (!(sender instanceof Player player)) return completions;

        if (args.length == 1) {
            int tradeCount = 5;
            if (player.getTargetEntity(5) instanceof Villager v) {
                tradeCount = v.getRecipeCount();
            }
            for (int i = 1; i <= tradeCount; i++) {
                completions.add("trade" + i);
            }
        } else if (args.length == 2 || args.length == 3) {
            completions.addAll(List.of("1", "8", "16", "32", "64"));
        }

        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}