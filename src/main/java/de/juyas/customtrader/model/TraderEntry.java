package de.juyas.customtrader.model;

import de.juyas.customtrader.api.TraderAttribute;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TraderEntry {

    private UUID id;
    private String name;
    private Location location;
    private EntityType type;
    private boolean npc;
    private List<TradeOfferEntry> offers = new ArrayList<>();

    private int refreshSeconds = 300;
    private boolean animationEnabled = true;
    private Villager.Profession profession = Villager.Profession.NONE;

    public TraderEntry(UUID id, String name, Location location, EntityType type, boolean npc) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.type = type;
        this.npc = npc;
    }

    public <T> T getAttribute(TraderAttribute attribute) {
        return attribute.getValue(this);
    }

    // GETTER & SETTER FÜR SPEICHERUNG
    public Villager.Profession getProfession() { return profession; }
    public void setProfession(Villager.Profession profession) { this.profession = profession; }

    public int getRefreshSeconds() { return refreshSeconds; }
    public void setRefreshSeconds(int refreshSeconds) { this.refreshSeconds = refreshSeconds; }
    public boolean isAnimationEnabled() { return animationEnabled; }
    public void setAnimationEnabled(boolean animationEnabled) { this.animationEnabled = animationEnabled; }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public EntityType getType() { return type; }
    public void setType(EntityType type) { this.type = type; }
    public boolean isNpc() { return npc; }
    public void setNpc(boolean npc) { this.npc = npc; }
    public List<TradeOfferEntry> getOffers() { return offers; }
    public void setOffers(List<TradeOfferEntry> offers) { this.offers = offers; }
}