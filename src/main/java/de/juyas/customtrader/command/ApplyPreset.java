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

import java.util.List;
import java.util.stream.Collectors;

public class ApplyPreset extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cBenutzung: /applypreset <Name>");
            return;
        }

        // NEU: Wir laden die Presets kurz neu, falls die Datei geändert wurde
        Presets.load();

        String presetName = args[0].toLowerCase();
        var newOffers = Presets.getPreset(presetName);

        if (newOffers == null || newOffers.isEmpty()) {
            player.sendMessage("§cDas Preset '" + presetName + "' existiert nicht in der presets.yml!");
            player.sendMessage("§7Verfügbar: " + String.join(", ", Presets.getPresetNames()));
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
            return Presets.getPresetNames().stream()
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}