package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class WipeAll implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Alle Trader in der Liste auf inaktiv setzen
        CustomTraderPlugin.getInstance().getManager().getTraders().forEach(trader -> trader.setActive(false));

        // Alle Entities physisch aus der Welt entfernen
        CustomTraderPlugin.getInstance().getManager().despawnAll();

        // Den Status "active: false" permanent in der traders.yml speichern
        CustomTraderPlugin.getInstance().getManager().save();

        sender.sendMessage("§e[CustomTrader] Alle Trader wurden deaktiviert und entfernt.");
        return true;
    }
}