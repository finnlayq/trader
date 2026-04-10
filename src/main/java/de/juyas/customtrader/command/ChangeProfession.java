package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeProfession extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cBenutzung: /changeprofession <Beruf>");
            return;
        }

        try {
            Villager.Profession prof = Villager.Profession.valueOf(args[0].toUpperCase());
            Entity entity = player.getTargetEntity(5);

            if (entity instanceof Villager villager) {
                villager.setProfession(prof);
                handler.trader().setProfession(prof);
                player.sendMessage("§a[CustomTrader] Beruf auf §f" + prof.name() + " §ageändert.");
                de.juyas.customtrader.CustomTraderPlugin.getInstance().getManager().save();
            } else {
                player.sendMessage("§cDas ist kein Villager!");
            }
        } catch (Exception e) {
            player.sendMessage("§cUngültiger Beruf!");
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            // Schlägt alle Minecraft-Berufe vor
            return Arrays.stream(Villager.Profession.values())
                    .map(p -> p.name().toLowerCase())
                    .filter(name -> name.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}