package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.TraderManager;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.utils.api.command.PlayerCommand;
import de.juyas.utils.api.hud.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.*;

/**
 * @author Juyas
 * @version 27.11.2023
 * @since 27.11.2023
 */
public abstract class AbstractSelectionCommand extends PlayerCommand implements Listener
{

    private final Set<UUID> registrationQueue;

    public AbstractSelectionCommand( String name, String... aliases )
    {
        super( name, aliases );
        this.registrationQueue = new HashSet<>( 5 );
        Bukkit.getPluginManager().registerEvents( this, CustomTraderPlugin.getInstance() );
    }

    protected boolean inQueue( Player player )
    {
        return this.registrationQueue.contains( player.getUniqueId() );
    }

    protected void putInQueue( Player player )
    {
        this.registrationQueue.add( player.getUniqueId() );
    }

    protected void pullFromQueue( Player player )
    {
        this.registrationQueue.remove( player.getUniqueId() );
    }

    protected TraderManager manager()
    {
        return CustomTraderPlugin.getInstance().getManager();
    }

    public boolean onInteractUnknownEntity( Player player, Entity entity )
    {
        pullFromQueue( player );
        Chat.send( player, "§cDieser Händler ist noch nicht im Register!" );
        return true;
    }

    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        pullFromQueue( player );
        Chat.send( player, "§cDieser Händler ist schon eingetragen." );
        return true;
    }

    @EventHandler
    public void onInteract( PlayerInteractEntityEvent event )
    {
        if ( !registrationQueue.contains( event.getPlayer().getUniqueId() ) ) return;
        TraderManager manager = CustomTraderPlugin.getInstance().getManager();
        Entity entity = event.getRightClicked();
        boolean known = manager.isKnown( entity );
        if ( known )
        {
            if ( onInteractKnownTrader( event.getPlayer(), entity, manager.getTrader( entity ) ) )
                event.setCancelled( true );
        }
        else
        {
            if ( onInteractUnknownEntity( event.getPlayer(), entity ) )
                event.setCancelled( true );
        }
    }

}