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

public class AddLoreLine implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl nutzen.");
            return true;
        }

        if (args.length == 0) {
            Chat.send(player, "&cBenutzung: /addloreline <Text>");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            Chat.send(player, "&cDu musst ein Item in der Hand halten!");
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return true;

        // Die neue Zeile aus den Argumenten zusammenbauen und Farben ersetzen
        String newLine = String.join(" ", args).replace("&", "§");

        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        if (lore == null) lore = new ArrayList<>();

        lore.add(newLine);
        meta.setLore(lore);
        item.setItemMeta(meta);

        Chat.send(player, "&aLore-Zeile hinzugefügt: &r" + newLine);
        return true;
    }
}