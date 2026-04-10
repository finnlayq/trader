package de.juyas.customtrader.model;

import de.juyas.customtrader.api.Trader;
import de.juyas.customtrader.api.TraderAttribute;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TraderEntry implements Trader {

    private final UUID id;
    private Location location;
    private String name;
    private boolean npc;
    private EntityType type;
    private int refreshSeconds = 0;
    private boolean animationEnabled = true;
    private final List<TradeOfferEntry> offers = new ArrayList<>();

    public TraderEntry(UUID id, String name, Location location, EntityType type, boolean npc) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.type = type;
        this.npc = npc;
    }

    public TraderEntry(UUID id) {
        this(id, "Trader", null, EntityType.VILLAGER, false);
    }

    // Damit der TraderManager zufrieden ist:
    public UUID getUniqueId() {
        return id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Location getLocation() { return location; }

    @Override
    public void setLocation(Location location) { this.location = location; }

    @Override
    public boolean isNpc() { return npc; }

    public void setNpc(boolean npc) { this.npc = npc; }

    @Override
    public EntityType getType() { return type; }

    public void setType(EntityType type) { this.type = type; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getRefreshSeconds() { return refreshSeconds; }

    public void setRefreshSeconds(int refreshSeconds) { this.refreshSeconds = refreshSeconds; }

    public boolean isAnimationEnabled() { return animationEnabled; }

    public void setAnimationEnabled(boolean animationEnabled) { this.animationEnabled = animationEnabled; }

    public List<TradeOfferEntry> getOffers() { return offers; }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(TraderAttribute attribute) {
        return (T) attribute.getValue(this);
    }
}