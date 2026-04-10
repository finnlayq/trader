package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ToggleAnimation extends AbstractSelectionCommand {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        boolean current = handler.trader().isAnimationEnabled();
        boolean newState = !current;

        handler.trader().setAnimationEnabled(newState);

        if (player.getTargetEntity(5) instanceof LivingEntity living) {
            living.setAI(newState);
            player.sendMessage("§a[CustomTrader] KI ist jetzt " + (newState ? "§2AN" : "§cOFF"));
        }
    }
}