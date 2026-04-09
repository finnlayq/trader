package de.juyas.customtrader.command.item;

import de.juyas.utils.api.command.ArgumentRange;
import de.juyas.utils.api.command.PlayerCommand;
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
public final class EmptyLine extends PlayerCommand
{

    public EmptyLine()
    {
        super( "emptyLine" );
        setArgumentRange( ArgumentRange.fix( 0 ) );
        setDescription( "Adds an empty line to the lore of the item." );
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
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
            lore.add( Component.empty() );
            meta.lore( lore );
        } );
        if ( edited ) Chat.send( player, "§cLore konnte nicht geändert werden." );
    }

}