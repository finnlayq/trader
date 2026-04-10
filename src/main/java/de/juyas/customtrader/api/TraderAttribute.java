package de.juyas.customtrader.api;

import de.juyas.customtrader.model.TraderEntry;
import org.bukkit.Location;
import java.util.function.Function;

public enum TraderAttribute {

    VILLAGER_PROFESSION(entry -> null),
    VILLAGER_TYPE(entry -> null),
    LOCATION(entry -> entry.getLocation()),
    REFRESH_SECONDS(entry -> entry.getRefreshSeconds()),

    // Sicherere Schreibweise per Lambda:
    ANIMATION(entry -> entry.isAnimationEnabled());

    private final Function<TraderEntry, Object> valueFetcher;

    TraderAttribute(Function<TraderEntry, Object> valueFetcher) {
        this.valueFetcher = valueFetcher;
    }

    public Object getValue(TraderEntry entry) {
        return valueFetcher.apply(entry);
    }
}