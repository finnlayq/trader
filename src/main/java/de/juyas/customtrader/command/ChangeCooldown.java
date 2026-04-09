package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderAttribute;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.utils.api.command.Arguments;
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
public class ChangeCooldown extends AbstractSelectionCommand
{

    private final HashMap<UUID, Integer> restockTime;

    public ChangeCooldown()
    {
        super( "restockTime", "cooldown" );
        setSignature( "seconds" );
        setMinArgs( 1 );
        setDescription( "Ändere die Häufigkeit mit der ein Händler seine Waren auffüllt" );
        restockTime = new HashMap<>();
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        Integer restock = Arguments.validateRange( player, args[0], Integer::parseInt, 1, 3600 * 24 );
        if ( restock == null ) return;
        putInQueue( player );
        this.restockTime.put( player.getUniqueId(), restock );
        Chat.send( player, "§aKlicke einen Händler an, damit er alle §9" + restock + " §aSekunden seine Waren auffüllt." );
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        info.trader().setAttribute( TraderAttribute.REFRESH_SECONDS, restockTime.remove( player.getUniqueId() ) );
        pullFromQueue( player );
        info.respawn();
        Chat.send( player, "§aDer Händler füllt nun alle §9" + info.trader().getAttribute( TraderAttribute.REFRESH_SECONDS ) + " §aSekunden seine Waren wieder auf." );
        return true;
    }

}