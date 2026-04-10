package de.juyas.customtrader;

import de.juyas.customtrader.model.TraderEntry;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TraderManager {

    private final Map<UUID, TraderEntry> traderData = new HashMap<>();
    private final File file;
    private FileConfiguration config;

    public TraderManager() {
        this.file = new File(CustomTraderPlugin.getInstance().getDataFolder(), "traders.yml");
        load();
    }

    public void createTrader(UUID id, String name, Location loc, EntityType type, boolean npc) {
        TraderEntry entry = new TraderEntry(id, name, loc, type, npc);
        traderData.put(id, entry);
        save();
    }

    public void spawn() {
        List<TraderEntry> currentTraders = new ArrayList<>(traderData.values());
        for (TraderEntry entry : currentTraders) {
            Location loc = entry.getLocation();
            if (loc == null || loc.getWorld() == null) continue;

            if (entry.isNpc()) {
                NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(entry.getId());
                if (npc != null && !npc.isSpawned()) npc.spawn(loc);
            } else {
                Entity entity = Bukkit.getEntity(entry.getId());
                if (entity == null) {
                    entity = loc.getWorld().spawnEntity(loc, entry.getType());
                    traderData.remove(entry.getId());
                    entry.setId(entity.getUniqueId());
                    traderData.put(entity.getUniqueId(), entry);
                }

                final Entity finalEntity = entity;
                // Wir warten 2 Ticks (0.1 Sek), damit Minecraft das Wesen wirklich registriert hat
                Bukkit.getScheduler().runTaskLater(CustomTraderPlugin.getInstance(), () -> {
                    finalEntity.setCustomName(entry.getName());
                    finalEntity.setCustomNameVisible(true);

                    if (finalEntity instanceof LivingEntity living) {
                        living.setInvulnerable(true); // Unverwundbar
                        living.setAI(entry.isAnimationEnabled()); // Bewegung stoppen
                        living.setRemoveWhenFarAway(false);
                        living.setPersistent(true);
                    }

                    if (finalEntity instanceof Villager villager) {
                        villager.setProfession(entry.getProfession());
                    }
                }, 2L);
            }
        }
    }

    public void despawnAll() {
        for (TraderEntry entry : traderData.values()) {
            if (entry.isNpc()) {
                NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(entry.getId());
                if (npc != null) npc.despawn();
            } else {
                Entity entity = Bukkit.getEntity(entry.getId());
                if (entity != null) entity.remove();
            }
        }
    }

    public void save() {
        config = new YamlConfiguration();
        for (TraderEntry entry : traderData.values()) {
            String path = "traders." + entry.getId().toString();
            config.set(path + ".name", entry.getName());
            config.set(path + ".type", entry.getType().name());
            config.set(path + ".location", entry.getLocation());
            config.set(path + ".isNpc", entry.isNpc());
            config.set(path + ".profession", entry.getProfession().name());
            config.set(path + ".animation", entry.isAnimationEnabled());
        }
        try {
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            config.save(file);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void load() {
        if (!file.exists()) return;
        config = YamlConfiguration.loadConfiguration(file);
        if (config.getConfigurationSection("traders") == null) return;

        traderData.clear();
        for (String key : config.getConfigurationSection("traders").getKeys(false)) {
            UUID id = UUID.fromString(key);
            String name = config.getString("traders." + key + ".name");
            EntityType type = EntityType.valueOf(config.getString("traders." + key + ".type", "VILLAGER"));
            Location loc = config.getLocation("traders." + key + ".location");
            boolean isNpc = config.getBoolean("traders." + key + ".isNpc");

            TraderEntry entry = new TraderEntry(id, name, loc, type, isNpc);
            if (config.contains("traders." + key + ".profession")) {
                entry.setProfession(Villager.Profession.valueOf(config.getString("traders." + key + ".profession")));
            }
            entry.setAnimationEnabled(config.getBoolean("traders." + key + ".animation", true));
            traderData.put(id, entry);
        }
    }

    public TraderEntry getTrader(UUID id) { return traderData.get(id); }
    public Collection<TraderEntry> getTraders() { return traderData.values(); }
    public void removeTrader(UUID id) { traderData.remove(id); save(); }
    public void reload() { despawnAll(); load(); spawn(); }
}