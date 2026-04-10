package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.customtrader.model.TraderEntry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSelectionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl ausführen.");
            return true;
        }

        Entity entity = player.getTargetEntity(5);

        if (entity == null) {
            player.sendMessage("§cDu musst einen Trader anschauen!");
            return true;
        }

        TraderEntry entry = CustomTraderPlugin.getInstance().getManager().getTrader(entity.getUniqueId());

        if (entry == null) {
            player.sendMessage("§cDieses Wesen ist kein registrierter Trader!");
            return true;
        }

        TraderNPCHandler handler = new TraderNPCHandler(entry);
        onSelectionCommand(player, handler, args);

        return true;
    }

    public abstract void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args);

    protected void putInQueue(Player player) {
    }

    protected void pullFromQueue(Player player) {
    }
}