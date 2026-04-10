package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadTrader implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Lädt die Trader neu
        CustomTraderPlugin.getInstance().getManager().reload();

        // Sendet die Bestätigung
        sender.sendMessage(ChatColor.RED + "CustomTrader has been reloaded.");

        return true;
    }
}