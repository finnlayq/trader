package de.juyas.customtrader.api;

import de.juyas.customtrader.model.TraderEntry;
import org.bukkit.entity.Villager;
import java.util.function.Function;

public enum TraderAttribute {

    VILLAGER_PROFESSION(entry -> Villager.Profession.NONE),
    VILLAGER_TYPE(entry -> Villager.Type.PLAINS),
    REFRESH_SECONDS(entry -> entry.getRefreshSeconds()),
    ANIMATION(entry -> entry.isAnimationEnabled());

    private final Function<TraderEntry, ?> getter;

    TraderAttribute(Function<TraderEntry, ?> getter) {
        this.getter = getter;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(TraderEntry entry) {
        return (T) getter.apply(entry);
    }
}