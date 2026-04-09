package de.juyas.customtrader.api;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Juyas
 * @version 22.11.2024
 * @since 22.11.2024
 */
public interface Trader
{

    Location location();

    @NotNull
    <T> T getAttribute( TraderAttribute<T> attribute );

    <T> void setAttribute( TraderAttribute<T> attribute, T value );

    boolean hasNPCSkin();

    List<TradeOffer> offers();

    void addOffer( TradeOfferData offer );

    boolean removeOffer( long id );

    void removeAllOffers();

    void deleteData();

    void updateData();

}