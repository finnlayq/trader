package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChangeCooldown extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        // Überprüfung, ob ein Wert für den Cooldown angegeben wurde
        if (args.length < 1) {
            player.sendMessage("§cBenutzung: /changecooldown <sekunden>");
            return;
        }

        try {
            int seconds = Integer.parseInt(args[0]);

            // Logik zum Ändern des Cooldowns
            // Hier greifen wir auf das Trader-Modell im Handler zu
            handler.trader().setRefreshSeconds(seconds);

            player.sendMessage("§a[CustomTrader] Cooldown für diesen Trader auf §f" + seconds + "s §ageändert.");
        } catch (NumberFormatException e) {
            player.sendMessage("§cBitte gib eine gültige Zahl für die Sekunden an!");
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        // Vorschläge für den Tab-Completer (z.B. Zeitwerte)
        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add("30");
            suggestions.add("60");
            suggestions.add("300");
            suggestions.add("3600");
            return suggestions;
        }
        return new ArrayList<>();
    }
}