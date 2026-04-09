package de.juyas.customtrader.villager;

import de.juyas.customtrader.api.*;
import de.juyas.customtrader.model.TradeOfferEntry;
import de.juyas.customtrader.model.TraderEntry;
import de.juyas.utils.api.data.base.ModelBase;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;

import java.util.*;

/**
 * @author Juyas
 * @version 22.11.2024
 * @since 22.11.2024
 */
public final class VillagerHandler implements TraderNPCHandler
{

    @Getter
    private final TraderEntry traderData;

    private final TraderResetTimer traderResetTimer;

    private VillagerHandler( TraderEntry traderNPC )
    {
        this.traderData = traderNPC;
        this.traderResetTimer = new TraderResetTimer( this );
    }

    public static VillagerHandler from( TraderEntry traderData )
    {
        return new VillagerHandler( traderData );
    }

    public static VillagerHandler fromNew( Villager villager )
    {
        VillagerHandler handler = new VillagerHandler( ModelBase.generate( TraderEntry.class ) );
        handler.takeFrom( villager );
        return handler;
    }

    private void setLocation( Location bukkitLocation )
    {
        this.traderData.setLocation( bukkitLocation );
    }

    private void setProfession( Villager.Profession profession )
    {
        traderData.getProfession().value().set( VillagerEnumMapper.map( profession ) );
    }

    private Villager.Profession getProfession()
    {
        return VillagerEnumMapper.mapProfession( traderData.getProfession().value().get().orElse( null ) );
    }

    private void setVillagerType( Villager.Type villagerType )
    {
        traderData.getVillagerType().value().set( VillagerEnumMapper.map( villagerType ) );
    }

    private Villager.Type getVillagerType()
    {
        return VillagerEnumMapper.mapType( traderData.getVillagerType().value().get().orElse( null ) );
    }

    private void respawnEntity()
    {
        killEntity();
        spawnEntity();
    }

    private Villager getEntity()
    {
        Location location = traderData.location();
        if ( location == null ) return null;
        World world = location.getWorld();
        if ( world == null ) return null;
        Entity result = world.getNearbyEntities( location, 0.1, 0.1, 0.1 )
                .stream().filter( e -> e instanceof Villager ).findFirst().orElse( null );
        if ( result == null ) return null;
        return (Villager) result;
    }

    private void killEntity()
    {
        removeProximityVillagers();
    }

    private void removeProximityVillagers()
    {
        Location location = traderData.location();
        if ( location == null || location.getWorld() == null ) return;
        Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities( location, 0.1, 0.1, 0.1 );
        nearbyEntities.forEach( entity ->
        {
            if ( entity instanceof Villager villager )
                villager.remove();
        } );
    }

    private void removeProximityOverlapVillagers()
    {
        Location location = traderData.location();
        if ( location == null || location.getWorld() == null ) return;
        Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities( location, 0.1, 0.1, 0.1 );
        if ( nearbyEntities.isEmpty() || nearbyEntities.size() == 1 ) return;
        nearbyEntities.stream().filter( entity -> entity instanceof Villager )
                .skip( 1 ).forEach( Entity::remove );
    }

    private void configureVillager( Villager villager )
    {
        villager.setAdult();
        villager.setProfession( getProfession() );
        villager.setVillagerType( getVillagerType() );
        villager.setVillagerLevel( 5 );
        villager.setVillagerExperience( 250 );
        villager.setAI( false );
        villager.setCanPickupItems( false );
        villager.setPersistent( true );
        villager.setRemoveWhenFarAway( false );
        String name = traderData.getAttribute( TraderAttribute.NAME );
        villager.customName( Component.text( name ) );
        villager.setCustomNameVisible( true );
    }

    private void spawnEntity()
    {
        Villager entity = getEntity();
        if ( entity != null ) return;
        Location location = traderData.location();
        if ( location == null || location.getWorld() == null ) return;
        removeProximityVillagers();
        Villager villager = location.getWorld().spawn( location, Villager.class );
        configureVillager( villager );
        resetOffers();
    }

    private void takeFrom( Villager villager )
    {
        traderData.applyDefaults();
        setLocation( villager.getLocation() );
        setProfession( villager.getProfession() );
        setVillagerType( villager.getVillagerType() );
        if ( traderData.insert() )
        {
            traderData.triggerOnceOnRelease( () -> villager.getRecipes().stream()
                    .map( TradeOfferData::fromRecipe ).forEach( traderData::addOffer ) );
        }
        //respawn
        respawnEntity();
    }

    @Override
    public Entity entity()
    {
        return getEntity();
    }

    @Override
    public Trader trader()
    {
        return traderData;
    }

    @Override
    public TraderResetTimer timer()
    {
        return traderResetTimer;
    }

    @Override
    public boolean matchEntity( Entity entity )
    {
        Location location = traderData.location();
        if ( location == null || location.getWorld() == null ) return false;
        return location.getWorld().getNearbyEntities( location, 0.1, 0.1, 0.1 ).contains( entity );
    }

    @Override
    public void resetOffers()
    {
        removeProximityOverlapVillagers();
        Villager villager = getEntity();
        if ( villager == null ) return;
        List<MerchantRecipe> recipes = new ArrayList<>();
        for ( TradeOfferEntry trade : traderData.getTradeOffers() )
        {
            MerchantRecipe recipe = trade.recipe();
            if ( recipe != null ) recipes.add( recipe );
        }
        villager.setRecipes( recipes );
    }

    @Override
    public void playAnimation()
    {
        Villager vil = getEntity();
        if ( vil == null ) return;
        vil.shakeHead();
        vil.getWorld().spawnParticle( Particle.COMPOSTER, vil.getEyeLocation(), 15, 0.3, 0.3, 0.3 );
    }

    @Override
    public void spawn()
    {
        spawnEntity();
        traderResetTimer.start();
    }

    @Override
    public void despawn()
    {
        traderResetTimer.stop();
        killEntity();
    }

}