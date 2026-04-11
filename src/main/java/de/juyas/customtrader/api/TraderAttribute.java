package de.juyas.customtrader.api;

import de.juyas.customtrader.model.TraderEntry;
import org.bukkit.entity.Villager;
import java.util.function.Function;

public enum TraderAttribute {

    VILLAGER_PROFESSION(TraderEntry::getProfession),
    VILLAGER_TYPE(TraderEntry::getVillagerType),
    REFRESH_SECONDS(TraderEntry::getRefreshSeconds),
    ANIMATION(TraderEntry::isAnimationEnabled);

    private final Function<TraderEntry, ?> getter;

    TraderAttribute(Function<TraderEntry, ?> getter) {
        this.getter = getter;
    }

    @SuppressWarnings("unchecked")
    public <T> Function<TraderEntry, T> getGetter() {
        return (Function<TraderEntry, T>) getter;
    }
}