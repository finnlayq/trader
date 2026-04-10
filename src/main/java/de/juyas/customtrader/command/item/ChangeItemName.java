package de.juyas.customtrader.command.item;

import de.juyas.customtrader.util.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ChangeItemName implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl nutzen.");
            return true;
        }

        if (args.length == 0) {
            Chat.send(player, "&cBenutzung: /changeitemname <Name>");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            Chat.send(player, "&cDu musst ein Item in der Hand halten!");
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return true;

        // Namen aus Argumenten zusammenbauen und Farbcodes (& -> §) ersetzen
        String newName = String.join(" ", args).replace("&", "§");

        meta.setDisplayName(newName);
        item.setItemMeta(meta);

        Chat.send(player, "&aItem-Name geändert zu: &r" + newName);
        return true;
    }
}