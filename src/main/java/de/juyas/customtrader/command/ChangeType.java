package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
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
            player.sendMessage("§cBenutzung: /changetype <Typ>");
            return;
        }

        Entity entity = player.getTargetEntity(5);
        if (entity == null) return;

        // WICHTIGER CHECK: Ist das ein Citizens NPC?
        if (entity.hasMetadata("NPC")) {
            player.sendMessage("§c[CustomTrader] Dieser Trader wird von Citizens gesteuert!");
            player.sendMessage("§7Um ihn zu verwandeln, schaue ihn an und tippe:");
            player.sendMessage("§e/npc type " + args[0].toLowerCase());
            return;
        }

        try {
            EntityType type = EntityType.valueOf(args[0].toUpperCase());

            if (!type.isAlive()) {
                player.sendMessage("§cDieser Typ ist für einen Trader nicht geeignet!");
                return;
            }

            // Typ in deiner Datenbank speichern
            handler.trader().setType(type);
            player.sendMessage("§a[CustomTrader] Typ im System gespeichert.");
            player.sendMessage("§7Da dies kein Citizens-NPC ist, musst du /reloadtrader nutzen, damit er sich neu spawnt.");

        } catch (IllegalArgumentException e) {
            player.sendMessage("§cUngültiger Typ! Nutze die Tab-Taste.");
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
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