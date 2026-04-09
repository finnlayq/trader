package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.utils.api.hud.Chat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @author Juyas
 * @version 26.11.2023
 * @since 26.11.2023
 */
public class RemoveTrader extends AbstractSelectionCommand
{

    public RemoveTrader()
    {
        super( "remove", "unregister" );
        setDescription( "Entfernt einen Händlers permanent aus dem Register.", "Dieser Befehl kann durch erneute Eingabe auch abgebrochen werden." );
        setMinArgs( 0 );
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        if ( !inQueue( player ) )
        {
            putInQueue( player );
            Chat.send( player, "§aKlicke nun den Händler an, der entfernt werden soll." );
        }
        else
        {
            pullFromQueue( player );
            Chat.send( player, "§cHändlerdezimierung abgebrochen." );
        }
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        if ( CustomTraderPlugin.getInstance().getManager().removeTrader( entity ) )
        {
            pullFromQueue( player );
            Chat.send( player, "§aHändler wurde angewiesen sich zu entfernen." );
            return true;
        }
        else Chat.send( player, "§cBeim Beseitigen ist etwas schiefgelaufen." );
        return false;
    }

}