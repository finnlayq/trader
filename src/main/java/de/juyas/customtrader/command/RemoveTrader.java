package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.entity.Player;

public class RemoveTrader extends AbstractSelectionCommand {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        CustomTraderPlugin.getInstance().getManager().removeTrader(handler.trader().getId());
        player.sendMessage("§aDer Trader wurde erfolgreich entfernt!");
    }
}