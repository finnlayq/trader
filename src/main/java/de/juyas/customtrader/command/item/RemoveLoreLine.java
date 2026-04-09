package de.juyas.customtrader.command.item;

import de.juyas.utils.api.command.*;
import de.juyas.utils.api.hud.Chat;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

/**
 * @author Juyas
 * @version 23.01.2025
 * @since 23.01.2025
 */
public final class RemoveLoreLine extends PlayerCommand
{

    public RemoveLoreLine()
    {
        super( "remlore" );
        setArgumentRange( ArgumentRange.fix( 1 ) );
        setDescription( "Removes a line from the lore of the item." );
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        Integer index = Arguments.numInt( player, args[0] );
        if ( index == null ) return;
        PlayerInventory inventory = player.getInventory();
        ItemStack mainHand = inventory.getItemInMainHand();
        if ( mainHand.isEmpty() )
        {
            Chat.send( player, "§cKein Item in der Hand." );
            return;
        }
        boolean edited = !mainHand.editMeta( meta ->
        {
            List<Component> lore = new ArrayList<>( meta.hasLore() ? Objects.requireNonNull( meta.lore() ) : Collections.emptyList() );
            int i = index;
            lore.remove( i );
            meta.lore( lore );
        } );
        if ( edited ) Chat.send( player, "§cLore konnte nicht geändert werden." );
    }

}