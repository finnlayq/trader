package de.juyas.customtrader.util;

import org.bukkit.entity.Player;
import java.util.Optional;

public class Arguments {

    /**
     * Versucht einen String in eine Zahl umzuwandeln und gibt bei Fehlern eine Nachricht an den Spieler aus.
     */
    public static Integer numInt(Player player, String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            Chat.send(player, "&c'" + arg + "' ist keine gültige Zahl!");
            return null;
        }
    }

    /**
     * Hilfsmethode für optionale Booleans (true/false) in Befehlen.
     */
    public static Optional<Boolean> optBool(String[] args, int index) {
        if (args.length <= index) return Optional.empty();
        String val = args[index].toLowerCase();
        if (val.equals("true") || val.equals("yes") || val.equals("1")) return Optional.of(true);
        if (val.equals("false") || val.equals("no") || val.equals("0")) return Optional.of(false);
        return Optional.empty();
    }
}