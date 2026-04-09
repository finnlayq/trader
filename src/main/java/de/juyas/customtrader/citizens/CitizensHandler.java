package de.juyas.customtrader.citizens;

import de.juyas.customtrader.api.*;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.*;
import net.citizensnpcs.trait.*;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.UUID;

/**
 * @author Juyas
 * @version 23.11.2024
 * @since 23.11.2024
 */
public final class CitizensHandler implements TraderNPCHandler
{

    @Getter
    private static NPCRegistry npcRegistry;

    public static void init()
    {
        if ( npcRegistry != null )
            deregister();
        npcRegistry = CitizensAPI.createAnonymousNPCRegistry( new MemoryNPCDataStore() );
    }

    public static void deregister()
    {
        npcRegistry.deregisterAll();
        npcRegistry = null;
    }

    private final Trader traderData;
    private final TraderResetTimer resetTimer;
    private NPC activeNpc;

    public CitizensHandler( Trader traderData )
    {
        this.traderData = traderData;
        this.activeNpc = null;
        this.resetTimer = new TraderResetTimer( this );
    }

    @Override
    public Entity entity()
    {
        return activeNpc != null ? activeNpc.getEntity() : null;
    }

    @Override
    public Trader trader()
    {
        return traderData;
    }

    @Override
    public boolean matchEntity( Entity entity )
    {
        return this.activeNpc != null && this.activeNpc.getEntity() != null
                && entity != null && this.activeNpc.getEntity().equals( entity );
    }

    @Override
    public TraderResetTimer timer()
    {
        return resetTimer;
    }

    @Override
    public void playAnimation()
    {
        Entity entity = entity();
        if ( entity == null ) return;
        Location location = entity.getLocation().add( 0, 1.5, 0 );
        entity.getWorld().spawnParticle( Particle.COMPOSTER, location, 15, 0.3, 0.3, 0.3 );
    }

    @Override
    public void resetOffers()
    {
        if ( activeNpc == null ) return;
        this.activeNpc.getTraitOptional( TraderTrait.class ).toJavaUtil().ifPresent( TraderTrait::reset );
    }

    @Override
    public void spawn()
    {
        if ( activeNpc != null ) despawn();
        String name = traderData.getAttribute( TraderAttribute.NAME );
        activeNpc = npcRegistry.createNPC( EntityType.PLAYER, name );
        if ( traderData.hasNPCSkin() )
        {
            SkinTrait skinTrait = new SkinTrait();
            activeNpc.addTrait( skinTrait );
            String[] skin = traderData.getAttribute( TraderAttribute.SKIN );
            skinTrait.setSkinPersistent( UUID.randomUUID().toString(), skin[0], skin[1] );
        }
        LookClose lookClose = new LookClose();
        lookClose.setTargetNPCs( false );
        lookClose.setRange( 4.0 );
        lookClose.lookClose( true );
        activeNpc.addTrait( lookClose );
        activeNpc.addTrait( HologramTrait.class );
        activeNpc.getTraitOptional( HologramTrait.class ).toJavaUtil().ifPresent( trait -> trait.addLine( name ) );
        activeNpc.data().set( NPC.Metadata.NAMEPLATE_VISIBLE, false );
        activeNpc.addTrait( new TraderTrait( traderData ) );
        if ( activeNpc.spawn( traderData.location() ) )
            resetTimer.start();
    }

    @Override
    public void despawn()
    {
        resetTimer.stop();
        if ( activeNpc == null ) return;
        activeNpc.despawn();
        activeNpc.destroy();
        activeNpc = null;
    }

}