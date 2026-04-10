package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SpawnAll implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Alle Trader in der Liste auf aktiv setzen
        CustomTraderPlugin.getInstance().getManager().getTraders().forEach(trader -> trader.setActive(true));

        // Die Spawn-Logik ausführen
        CustomTraderPlugin.getInstance().getManager().spawn();

        // Den Status "active: true" permanent in der traders.yml speichern
        CustomTraderPlugin.getInstance().getManager().save();

        sender.sendMessage("§a[CustomTrader] Alle Trader wurden aktiviert und gespawnt.");
        return true;
    }
}