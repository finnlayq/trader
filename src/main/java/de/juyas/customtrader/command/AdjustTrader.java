package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdjustTrader extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        // Diese Methode wird aufgerufen, wenn ein Spieler einen Trader anschaut und den Befehl nutzt.
        if (args.length < 1) {
            player.sendMessage("§cBenutzung: /adjusttrader <name|type|npc|animation> <wert>");
            return;
        }

        String subCommand = args[0].toLowerCase();
        player.sendMessage("§a[CustomTrader] Bearbeite Eigenschaft: §f" + subCommand);

        // Hier folgt normalerweise die Logik, um Name, Typ oder NPC-Status zu ändern.
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("name", "type", "npc", "animation");
        }
        return new ArrayList<>();
    }
}