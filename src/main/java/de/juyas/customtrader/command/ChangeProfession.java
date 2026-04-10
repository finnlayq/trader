package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class ChangeProfession extends AbstractSelectionCommand {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cBenutzung: /changeprofession <Beruf>");
            return;
        }

        try {
            Villager.Profession prof = Villager.Profession.valueOf(args[0].toUpperCase());
            Entity entity = player.getTargetEntity(5);

            if (entity instanceof Villager villager) {
                villager.setProfession(prof);
                // WICHTIG: Hier wird es im Speicher abgelegt!
                handler.trader().setProfession(prof);
                player.sendMessage("§a[CustomTrader] Beruf auf §f" + prof.name() + " §ageändert und gespeichert.");
            } else {
                player.sendMessage("§cDas ist kein Villager!");
            }
        } catch (Exception e) {
            player.sendMessage("§cUngültiger Beruf!");
        }
    }
}