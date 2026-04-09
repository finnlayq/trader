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

/**
 * @author Juyas
 * @version 23.01.2025
 * @since 23.01.2025
 */
public final class ChangeItemName extends PlayerCommand
{

    public ChangeItemName()
    {
        super( "name" );
        setArgumentRange( ArgumentRange.openMax( 1 ) );
        setDescription( "Changes the name of the item in hand." );
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
        if ( !mainHand.editMeta( meta -> meta.displayName( finalTextComponent ) ) )
        {
            Chat.send( player, "§cName konnte nicht geändert werden." );
        }
    }

}