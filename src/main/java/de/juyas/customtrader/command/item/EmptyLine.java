package de.juyas.customtrader.command.item;

import de.juyas.customtrader.util.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EmptyLine implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl nutzen.");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            Chat.send(player, "&cDu musst ein Item in der Hand halten!");
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return true;

        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        if (lore == null) lore = new ArrayList<>();

        // Eine "leere" Zeile in Minecraft Lore ist meist ein Farbstopp-Zeichen oder Leerzeichen
        lore.add("§r");

        meta.setLore(lore);
        item.setItemMeta(meta);

        Chat.send(player, "&aEine leere Zeile wurde der Lore hinzugefügt.");
        return true;
    }
}