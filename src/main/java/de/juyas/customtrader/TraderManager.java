package de.juyas.customtrader;

import de.juyas.customtrader.api.TraderAttribute;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.customtrader.citizens.CitizensHandler;
import de.juyas.customtrader.model.TradeOfferEntry;
import de.juyas.customtrader.model.TraderEntry;
import de.juyas.customtrader.villager.VillagerHandler;
import de.juyas.utils.api.data.base.ModelBase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Juyas
 * @version 26.11.2023
 * @since 26.11.2023
 */
public class TraderManager
{

    private boolean citizens;

    private final List<TraderNPCHandler> traders;

    public TraderManager()
    {
        this.citizens = false;
        this.traders = new ArrayList<>();
    }

    public boolean load( boolean citizens )
    {
        this.traders.clear();
        ModelBase.generate( TraderEntry.class ).init();
        ModelBase.generate( TradeOfferEntry.class ).init();
        this.citizens = citizens;
        TraderEntry.loadEntries( list ->
        {
            list.forEach( this::createHandler );
            CustomTraderPlugin.getInstance().info( traders.size() + " traders loaded." );
        } );
        return true;
    }

    private TraderNPCHandler createHandler( TraderEntry entry )
    {
        if ( citizens && entry.getAttribute( TraderAttribute.PREFER_NPC ) )
        {
            CitizensHandler handler = new CitizensHandler( entry );
            traders.add( handler );
            return handler;
        }
        else
        {
            VillagerHandler handler = VillagerHandler.from( entry );
            traders.add( handler );
            return handler;
        }
    }

    public boolean removeTrader( Entity entity )
    {
        TraderNPCHandler trader = getTrader( entity );
        if ( trader != null )
        {
            trader.despawn();
            trader.trader().deleteData();
            return this.traders.remove( trader );
        }
        return false;
    }

    public void saveTrader( Entity entity )
    {
        TraderNPCHandler trader = getTrader( entity );
        if ( trader != null )
            trader.trader().updateData();
    }

    public TraderNPCHandler getTrader( Entity villager )
    {
        return traders.stream()
                .filter( handler -> handler.matchEntity( villager ) )
                .findFirst().orElse( null );
    }

    public boolean isKnown( Entity entity )
    {
        return traders.stream().anyMatch( handler -> handler.matchEntity( entity ) );
    }

    public void registerNew( Villager villager )
    {
        registerNew( villager, false );
    }

    public void registerNew( Villager villager, boolean asNPC )
    {
        TraderEntry entry = ModelBase.generate( TraderEntry.class );
        entry.from( villager );
        entry.applyDefaults();
        entry.setAttribute( TraderAttribute.PREFER_NPC, asNPC );
        entry.insert();
        if ( asNPC ) villager.remove();
        TraderNPCHandler handler = createHandler( entry );
        handler.respawn();
    }

    public void restartTimers()
    {
        traders.forEach( handler -> handler.timer().start() );
    }

    public void save()
    {
        traders.forEach( handler -> handler.trader().updateData() );
    }

    public void wipe()
    {
        traders.forEach( TraderNPCHandler::despawn );
    }

    public void spawn()
    {
        traders.forEach( TraderNPCHandler::spawn );
    }

}