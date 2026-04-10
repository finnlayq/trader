package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.entity.Player;

public class ShowInfo extends AbstractSelectionCommand {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        player.sendMessage("§8» §7Name: §e" + handler.trader().getName());
        player.sendMessage("§8» §7Typ: §e" + handler.trader().getType().name());
        player.sendMessage("§8» §7Trades: §e" + handler.trader().getOffers().size());
    }
}