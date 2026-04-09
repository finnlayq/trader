package de.juyas.customtrader.command;

import de.juyas.customtrader.api.TraderAttribute;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.utils.api.command.Arguments;
import de.juyas.utils.api.hud.Chat;
import net.citizensnpcs.util.MojangSkinGenerator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Juyas
 * @version 27.11.2023
 * @since 27.11.2023
 */
public class ChangeSkin extends AbstractSelectionCommand
{

    private final HashMap<UUID, String[]> skinUpdates;

    public ChangeSkin()
    {
        super( "skin" );
        setSignature( "url", "slim?" );
        setMinArgs( 1 );
        setDescription( "Lädt einen Skin von URL und weist einem Händler diesen zu." );
        skinUpdates = new HashMap<>();
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        try
        {
            JSONObject object = MojangSkinGenerator.generateFromURL( args[0], Arguments.optBool( player, args, 1 ).orElse( false ) );
            //String uuid = (String) object.get( "uuid" );
            JSONObject texture = (JSONObject) object.get( "texture" );
            String signature = (String) texture.get( "signature" );
            String data = (String) texture.get( "value" );
            this.skinUpdates.put( player.getUniqueId(), new String[] { signature, data } );
        }
        catch ( Exception e )
        {
            player.sendMessage( "§cDer Link funktioniert nicht: \"§7" + args[0] + "§c\"" );
            return;
        }
        putInQueue( player );
        Chat.send( player, "§aKlicke einen Händler an, damit er seine Kleidung ändert." );
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        info.trader().setAttribute( TraderAttribute.SKIN, skinUpdates.get( player.getUniqueId() ) );
        skinUpdates.remove( player.getUniqueId() );
        pullFromQueue( player );
        info.respawn();
        Chat.send( player, "§aDer Händler hat nun andere Kleidung an." );
        return true;
    }

}