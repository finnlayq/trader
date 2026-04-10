package de.juyas.customtrader.command;

import de.juyas.customtrader.api.Presets;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.customtrader.model.TradeOfferEntry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApplyPreset extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cBenutzung: /applypreset <Name>");
            return;
        }

        String presetName = args[0].toLowerCase();
        var newOffers = Presets.getPreset(presetName);

        if (newOffers.isEmpty()) {
            player.sendMessage("§cDieses Preset existiert nicht!");
            return;
        }

        handler.trader().getOffers().clear();
        handler.trader().getOffers().addAll(newOffers);

        if (player.getTargetEntity(5) instanceof Villager villager) {
            villager.setRecipes(newOffers.stream()
                    .map(TradeOfferEntry::getRecipe)
                    .collect(Collectors.toList()));
        }

        player.sendMessage("§a[CustomTrader] Preset §f" + presetName + " §aaktiviert!");
        de.juyas.customtrader.CustomTraderPlugin.getInstance().getManager().save();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            // Hier schlagen wir deine 4 Presets vor
            List<String> suggestions = Arrays.asList("normal1", "normal2", "lucastro", "ochsfurth");
            return suggestions.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}