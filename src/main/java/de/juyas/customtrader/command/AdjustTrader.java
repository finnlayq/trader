package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderAttribute;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.utils.api.command.TabCompletion;
import de.juyas.utils.api.hud.Chat;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Juyas
 * @version 26.11.2023
 * @since 26.11.2023
 */
public class AdjustTrader extends AbstractSelectionCommand
{

    public record Adjustments(boolean centerX, boolean centerZ, boolean groundY, Float yaw, Float pitch) {}

    private final HashMap<UUID, Adjustments> adjustmentMap;

    public AdjustTrader()
    {
        super( "align", "adjust" );
        setSignature( "centerX", "centerZ", "groundY", "yaw", "pitch" );
        setDescription( "Korrigiert die Position eines Händlers" );
        setMinArgs( 3 );
        this.adjustmentMap = new HashMap<>();
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        Float yaw, pitch;
        boolean centerX, centerZ, groundY;
        try
        {
            centerX = Boolean.parseBoolean( args[0] );
            centerZ = Boolean.parseBoolean( args[1] );
            groundY = Boolean.parseBoolean( args[2] );
            yaw = args.length == 5 ? Float.parseFloat( args[3] ) : null;
            pitch = args.length == 5 ? Float.parseFloat( args[4] ) : null;
        }
        catch ( Exception e )
        {
            return;
        }
        Adjustments adjustments = new Adjustments( centerX, centerZ, groundY, yaw, pitch );
        adjustmentMap.put( player.getUniqueId(), adjustments );
        putInQueue( player );
        Chat.send( player, "§aKlicke nun den Händler an, der justiert werden soll." );
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        Location location = info.trader().location();
        Adjustments adjustments = adjustmentMap.get( player.getUniqueId() );
        if ( adjustments.centerX() ) location.setX( location.getBlockX() + 0.5f );
        if ( adjustments.centerZ() ) location.setZ( location.getBlockZ() + 0.5f );
        if ( adjustments.groundY() ) location.setY( location.getBlockY() + 0.05f );
        if ( adjustments.yaw() != null && adjustments.pitch() != null )
        {
            location.setYaw( adjustments.yaw() );
            location.setPitch( adjustments.pitch() );
        }
        info.trader().setAttribute( TraderAttribute.LOCATION, location );
        pullFromQueue( player );
        info.respawn();
        Chat.send( player, "§aHändler wurde angewiesen sich ordentlich hinzustellen." );
        return true;
    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        if ( args.length == 1 || args.length == 2 || args.length == 3 )
            return TabCompletion.BOOLEAN;
        return TabCompletion.NONE;
    }

}