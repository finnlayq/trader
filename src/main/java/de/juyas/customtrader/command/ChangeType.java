package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderAttribute;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.customtrader.villager.VillagerEnumMapper;
import de.juyas.utils.api.command.TabCompletion;
import de.juyas.utils.api.hud.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.*;

/**
 * @author Juyas
 * @version 27.11.2023
 * @since 27.11.2023
 */
@SuppressWarnings({ "UnstableApiUsage" })
public class ChangeType extends AbstractSelectionCommand
{

    private final HashMap<UUID, Villager.Type> changeType;

    public ChangeType()
    {
        super( "type" );
        setSignature( "type" );
        setMinArgs( 1 );
        setDescription( "Ändere den Biom-Typ eines Händlers" );
        changeType = new HashMap<>();
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        Villager.Type type = VillagerEnumMapper.mapType( args[0] );
        putInQueue( player );
        changeType.put( player.getUniqueId(), type );
        Chat.send( player, "§aKlicke einen Händler an, um seinen Biom-Typ zu §9" + VillagerEnumMapper.map( type ) + " §azu ändern." );
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        info.trader().setAttribute( TraderAttribute.VILLAGER_TYPE, changeType.get( player.getUniqueId() ) );
        changeType.remove( player.getUniqueId() );
        pullFromQueue( player );
        info.respawn();
        Chat.send( player, "§aDer Händler ist nun vom Biom-Typ §9" + VillagerEnumMapper.map( info.trader().getAttribute( TraderAttribute.VILLAGER_TYPE ) ) );
        return true;
    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        return args.length == 1 ? () -> Arrays.stream( VillagerEnumMapper.typeValues() )
                .map( VillagerEnumMapper::map ).toList() : TabCompletion.NONE;
    }

}