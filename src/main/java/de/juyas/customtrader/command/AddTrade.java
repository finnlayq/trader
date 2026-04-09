package de.juyas.customtrader.command;

import de.juyas.customtrader.CustomTraderPlugin;
import de.juyas.customtrader.api.TradeOfferData;
import de.juyas.customtrader.api.TraderNPCHandler;
import de.juyas.utils.api.command.Arguments;
import de.juyas.utils.api.command.TabCompletion;
import de.juyas.utils.api.hud.Chat;
import de.juyas.utils.api.hud.FancyTextColor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Juyas
 * @version 23.11.2024
 * @since 23.11.2024
 */
public class AddTrade extends AbstractSelectionCommand
{

    private final HashMap<UUID, TradeOfferData> newTrades;

    public AddTrade()
    {
        super( "addTrade" );
        setSignature( "capacity", "experience?", "discount?", "order" );
        setMinArgs( 1 );
        setDescription( "Füge einem Händler einen komplexeren Tauschhandel hinzu.",
                "Die Kapazität beschreibt die Menge der Handel pro Reset.",
                "Ob Erfahrungspunkte erhalten können werden. Default: true",
                "Und ob der Tauschhandel den Helden-Des-Dorfes Rabatt unterstützt. Default: true",
                "Die Order bestimmt die Reihenfolge in der Handelsliste. Default: 0" );
        this.newTrades = new HashMap<>();
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        Integer capacity = Arguments.validateRange( player, args[0], Integer::parseInt, 1, 1000 );
        if ( capacity == null ) return;
        boolean experience = Arguments.optBool( player, args, 1 ).orElse( true );
        boolean discount = Arguments.optBool( player, args, 2 ).orElse( true );
        int order = Arguments.optInteger( player, args, 3 ).orElse( 0 );
        List<ItemStack> results = new ArrayList<>();
        requestItems( player, Component.text( "Abbruch bei ungültiger Eingabe." ).color( FancyTextColor.BLUEISH_GRAY ), result ->
        {
            if ( result.isEmpty() ) return;
            results.add( result.get() );
        }, () -> {
            if ( results.isEmpty() || results.size() < 2 || results.size() > 3 )
            {
                Chat.send( player, "§cAbgebrochen." );
            }
            else
            {
                ItemStack first = results.get( 0 );
                ItemStack second = results.get( 1 );
                ItemStack third = results.size() == 3 ? results.get( 2 ) : null;
                TradeOfferData data = new TradeOfferData( first, third == null ? null : second, third == null ? second : third, experience, !discount, order, capacity );
                putInQueue( player );
                newTrades.put( player.getUniqueId(), data );
                Chat.send( player, "§aKlicke einen Händler an, um ihm den neuen Handel hinzuzufügen." );
            }
        } );
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        TradeOfferData trade = newTrades.remove( player.getUniqueId() );
        info.trader().addOffer( trade );
        info.resetOffers();
        pullFromQueue( player );
        Chat.send( player, "§aDer Händler hat sich dazu entschieden neue Waren anzubieten." );
        return true;
    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        return switch ( args.length )
        {
            case 2, 3 -> TabCompletion.BOOLEAN;
            default -> TabCompletion.NONE;
        };
    }

    private void requestItems( Player player, Component title, Consumer<Optional<ItemStack>> result, Runnable done )
    {
        Inventory inventory = Bukkit.createInventory( player, InventoryType.HOPPER, title );
        deployListener( inventory, () -> {
            List<ItemStack> items = Arrays.stream( inventory.getContents() )
                    .map( item -> item != null && item.isEmpty() ? null : item ).toList();
            if ( items.isEmpty() )
                result.accept( Optional.empty() );
            else items.forEach( item -> result.accept( Optional.ofNullable( item ) ) );
            done.run();
        } );
        player.openInventory( inventory );
    }

    private void deployListener( Inventory inventory, Runnable onClose )
    {
        Listener listener = new Listener()
        {
            @EventHandler
            public void onClose( InventoryCloseEvent event )
            {
                if ( event.getInventory().equals( inventory ) )
                {
                    onClose.run();
                    HandlerList.unregisterAll( this );
                }
            }
        };
        Bukkit.getPluginManager().registerEvents( listener, CustomTraderPlugin.getInstance() );
    }

}