package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ToggleAnimation extends AbstractSelectionCommand {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        Entity target = player.getTargetEntity(5);
        if (target instanceof LivingEntity livingTarget) {
            boolean currentAI = livingTarget.hasAI();
            livingTarget.setAI(!currentAI);
            player.sendMessage("§aAnimation / KI für diesen Trader umgeschaltet!");
        } else {
            player.sendMessage("§cDieser Trader-Typ unterstützt keine Animationen.");
        }
    }
}