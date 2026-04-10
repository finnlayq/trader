package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SpawnAll implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CustomTraderPlugin.getInstance().getManager().spawn();
        sender.sendMessage("§a[CustomTrader] Alle registrierten Trader wurden gespawnt!");
        return true;
    }
}