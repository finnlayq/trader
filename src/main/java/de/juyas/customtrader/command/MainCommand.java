package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§8§m-------§r §6§lCustomTrader §8§m-------");
            sender.sendMessage("§e/trader create <Name> §7- Erstellt einen Trader");
            sender.sendMessage("§e/trader delete §7- Löscht den anvisierten Trader");
            sender.sendMessage("§e/trader toggle §7- Stumm/Animation umschalten");
            sender.sendMessage("§e/trader spawnall §7- Alle Trader erscheinen lassen");
            sender.sendMessage("§e/trader wipeall §7- Alle Trader verstecken");
            sender.sendMessage("§e/trader reload §7- Config neu laden");
            return true;
        }

        String sub = args[0].toLowerCase();
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        // Hier leiten wir an deine bestehenden Klassen weiter
        return switch (sub) {
            case "create" -> new CreateTrader().onCommand(sender, cmd, label, subArgs);
            case "delete", "remove" -> new RemoveTrader().onCommand(sender, cmd, label, subArgs);
            case "toggle", "animation" -> new ToggleAnimation().onCommand(sender, cmd, label, subArgs);
            case "spawnall" -> new SpawnAll().onCommand(sender, cmd, label, subArgs);
            case "wipeall" -> new WipeAll().onCommand(sender, cmd, label, subArgs);
            case "reload" -> new ReloadTrader().onCommand(sender, cmd, label, subArgs);
            case "info" -> new ShowInfo().onCommand(sender, cmd, label, subArgs);
            case "move" -> new MoveTrader().onCommand(sender, cmd, label, subArgs);
            case "preset" -> new ApplyPreset().onCommand(sender, cmd, label, subArgs);
            case "price" -> new ChangePrice().onCommand(sender, cmd, label, subArgs);
            default -> {
                sender.sendMessage("§cUnbekannter Unterbefehl: " + sub);
                yield true;
            }
        };
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> subs = Arrays.asList("create", "delete", "toggle", "spawnall", "wipeall", "reload", "info", "move", "preset", "price");
            return subs.stream().filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }

        // Tab-Completion für das zweite Argument (z.B. Presets oder Spielernamen)
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("preset")) {
                return Arrays.asList("normal1", "normal2", "lucastro", "ochsfurth");
            }
            if (args[0].equalsIgnoreCase("move")) {
                return null; // Zeigt Spielernamen an
            }
        }
        return new ArrayList<>();
    }
}