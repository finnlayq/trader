package de.juyas.customtrader.command;

import de.juyas.utils.api.command.Arguments;
import de.juyas.utils.api.command.TabCompletion;
import de.juyas.utils.api.hud.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.HashSet;
import java.util.UUID;

/**
 * @author Juyas
 * @version 26.11.2023
 * @since 26.11.2023
 */
public class CreateTrader extends AbstractSelectionCommand
{

    private final HashSet<UUID> npcRegistration = new HashSet<>();

    public CreateTrader()
    {
        super( "create", "new" );
        setSignature( "citizens?" );
        setMinArgs( 0 );
        setDescription( "Erstellt einen neuen Händler auf Basis eines vorhandenen Dorfbewohners.",
                "Optional kann sich für ein Citizens NPC entschieden werden." );
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        if ( inQueue( player ) )
        {
            player.sendMessage( "§cDu musst schon den Händler anklicken..." );
            return;
        }
        putInQueue( player );
        if ( Arguments.optBool( player, args, 0 ).orElse( false ) )
            npcRegistration.add( player.getUniqueId() );
        Chat.send( player, "§aKlicke nun den Händler an, der registriert werden soll." );
    }

    @Override
    public boolean onInteractUnknownEntity( Player player, Entity entity )
    {
        if ( entity instanceof Villager villager )
        {
            boolean asNPC = npcRegistration.contains( player.getUniqueId() );
            manager().registerNew( villager, asNPC );
            npcRegistration.remove( player.getUniqueId() );
            pullFromQueue( player );
            Chat.send( player, "§aDer neue Händler steht nun im Handelsregister. Citizens bevorzugt: " + asNPC );
            return true;
        }
        return false;
    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        return args.length == 1 ? TabCompletion.BOOLEAN : TabCompletion.NONE;
    }
}