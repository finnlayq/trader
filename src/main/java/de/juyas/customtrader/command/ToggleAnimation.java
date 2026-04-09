package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderAttribute;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.utils.api.hud.Chat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @author Juyas
 * @version 27.11.2023
 * @since 27.11.2023
 */
public class ToggleAnimation extends AbstractSelectionCommand
{

    public ToggleAnimation()
    {
        super( "toggleAnimation" );
        setMinArgs( 0 );
        setDescription( "Schalte die Animation eines Händlers an/aus" );
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        putInQueue( player );
        Chat.send( player, "§aKlicke einen Händler an, um seine Animation umzuschalten." );
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        pullFromQueue( player );
        boolean animation = info.trader().getAttribute( TraderAttribute.ANIMATION );
        animation = !animation;
        info.trader().setAttribute( TraderAttribute.ANIMATION, animation );
        Chat.send( player, "§7Die Animation des Händlers ist nun " + ( animation ? "§aeingeschaltet." : "§causgeschaltet." ) );
        return true;
    }

}