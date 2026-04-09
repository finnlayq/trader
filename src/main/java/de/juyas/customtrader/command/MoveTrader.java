package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderAttribute;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.utils.api.hud.Chat;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Juyas
 * @version 26.11.2023
 * @since 26.11.2023
 */
public class MoveTrader extends AbstractSelectionCommand
{

    private final HashMap<UUID, Location> locationMap;

    public MoveTrader()
    {
        super( "move", "adjust" );
        setDescription( "Bewegt einen Händlers an deine Position bei Eingabe des Befehls." );
        setMinArgs( 0 );
        this.locationMap = new HashMap<>();
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        locationMap.put( player.getUniqueId(), player.getLocation() );
        putInQueue( player );
        Chat.send( player, "§aKlicke nun den Händler an, der bewegt werden soll." );
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        info.trader().setAttribute( TraderAttribute.LOCATION, locationMap.remove( player.getUniqueId() ) );
        pullFromQueue( player );
        info.respawn();
        Chat.send( player, "§aHändler wurde angewiesen sich woanders hinzustellen." );
        return true;
    }

}