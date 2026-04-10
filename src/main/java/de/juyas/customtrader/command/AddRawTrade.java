package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.customtrader.model.TradeOfferEntry;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddRawTrade extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        if (args.length < 4) {
            player.sendMessage("§cBenutzung: /addrawtrade <Item1> <Menge1> <Ergebnis> <Menge2>");
            return;
        }

        try {
            Material mat1 = Material.valueOf(args[0].toUpperCase());
            int amount1 = Integer.parseInt(args[1]);
            Material mat2 = Material.valueOf(args[2].toUpperCase());
            int amount2 = Integer.parseInt(args[3]);

            MerchantRecipe recipe = new MerchantRecipe(new ItemStack(mat2, amount2), 999999);
            recipe.addIngredient(new ItemStack(mat1, amount1));

            handler.trader().getOffers().add(new TradeOfferEntry(recipe));

            if (player.getTargetEntity(5) instanceof Villager villager) {
                villager.setRecipes(handler.trader().getOffers().stream()
                        .map(TradeOfferEntry::getRecipe)
                        .collect(Collectors.toList()));
            }

            player.sendMessage("§a[CustomTrader] Trade hinzugefügt!");
            de.juyas.customtrader.CustomTraderPlugin.getInstance().getManager().save();

        } catch (Exception e) {
            player.sendMessage("§cFehler: " + e.getMessage());
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        // Schlägt Materialien bei Argument 1 und 3 vor
        if (args.length == 1 || args.length == 3) {
            return Arrays.stream(Material.values())
                    .map(m -> m.name().toLowerCase())
                    .filter(name -> name.startsWith(args[args.length-1].toLowerCase()))
                    .limit(20) // Damit die Liste nicht explodiert
                    .collect(Collectors.toList());
        }
        // Schlägt Mengen vor bei Argument 2 und 4
        if (args.length == 2 || args.length == 4) {
            return List.of("1", "16", "32", "64");
        }
        return List.of();
    }
}