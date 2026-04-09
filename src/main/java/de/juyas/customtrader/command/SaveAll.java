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
public class SaveAll extends PlayerCommand
{

    public SaveAll()
    {
        super( "save", "saveTraders" );
        setSignature();
        setDescription( "Speichert alle Änderungen an den Händlern" );
    }

    @Override
    public void onPlayerCommand( Player player, String[] strings )
    {
        Chat.send( player, "§aAlle Trader werden gespeichert." );
        CustomTraderPlugin.getInstance().getManager().save();
    }

}