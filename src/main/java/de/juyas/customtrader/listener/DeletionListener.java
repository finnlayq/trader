package de.juyas.customtrader.listener;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.utils.api.hud.Chat;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

/**
 * @author Juyas
 * @version 27.11.2023
 * @since 27.11.2023
 */
public class DeletionListener implements Listener
{

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVillagerKill( EntityDeathEvent event )
    {
        if ( CustomTraderPlugin.getInstance().getManager().removeTrader( event.getEntity() ) )
        {
            if ( event.getEntity().getKiller() != null )
                Chat.send( event.getEntity().getKiller(), "§aHändler aus dem Register... §oentfernt." );
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVillagerHarm( EntityDamageByEntityEvent event )
    {
        if ( CustomTraderPlugin.getInstance().getManager().isKnown( event.getEntity() ) )
        {
            if ( event.getDamager().hasPermission( "trader.edit" ) ) return;
            event.setDamage( 0 );
            event.setCancelled( true );
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVillagerHarm( EntityDamageByBlockEvent event )
    {
        if ( CustomTraderPlugin.getInstance().getManager().isKnown( event.getEntity() ) )
        {
            event.setDamage( 0 );
            event.setCancelled( true );
        }
    }

}