package de.juyas.customtrader.model;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TraderEntry implements ConfigurationSerializable {

    private UUID id;
    private String name;
    private Location location;
    private EntityType type;
    private Villager.Profession profession = Villager.Profession.NONE;
    private Villager.Type villagerType = Villager.Type.PLAINS;
    private boolean active;
    private boolean animationEnabled = true;
    private int refreshSeconds = 300;
    private boolean isNpc = false;
    private List<TradeOfferEntry> offers = new ArrayList<>();

    // Konstruktor für manuelle Erstellung (z.B. /createtrader)
    public TraderEntry(UUID id, String name, Location location, EntityType type, boolean active) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.type = type;
        this.active = active;
    }

    // --- GETTER & SETTER (Fix für die 70+ Fehler) ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public EntityType getType() { return type; }
    public void setType(EntityType type) { this.type = type; }
    public Villager.Profession getProfession() { return profession; }
    public void setProfession(Villager.Profession profession) { this.profession = profession; }
    public Villager.Type getVillagerType() { return villagerType; }
    public void setVillagerType(Villager.Type villagerType) { this.villagerType = villagerType; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isAnimationEnabled() { return animationEnabled; }
    public void setAnimationEnabled(boolean animationEnabled) { this.animationEnabled = animationEnabled; }
    public int getRefreshSeconds() { return refreshSeconds; }
    public void setRefreshSeconds(int refreshSeconds) { this.refreshSeconds = refreshSeconds; }
    public boolean isNpc() { return isNpc; }
    public void setNpc(boolean npc) { isNpc = npc; }
    public List<TradeOfferEntry> getOffers() { return offers; }
    public void setOffers(List<TradeOfferEntry> offers) { this.offers = offers; }

    // --- ATTRIBUT LOGIK (Fix für TraderNPCHandler) ---
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(de.juyas.customtrader.api.TraderAttribute attribute) {
        return (T) attribute.getGetter().apply(this);
    }

    // --- SERIALISIERUNG (Fix für den Paper 1.21.1 Configuration Error) ---
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id.toString());
        map.put("name", name);
        map.put("location", location);
        map.put("type", type.name());
        map.put("profession", profession.name());
        map.put("villagerType", villagerType.name());
        map.put("active", active);
        map.put("animation", animationEnabled);
        map.put("refreshSeconds", refreshSeconds);
        map.put("isNpc", isNpc);
        map.put("offers", offers);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static TraderEntry deserialize(Map<String, Object> map) {
        UUID id = UUID.fromString((String) map.get("id"));
        String name = (String) map.get("name");
        Location loc = (Location) map.get("location");
        EntityType type = EntityType.valueOf((String) map.get("type"));
        boolean active = (boolean) map.get("active");

        TraderEntry entry = new TraderEntry(id, name, loc, type, active);

        if (map.containsKey("profession")) entry.setProfession(Villager.Profession.valueOf((String) map.get("profession")));
        if (map.containsKey("villagerType")) entry.setVillagerType(Villager.Type.valueOf((String) map.get("villagerType")));
        if (map.containsKey("animation")) entry.setAnimationEnabled((boolean) map.get("animation"));
        if (map.containsKey("refreshSeconds")) entry.setRefreshSeconds((int) map.get("refreshSeconds"));
        if (map.containsKey("isNpc")) entry.setNpc((boolean) map.get("isNpc"));
        if (map.containsKey("offers")) entry.setOffers((List<TradeOfferEntry>) map.get("offers"));

        return entry;
    }
}