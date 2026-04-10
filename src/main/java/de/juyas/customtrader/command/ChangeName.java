package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderNPCHandler;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChangeName extends AbstractSelectionCommand implements TabCompleter {

    @Override
    public void onSelectionCommand(Player player, TraderNPCHandler handler, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cBenutzung: /changename <neuer name>");
            return;
        }

        String newName = String.join(" ", args);
        handler.trader().setName(newName);

        Entity entity = player.getTargetEntity(5);
        if (entity != null) {
            // WICHTIG: Hier prüfen wir, ob es ein Citizens NPC ist
            if (entity.hasMetadata("NPC")) {
                NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
                if (npc != null) {
                    npc.setName(newName); // Wir befehlen Citizens, den Namen zu ändern!
                    player.sendMessage("§a[CustomTrader] Citizens-NPC Name erfolgreich zu §f" + newName + " §ageändert.");
                    return;
                }
            }

            // Fallback für normale Minecraft-Wesen (keine Citizens NPCs)
            entity.setCustomName(newName);
            entity.setCustomNameVisible(true);
            player.sendMessage("§a[CustomTrader] Normaler Name erfolgreich zu §f" + newName + " §ageändert.");
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}