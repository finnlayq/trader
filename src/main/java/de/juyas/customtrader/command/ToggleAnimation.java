package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ToggleAnimation extends AbstractSelectionCommand {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        // Status in den Daten umkehren
        boolean newState = !handler.trader().isAnimationEnabled();
        handler.trader().setAnimationEnabled(newState);

        // Sofort auf den Villager in der Welt anwenden
        if (player.getTargetEntity(5) instanceof LivingEntity living) {
            // Unverwundbarkeit (Immer an, damit er nicht stirbt)
            living.setInvulnerable(true);

            // KI und Sound steuern
            living.setAI(newState);         // Wenn false -> bewegt sich nicht mehr
            living.setSilent(!newState);    // Wenn false -> macht keine Geräusche mehr

            String status = newState ? "§aaktiviert (mit Sounds)" : "§cdeaktiviert (Stumm & Eingefroren)";
            player.sendMessage("§a[CustomTrader] Status für diesen Trader: " + status);
        }

        // Permanent in der traders.yml speichern
        de.juyas.customtrader.CustomTraderPlugin.getInstance().getManager().save();
    }
}