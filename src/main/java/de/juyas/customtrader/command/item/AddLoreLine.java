package de.juyas.customtrader.command.item;

import de.juyas.utils.api.BukkitLegacy;
import de.juyas.utils.api.command.ArgumentRange;
import de.juyas.utils.api.command.PlayerCommand;
import de.juyas.utils.api.hud.Chat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

/**
 * @author Juyas
 * @version 23.01.2025
 * @since 23.01.2025
 */
public final class AddLoreLine extends PlayerCommand
{

    public AddLoreLine()
    {
        super( "lore" );
        setArgumentRange( ArgumentRange.openMax( 1 ) );
        setDescription( "Adds a line to the lore of the item." );
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        String name = String.join( " ", args );
        Component textComponent = BukkitLegacy.legacyText( name );
        if ( !textComponent.hasDecoration( TextDecoration.ITALIC ) )
            textComponent = textComponent.decoration( TextDecoration.ITALIC, false );
        PlayerInventory inventory = player.getInventory();
        ItemStack mainHand = inventory.getItemInMainHand();
        if ( mainHand.isEmpty() )
        {
            Chat.send( player, "§cKein Item in der Hand." );
            return;
        }
        Component finalTextComponent = textComponent;
        boolean edited = !mainHand.editMeta( meta ->
        {
            List<Component> lore = new ArrayList<>( meta.hasLore() ? Objects.requireNonNull( meta.lore() ) : Collections.emptyList() );
            lore.add( finalTextComponent );
            meta.lore( lore );
        } );
        if ( edited ) Chat.send( player, "§cLore konnte nicht geändert werden." );
    }

}