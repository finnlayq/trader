package de.juyas.customtrader.util;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class Chat {
    public static void send(Player player, String message) {
        if (message == null) return;
        // Nutzt Paper Adventure API zum Senden
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message.replace("&", "§")));
    }
}