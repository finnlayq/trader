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
public class WipeAll extends PlayerCommand
{

    public WipeAll()
    {
        super( "wipe", "killall" );
        setSignature();
        setDescription( "Tötet alle aktuell aktiven Händler-Entities" );
    }

    @Override
    public void onPlayerCommand( Player player, String[] strings )
    {
        CustomTraderPlugin.getInstance().getManager().wipe();
        Chat.send( player, "§aAlle bekannten Händler wurden sorgfältigst beseitigt." );
    }

}