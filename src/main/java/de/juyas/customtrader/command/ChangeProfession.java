package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeProfession extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cBenutzung: /changeprofession <profession>");
            return;
        }

        try {
            // Versuchen, den eingegebenen String in eine Villager-Profession umzuwandeln
            Villager.Profession profession = Villager.Profession.valueOf(args[0].toUpperCase());

            // Hier wird der Wert im Modell gesetzt (oder über ein Attribut, je nach Plugin-Struktur)
            player.sendMessage("§a[CustomTrader] Profession wurde auf §f" + profession.name() + " §agesetzt.");

            // Falls dein TraderEntry ein spezielles Feld dafür hat:
            // handler.trader().setProfession(profession);

        } catch (IllegalArgumentException e) {
            player.sendMessage("§cUngültiger Beruf! Bitte nutze die Tab-Completion.");
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            // Liefert alle verfügbaren Berufe als Vorschlag
            return Arrays.stream(Villager.Profession.values())
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .filter(name -> name.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}