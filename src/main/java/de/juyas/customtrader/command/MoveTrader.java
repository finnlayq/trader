package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.model.TraderEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MoveTrader implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Benötigt: /movetrader <TraderName> <SpielerName>
        if (args.length < 2) {
            sender.sendMessage("§cBenutzung: /movetrader <TraderName> <ZielSpieler>");
            return true;
        }

        String traderName = args[0];
        Player targetPlayer = Bukkit.getPlayer(args[1]);

        if (targetPlayer == null) {
            sender.sendMessage("§cSpieler '" + args[1] + "' ist nicht online!");
            return true;
        }

        // Suche den Trader in den Daten anhand seines Namens
        TraderEntry traderEntry = CustomTraderPlugin.getInstance().getManager().getTraders().stream()
                .filter(t -> t.getName().equalsIgnoreCase(traderName))
                .findFirst()
                .orElse(null);

        if (traderEntry == null) {
            sender.sendMessage("§cEin Trader mit dem Namen '" + traderName + "' wurde nicht gefunden!");
            return true;
        }

        // Neue Position ist die Position des Ziel-Spielers
        Location newLoc = targetPlayer.getLocation();
        traderEntry.setLocation(newLoc);

        // Die Entity in der Welt teleportieren
        Entity entity = Bukkit.getEntity(traderEntry.getId());
        if (entity != null) {
            entity.teleport(newLoc);
            sender.sendMessage("§a[CustomTrader] Trader §f" + traderName + " §awurde zu §f" + targetPlayer.getName() + " §ateleportiert!");
        } else {
            sender.sendMessage("§e[CustomTrader] Trader-Daten aktualisiert, aber Entity war nicht geladen. Er wird beim nächsten Spawn dort erscheinen.");
        }

        // Speichern, damit er nach Restart dort bleibt
        CustomTraderPlugin.getInstance().getManager().save();
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            // Schlage alle vorhandenen Trader-Namen vor
            return CustomTraderPlugin.getInstance().getManager().getTraders().stream()
                    .map(TraderEntry::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            // Schlage alle Online-Spieler vor
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return completions;
    }
}