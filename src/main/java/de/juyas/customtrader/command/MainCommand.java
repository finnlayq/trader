package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.api.Presets;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§cDu hast keine Berechtigung!");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        return switch (sub) {
            case "create" -> new CreateTrader().onCommand(sender, cmd, label, subArgs);
            case "delete", "remove" -> new RemoveTrader().onCommand(sender, cmd, label, subArgs);
            case "addtrade" -> new AddTrade().onCommand(sender, cmd, label, subArgs);
            case "addrawtrade" -> new AddRawTrade().onCommand(sender, cmd, label, subArgs);
            case "removetrade" -> new RemoveTrade().onCommand(sender, cmd, label, subArgs);
            case "clear" -> new ClearTrades().onCommand(sender, cmd, label, subArgs); // NEUER BEFEHL
            case "preset" -> new ApplyPreset().onCommand(sender, cmd, label, subArgs);
            case "price" -> new ChangePrice().onCommand(sender, cmd, label, subArgs);
            case "name" -> new ChangeName().onCommand(sender, cmd, label, subArgs);
            case "type" -> new ChangeType().onCommand(sender, cmd, label, subArgs);
            case "profession" -> new ChangeProfession().onCommand(sender, cmd, label, subArgs);
            case "toggle", "animation" -> new ToggleAnimation().onCommand(sender, cmd, label, subArgs);
            case "spawnall" -> new SpawnAll().onCommand(sender, cmd, label, subArgs);
            case "wipeall" -> new WipeAll().onCommand(sender, cmd, label, subArgs);
            case "move" -> new MoveTrader().onCommand(sender, cmd, label, subArgs);
            case "info" -> new ShowInfo().onCommand(sender, cmd, label, subArgs);
            case "reload" -> new ReloadTrader().onCommand(sender, cmd, label, subArgs);
            default -> {
                sender.sendMessage("§cUnbekannter Befehl. Nutze §e/trader help");
                yield true;
            }
        };
    }

    private void sendHelp(CommandSender s) {
        s.sendMessage("§8§m-----------§r §6§lCustomTrader Hilfe §8§m-----------");
        s.sendMessage("§e/t create <Name> §8- §7Neuer Trader");
        s.sendMessage("§e/t delete §8- §7Trader löschen");
        s.sendMessage("§e/t addtrade §8- §7Trade per Hand-Scanner");
        s.sendMessage("§e/t clear §8- §7Alle Trades des Traders löschen");
        s.sendMessage("§e/t removetrade <Nr> §8- §7Einen Trade löschen");
        s.sendMessage("§e/t price <Nr> <Menge> §8- §7Preis ändern");
        s.sendMessage("§e/t preset <Name> §8- §7Item-Set anwenden");
        s.sendMessage("§e/t type/profession §8- §7Aussehen");
        s.sendMessage("§e/t name <Name> §8- §7Anzeigename");
        s.sendMessage("§e/t toggle §8- §7KI an/aus");
        s.sendMessage("§8§m---------------------------------------");
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        if (!sender.isOp()) return new ArrayList<>();
        List<String> suggestions = new ArrayList<>();
        if (!(sender instanceof Player player)) return suggestions;

        if (args.length == 1) {
            return Arrays.asList("create", "delete", "addtrade", "addrawtrade", "removetrade", "clear", "preset", "price", "name", "type", "profession", "toggle", "spawnall", "wipeall", "move", "info", "reload", "help")
                    .stream().filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }

        if (args.length == 2) {
            String sub = args[0].toLowerCase();
            switch (sub) {
                case "addrawtrade" -> suggestions.addAll(Arrays.stream(Material.values()).filter(Material::isItem).map(m -> m.name().toLowerCase()).collect(Collectors.toList()));
                case "preset" -> suggestions.addAll(Presets.getPresetNames());
                case "type" -> suggestions.addAll(Arrays.stream(EntityType.values()).filter(EntityType::isAlive).map(e -> e.name().toLowerCase()).collect(Collectors.toList()));
                case "profession" -> suggestions.addAll(Arrays.stream(Villager.Profession.values()).filter(p -> p != Villager.Profession.NONE).map(p -> p.name().toLowerCase()).collect(Collectors.toList()));
                case "move" -> suggestions.addAll(CustomTraderPlugin.getInstance().getManager().getTraders().stream().map(t -> t.getName()).collect(Collectors.toList()));
                case "price", "removetrade" -> {
                    try {
                        Entity target = player.getTargetEntity(5);
                        int count = (target instanceof Villager v) ? v.getRecipeCount() : 5;
                        for (int i = 1; i <= count; i++) suggestions.add(String.valueOf(i));
                    } catch (Exception e) {
                        for (int i = 1; i <= 5; i++) suggestions.add(String.valueOf(i));
                    }
                }
            }
        }

        if (args.length == 3) {
            String sub = args[0].toLowerCase();
            if (sub.equals("addrawtrade") || sub.equals("price")) {
                suggestions.addAll(Arrays.asList("1", "8", "16", "32", "64"));
            } else if (sub.equals("move")) {
                suggestions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
            }
        }

        return suggestions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}