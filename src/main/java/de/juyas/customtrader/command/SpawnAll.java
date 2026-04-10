package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.util.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnAll implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl nutzen.");
            return true;
        }

        // Berechtigungsprüfung
        if (!player.hasPermission("customtrader.spawnall")) {
            Chat.send(player, "&cDu hast keine Berechtigung für diesen Befehl.");
            return true;
        }

        // Zuerst alle existierenden Instanzen entfernen, um Dubletten zu vermeiden
        CustomTraderPlugin.getInstance().getManager().despawnAll();

        // Alle Trader aus der Datenbank/Konfiguration neu spawnen
        CustomTraderPlugin.getInstance().getManager().spawn();

        Chat.send(player, "&aAlle registrierten Trader wurden neu gespawnt!");
        return true;
    }
}