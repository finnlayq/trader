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

public class ChangeSkin extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cBenutzung: /changeskin <Spielername>");
            return;
        }

        String skinSource = args[0];

        // Hier wird normalerweise die Skin-Änderung über Citizens oder dein System getriggert
        player.sendMessage("§a[CustomTrader] Versuche, den Skin auf §f" + skinSource + " §azu ändern...");

        // Beispiel-Logik:
        // handler.setSkin(skinSource);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        // Tab-Completion für online Spieler als Skin-Vorschlag
        if (args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            org.bukkit.Bukkit.getOnlinePlayers().forEach(p -> playerNames.add(p.getName()));
            return playerNames;
        }
        return new ArrayList<>();
    }
}