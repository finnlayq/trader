package de.juyas.customtrader;

import de.juyas.customtrader.model.TraderEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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

    public void spawn() {
        for (TraderEntry entry : traders) {
            spawn(entry);
        }
    }

    public void spawn(TraderEntry entry) {
        if (entry == null || !entry.isActive()) return;
        Location loc = entry.getLocation();
        if (loc == null || loc.getWorld() == null) return;

        // --- ANTI-GHOST-VILLAGER LOGIK ---
        // Entferne alle Entities (außer Spieler) an dieser Stelle, bevor wir neu spawnen
        loc.getWorld().getNearbyEntities(loc, 0.5, 1.0, 0.5).forEach(e -> {
            if (!(e instanceof Player)) {
                e.remove();
            }
        });

        // Alten Trader anhand der ID löschen (falls noch irgendwo geladen)
        Entity old = Bukkit.getEntity(entry.getId());
        if (old != null) old.remove();

        // Neuen Trader spawnen
        Entity entity = loc.getWorld().spawnEntity(loc, entry.getType());
        entry.setId(entity.getUniqueId());
        save(); // Neue ID direkt permanent speichern

        Bukkit.getScheduler().runTaskLater(CustomTraderPlugin.getInstance(), () -> {
            entity.setCustomName(entry.getName().replace("&", "§"));
            entity.setCustomNameVisible(true);

            if (entity instanceof LivingEntity living) {
                living.setInvulnerable(true); // HCS Schutz
                living.setAI(entry.isAnimationEnabled());
                living.setSilent(!entry.isAnimationEnabled());
                living.setPersistent(true);
            }

            if (entity instanceof Villager villager) {
                villager.setProfession(entry.getProfession());
                villager.setVillagerType(entry.getVillagerType());
                villager.setRecipes(entry.getOffers().stream()
                        .map(de.juyas.customtrader.model.TradeOfferEntry::getRecipe)
                        .collect(Collectors.toList()));
            }
        }, 2L);
    }

    public void despawnAll() {
        for (TraderEntry entry : traders) {
            Entity entity = Bukkit.getEntity(entry.getId());
            if (entity != null) entity.remove();

            // Falls die ID nicht gefunden wurde, zur Sicherheit am Ort suchen
            Location loc = entry.getLocation();
            if (loc != null && loc.getWorld() != null) {
                loc.getWorld().getNearbyEntities(loc, 0.5, 1.0, 0.5).forEach(e -> {
                    if (!(e instanceof Player)) e.remove();
                });
            }
            entry.setActive(false);
        }
        save();
    }

    public TraderEntry getTrader(UUID id) {
        return traders.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    public void removeTrader(UUID id) {
        TraderEntry entry = getTrader(id);
        if (entry != null) {
            Entity entity = Bukkit.getEntity(id);
            if (entity != null) entity.remove();
            traders.remove(entry);
            save();
        }
    }

    public void createTrader(UUID id, String name, Location loc, org.bukkit.entity.EntityType type, boolean active) {
        TraderEntry entry = new TraderEntry(id, name, loc, type, active);
        traders.add(entry);
        spawn(entry);
        save();
    }

    public List<TraderEntry> getTraders() {
        return traders;
    }
}