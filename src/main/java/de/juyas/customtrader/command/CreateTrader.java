package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CreateTrader implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cNur Spieler können diesen Befehl ausführen.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cBenutzung: /createtrader <Name>");
            return true;
        }

        // Wir suchen das Entity, das der Spieler anschaut
        Entity entity = player.getTargetEntity(5);

        if (entity == null) {
            player.sendMessage("§cDu musst ein Wesen (z.B. einen Villager) anschauen, um es zu einem Trader zu machen!");
            return true;
        }

        // Wir prüfen, ob es vielleicht SCHON ein Trader ist
        if (CustomTraderPlugin.getInstance().getManager().getTrader(entity.getUniqueId()) != null) {
            player.sendMessage("§cDieses Wesen ist bereits ein registrierter Trader!");
            return true;
        }

        // Den Namen aus den Argumenten zusammensetzen (falls er Leerzeichen hat)
        String name = String.join(" ", args);

        // Den Trader im Manager registrieren
        boolean isNpc = entity.hasMetadata("NPC"); // Einfacher Check, ob es ein Citizens NPC ist
        CustomTraderPlugin.getInstance().getManager().createTrader(
                entity.getUniqueId(),
                name,
                entity.getLocation(),
                entity.getType(),
                isNpc
        );

        player.sendMessage("§a[CustomTrader] Trader §f" + name + " §aerfolgreich erstellt!");
        return true;
    }
}