package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeType extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cBenutzung: /changetype <EntityType>");
            return;
        }

        try {
            // Umwandeln der Eingabe in einen gültigen EntityType
            EntityType type = EntityType.valueOf(args[0].toUpperCase());

            if (!type.isAlive()) {
                player.sendMessage("§cDieser Typ ist für einen Trader nicht geeignet!");
                return;
            }

            // Typ im Trader-Modell aktualisieren
            handler.trader().setType(type);

            player.sendMessage("§a[CustomTrader] Typ wurde auf §f" + type.name() + " §agesetzt.");
            player.sendMessage("§7(Hinweis: Der NPC muss eventuell neu geladen werden)");

        } catch (IllegalArgumentException e) {
            player.sendMessage("§cUngültiger Entity-Typ! Nutze die Tab-Vervollständigung.");
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            // Schlägt nur lebendige Entities vor (Villager, Zombie, etc.)
            return Arrays.stream(EntityType.values())
                    .filter(EntityType::isAlive)
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .filter(name -> name.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return null;
    }
}