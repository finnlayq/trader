package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.utils.api.command.PlayerCommand;
import de.juyas.utils.api.hud.Chat;
import org.bukkit.entity.Player;

/**
 * @author Juyas
 * @version 26.11.2023
 * @since 26.11.2023
 */
public class SpawnAll extends PlayerCommand
{

    public SpawnAll()
    {
        super( "spawnAll", "spawnEntities" );
        setSignature();
        setDescription( "Erschafft neue Entities für alle bekannten Händler, die keine aktiven Entities haben" );
    }

    @Override
    public void onPlayerCommand( Player player, String[] strings )
    {
        CustomTraderPlugin.getInstance().getManager().spawn();
        Chat.send( player, "§aAlle Händler wurden aus ihrer Pause geholt. Bereits Anwesende wurden §nnicht§r§a ersetzt." );
    }

}