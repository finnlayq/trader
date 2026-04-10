package de.juyas.customtrader.api;

import de.juyas.customtrader.model.TradeOfferEntry;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import java.util.List;
import java.util.UUID;

public interface Trader {
    UUID getId();
    Location getLocation();
    <T> T getAttribute(TraderAttribute attribute);
    void setLocation(Location location);
    boolean isNpc();
    EntityType getType();
    List<TradeOfferEntry> getOffers();
}