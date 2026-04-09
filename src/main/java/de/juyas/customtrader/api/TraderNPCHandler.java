package de.juyas.customtrader.api;

import org.bukkit.entity.Entity;

/**
 * @author Juyas
 * @version 23.11.2024
 * @since 23.11.2024
 */
public interface TraderNPCHandler
{

    Entity entity();

    Trader trader();

    boolean matchEntity( Entity entity );

    TraderResetTimer timer();

    void playAnimation();

    void resetOffers();

    void spawn();

    void despawn();

    default void respawn()
    {
        despawn();
        spawn();
    }

}