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
@SuppressWarnings({ "deprecation", "UnstableApiUsage" })
public class ChangeProfession extends AbstractSelectionCommand
{

    private final HashMap<UUID, Villager.Profession> changeProfession;

    public ChangeProfession()
    {
        super( "profession", "job" );
        setSignature( "profession" );
        setMinArgs( 1 );
        setDescription( "Ändere den Beruf eines Händlers" );
        changeProfession = new HashMap<>();
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        Villager.Profession profession = VillagerEnumMapper.mapProfession( args[0] );
        if ( profession == null )
        {
            Chat.send( player, "§cUnbekannter Beruf. (" + args[0] + ")" );
            return;
        }
        putInQueue( player );
        changeProfession.put( player.getUniqueId(), profession );
        Chat.send( player, "§aKlicke einen Händler an, um seinen Beruf zu §9" + VillagerEnumMapper.map( profession ) + " §azu ändern." );
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        info.trader().setAttribute( TraderAttribute.VILLAGER_PROFESSION, changeProfession.get( player.getUniqueId() ) );
        changeProfession.remove( player.getUniqueId() );
        pullFromQueue( player );
        info.respawn();
        Chat.send( player, "§aDer Händler ist nun §9" + VillagerEnumMapper.map( info.trader().getAttribute( TraderAttribute.VILLAGER_PROFESSION ) ) );
        return true;
    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        return args.length == 1 ? () -> Arrays.stream( VillagerEnumMapper.professionValues() )
                .map( VillagerEnumMapper::map ).toList() : TabCompletion.NONE;
    }

}