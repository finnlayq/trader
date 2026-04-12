package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
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
        // Hilfe anzeigen, wenn kein Argument oder "help" eingegeben wurde
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        return switch (sub) {
            case "create" -> new CreateTrader().onCommand(sender, cmd, label, subArgs);
            case "delete", "remove" -> new RemoveTrader().onCommand(sender, cmd, label, subArgs);
            case "addrawtrade" -> new AddRawTrade().onCommand(sender, cmd, label, subArgs);
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
                sender.sendMessage("§cUnbekannter Befehl. Nutze §e/trader help§c für eine Übersicht.");
                yield true;
            }
        };
    }

    private void sendHelp(CommandSender s) {
        s.sendMessage("§8§m-----------§r §6§lCustomTrader Hilfe §8§m-----------");
        s.sendMessage("§e/t create <Name> §8- §7Erstellt neuen Trader an Pos");
        s.sendMessage("§e/t delete §8- §7Löscht den anvisierten Trader");
        s.sendMessage("§e/t addrawtrade <Item1> <Preis1> <Item2> <Preis2> §8- §7Trade hinzufügen");
        s.sendMessage("§e/t price <tradeX> <Menge> §8- §7Ändert Preis von Slot X");
        s.sendMessage("§e/t preset <Name> §8- §7Wendet ein Item-Set an");
        s.sendMessage("§e/t type/profession §8- §7Ändert Aussehen/Beruf");
        s.sendMessage("§e/t name <Name> §8- §7Ändert Anzeigenamen (& Farbcodes)");
        s.sendMessage("§e/t toggle §8- §7KI & Sounds an/aus");
        s.sendMessage("§e/t move <Trader> <Player> §8- §7Teleportiert Trader");
        s.sendMessage("§e/t spawnall/wipeall §8- §7Alle Trader zeigen/verstecken");
        s.sendMessage("§e/t info/reload §8- §7Admin-Infos & Config Reload");
        s.sendMessage("§8§m---------------------------------------");
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (!(sender instanceof Player player)) return suggestions;

        if (args.length == 1) {
            return Arrays.asList("create", "delete", "addrawtrade", "preset", "price", "name", "type", "profession", "toggle", "spawnall", "wipeall", "move", "info", "reload", "help")
                    .stream().filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }

        if (args.length == 2) {
            String sub = args[0].toLowerCase();
            switch (sub) {
                case "addrawtrade" -> suggestions.addAll(Arrays.stream(Material.values()).filter(Material::isItem).map(m -> m.name().toLowerCase()).collect(Collectors.toList()));
                case "preset" -> suggestions.addAll(Arrays.asList("normal1", "normal2", "lucastro", "ochsfurth"));
                case "type" -> suggestions.addAll(Arrays.stream(EntityType.values()).filter(EntityType::isAlive).map(e -> e.name().toLowerCase()).collect(Collectors.toList()));
                case "profession" -> suggestions.addAll(Arrays.stream(Villager.Profession.values()).filter(p -> p != Villager.Profession.NONE).map(p -> p.name().toLowerCase()).collect(Collectors.toList()));
                case "move" -> suggestions.addAll(CustomTraderPlugin.getInstance().getManager().getTraders().stream().map(t -> t.getName()).collect(Collectors.toList()));
                case "price" -> {
                    Entity target = player.getTargetEntity(5);
                    int count = (target instanceof Villager v) ? v.getRecipeCount() : 3;
                    for (int i = 1; i <= count; i++) suggestions.add("trade" + i);
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

        if (args.length == 4 && args[0].equalsIgnoreCase("addrawtrade")) {
            suggestions.addAll(Arrays.stream(Material.values()).filter(Material::isItem).map(m -> m.name().toLowerCase()).collect(Collectors.toList()));
        }

        if (args.length == 5 && args[0].equalsIgnoreCase("addrawtrade")) {
            suggestions.addAll(Arrays.asList("1", "8", "16", "32", "64"));
        }

        return suggestions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}