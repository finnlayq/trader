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
            player.sendMessage("§cBenutzung: /changetype <EntityTyp>");
            return;
        }

        try {
            EntityType type = EntityType.valueOf(args[0].toUpperCase());

            // Den Typ in den Daten speichern
            handler.trader().setType(type);

            player.sendMessage("§a[CustomTrader] Typ auf §f" + type.name() + " §ageändert.");
            player.sendMessage("§7Nutze /spawnall oder starte den Server neu, um die Änderung zu sehen.");

            // Speichern
            de.juyas.customtrader.CustomTraderPlugin.getInstance().getManager().save();

        } catch (IllegalArgumentException e) {
            player.sendMessage("§cUngültiger Entity-Typ!");
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            // Schlägt alle lebenden/spawnbaren Entity-Typen vor
            return Arrays.stream(EntityType.values())
                    .filter(EntityType::isSpawnable)
                    .filter(EntityType::isAlive)
                    .map(t -> t.name().toLowerCase())
                    .filter(name -> name.startsWith(args[0].toLowerCase()))
                    .limit(25) // Begrenzung, damit die Liste übersichtlich bleibt
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}