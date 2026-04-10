package de.juyas.customtrader;

import de.juyas.customtrader.model.TraderEntry;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Collection;

public class TraderManager {

    private final Map<UUID, TraderEntry> traderData = new HashMap<>();

    public void createTrader(UUID id, String name, Location loc, EntityType type, boolean npc) {
        TraderEntry entry = new TraderEntry(id, name, loc, type, npc);
        traderData.put(entry.getId(), entry);
    }

    public TraderEntry getTrader(UUID id) {
        return traderData.get(id);
    }

    public Collection<TraderEntry> getTraders() {
        return traderData.values();
    }

    public void removeTrader(UUID id) {
        traderData.remove(id);
    }

    public void spawn() {}

    public void despawnAll() {}

    public void save() {}

    public void reload() {
        despawnAll();
        spawn();
    }
}