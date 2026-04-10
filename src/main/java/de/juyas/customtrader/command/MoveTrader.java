package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.entity.Player;

public class MoveTrader extends AbstractSelectionCommand {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        handler.trader().setLocation(player.getLocation());
        player.sendMessage("§aTrader wurde zu deiner Position bewegt!");
    }
}