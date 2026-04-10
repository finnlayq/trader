package de.juyas.customtrader.command.item;

import de.juyas.customtrader.util.Arguments;
import de.juyas.customtrader.util.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RemoveLoreLine implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl nutzen.");
            return true;
        }

        if (args.length == 0) {
            Chat.send(player, "&cBenutzung: /removeloreline <Zeile (1, 2, 3...)>");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            Chat.send(player, "&cDu musst ein Item in der Hand halten!");
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            Chat.send(player, "&cDieses Item hat keine Lore!");
            return true;
        }

        // Wir nutzen unsere Arguments-Klasse zum Parsen
        Integer index = Arguments.numInt(player, args[0]);
        if (index == null) return true;

        List<String> lore = meta.getLore();
        int listIndex = index - 1; // Umrechnung von 1-basiert auf 0-basiert

        if (lore != null && listIndex >= 0 && listIndex < lore.size()) {
            String removedText = lore.remove(listIndex);
            meta.setLore(lore);
            item.setItemMeta(meta);
            Chat.send(player, "&aZeile &e" + index + " &aentfernt: &r" + removedText);
        } else {
            Chat.send(player, "&cZeile " + index + " existiert nicht!");
        }

        return true;
    }
}