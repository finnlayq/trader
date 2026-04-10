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

public class ChangeName extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        // Überprüfen, ob ein Name angegeben wurde
        if (args.length < 1) {
            player.sendMessage("§cBenutzung: /changename <neuer name>");
            return;
        }

        // Alle Argumente zusammenfügen, falls der Name Leerzeichen enthält
        String newName = String.join(" ", args);

        // Den Namen im Trader-Objekt aktualisieren
        handler.trader().setName(newName);

        player.sendMessage("§a[CustomTrader] Der Name des Traders wurde in §f" + newName + " §ageändert.");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        // Tab-Completion kann hier leer bleiben oder Vorschläge für Namen liefern
        return new ArrayList<>();
    }
}