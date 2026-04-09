package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderAttribute;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.utils.api.command.ArgumentRange;
import de.juyas.utils.api.hud.Chat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Juyas
 * @version 27.11.2023
 * @since 27.11.2023
 */
public class ChangeName extends AbstractSelectionCommand
{

    private final HashMap<UUID, String> names;

    public ChangeName()
    {
        super( "name" );
        setSignature( "name" );
        setArgumentRange( ArgumentRange.openMax( 1 ) );
        setDescription( "Gibt einem Händler einen Namen." );
        names = new HashMap<>();
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        String name = String.join( " ", args );
        names.put( player.getUniqueId(), name );
        putInQueue( player );
        Chat.send( player, "§aKlicke einen Händler an, damit er seinen Namen ändert." );
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        info.trader().setAttribute( TraderAttribute.NAME, names.get( player.getUniqueId() ) );
        names.remove( player.getUniqueId() );
        pullFromQueue( player );
        info.respawn();
        Chat.send( player, "§aDer Händler hat nun einen anderen Namen." );
        return true;
    }

}