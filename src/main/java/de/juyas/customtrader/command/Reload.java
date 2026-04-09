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
public class Reload extends PlayerCommand
{

    public Reload()
    {
        super( "reload", "rl" );
        setSignature();
        setDescription( "Lädt das Plugin neu - Beinhaltet das neu-spawnen aller Händler." );
    }

    @Override
    public void onPlayerCommand( Player player, String[] strings )
    {
        CustomTraderPlugin.getInstance().reload();
        Chat.send( player, "§aAlle Funktionen wurden neu geladen." );
    }

}