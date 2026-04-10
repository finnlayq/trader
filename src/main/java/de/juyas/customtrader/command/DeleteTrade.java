package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DeleteTrade extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        int size = handler.trader().getOffers().size();
        player.sendMessage("§aTrade löschen-Modus. Aktuelle Trades: " + size);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}