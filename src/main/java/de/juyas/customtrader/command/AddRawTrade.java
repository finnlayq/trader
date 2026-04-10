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

public class AddRawTrade extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        // Hier kommt die Logik rein, um einen Trade hinzuzufügen
        if (args.length < 1) {
            player.sendMessage("§cBenutzung: /addrawtrade <ID>");
            return;
        }

        player.sendMessage("§aHandel wird für den Trader vorbereitet...");
        // Deine Logik zum Hinzufügen des Trades...
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>(); // Platzhalter für Tab-Completion
    }
}