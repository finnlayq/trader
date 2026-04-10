package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.util.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Reload implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Berechtigungsprüfung (optional, kannst du anpassen)
        if (!sender.hasPermission("customtrader.reload")) {
            sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl.");
            return true;
        }

        // 1. Alle aktuellen Trader despawnen
        CustomTraderPlugin.getInstance().getManager().despawnAll();

        // 2. Daten neu laden
        CustomTraderPlugin.getInstance().getManager().reload();

        // 3. Trader neu spawnen
        CustomTraderPlugin.getInstance().getManager().spawn();

        if (sender instanceof org.bukkit.entity.Player player) {
            Chat.send(player, "&aPlugin-Konfiguration und Trader erfolgreich neu geladen!");
        } else {
            sender.sendMessage("[CustomTrader] Plugin erfolgreich neu geladen!");
        }

        return true;
    }
}