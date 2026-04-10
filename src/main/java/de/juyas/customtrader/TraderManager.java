package de.juyas.customtrader;

import de.juyas.customtrader.model.TraderEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TraderManager {

    private final List<TraderEntry> traders = new ArrayList<>();
    private final File file;
    private FileConfiguration config;

    // Fix für CustomTraderPlugin.java:20
    public TraderManager() {
        this(CustomTraderPlugin.getInstance().getDataFolder());
    }

    public TraderManager(File dataFolder) {
        if (!dataFolder.exists()) dataFolder.mkdirs();
        this.file = new File(dataFolder, "traders.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        reload();
    }

    // Fix für Reload.java:24 und ReloadTrader.java:15
    public void reload() {
        traders.clear();
        config = YamlConfiguration.loadConfiguration(file);
        if (config.contains("traders")) {
            for (String key : config.getConfigurationSection("traders").getKeys(false)) {
                Object obj = config.get("traders." + key);
                if (obj instanceof TraderEntry entry) {
                    traders.add(entry);
                }
            }
        }
    }

    public void save() {
        config.set("traders", null);
        for (TraderEntry entry : traders) {
            config.set("traders." + entry.getId().toString(), entry);
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fix für SpawnAll.java:17 und CustomTraderPlugin.java:27
    public void spawn() {
        for (TraderEntry entry : traders) {
            spawn(entry);
        }
    }

    public void spawn(TraderEntry entry) {
        if (entry == null || !entry.isActive()) return;
        Location loc = entry.getLocation();
        if (loc == null || loc.getWorld() == null) return;

        Entity old = Bukkit.getEntity(entry.getId());
        if (old != null) old.remove();

        Entity entity = loc.getWorld().spawnEntity(loc, entry.getType());
        entry.setId(entity.getUniqueId());

        Bukkit.getScheduler().runTaskLater(CustomTraderPlugin.getInstance(), () -> {
            entity.setCustomName(entry.getName());
            entity.setCustomNameVisible(true);

            if (entity instanceof LivingEntity living) {
                living.setInvulnerable(true);
                boolean isAnimated = entry.isAnimationEnabled();
                living.setAI(isAnimated);
                living.setSilent(!isAnimated);
                living.setPersistent(true);
            }

            if (entity instanceof Villager villager) {
                villager.setProfession(entry.getProfession());
                villager.setRecipes(entry.getOffers().stream()
                        .map(de.juyas.customtrader.model.TradeOfferEntry::getRecipe)
                        .collect(Collectors.toList()));
            }
        }, 2L);
    }

    // Fix für WipeAll.java:17 und Reload.java:21
    public void despawnAll() {
        for (TraderEntry entry : traders) {
            Entity entity = Bukkit.getEntity(entry.getId());
            if (entity != null) entity.remove();
            entry.setActive(false);
        }
        save();
    }

    // Fix für CitizensHandler.java:19, CreateTrader.java:34, AbstractSelectionCommand:29, DeletionListener:15
    public TraderEntry getTrader(UUID id) {
        if (id == null) return null;
        return traders.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    // Fix für RemoveTrader.java:11 und DeletionListener:18
    public void removeTrader(UUID id) {
        TraderEntry entry = getTrader(id);
        if (entry != null) {
            Entity entity = Bukkit.getEntity(id);
            if (entity != null) entity.remove();
            traders.remove(entry);
            save();
        }
    }

    // Fix für CreateTrader.java:44
    public void createTrader(UUID id, String name, Location loc, EntityType type, boolean active) {
        TraderEntry entry = new TraderEntry(id, name, loc, type, active);
        traders.add(entry);
        spawn(entry);
        save();
    }

    public List<TraderEntry> getTraders() {
        return traders;
    }
}