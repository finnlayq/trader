package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.util.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WipeAll implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl nutzen.");
            return true;
        }

        // Sicherheitsabfrage: Berechtigung prüfen
        if (!player.hasPermission("customtrader.wipe")) {
            Chat.send(player, "&cDu hast keine Berechtigung, alle Trader zu löschen!");
            return true;
        }

        // 1. Alle physischen Entities/NPCs aus der Welt entfernen
        CustomTraderPlugin.getInstance().getManager().despawnAll();

        // 2. Alle Daten aus der Liste und der Datei löschen
        // Wir gehen davon aus, dass dein Manager eine Methode hat, um alles zu leeren.
        CustomTraderPlugin.getInstance().getManager().getTraders().clear();
        CustomTraderPlugin.getInstance().getManager().save();

        Chat.send(player, "&4&lALLE Trader wurden erfolgreich gelöscht und aus der Datenbank entfernt!");
        return true;
    }
}