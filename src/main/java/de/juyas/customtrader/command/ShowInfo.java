package de.juyas.customtrader.command;

import de.juyas.customtrader.api.*;
import de.juyas.utils.api.hud.Chat;
import de.juyas.utils.api.hud.FancyTextColor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @author Juyas
 * @version 27.11.2023
 * @since 27.11.2023
 */
public class ShowInfo extends AbstractSelectionCommand
{

    public ShowInfo()
    {
        super( "info" );
        setSignature();
        setDescription( "Zeige eine Info zu einem Händler" );
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        putInQueue( player );
        Chat.send( player, "§aKlicke einen Händler an, um eine Info anzuzeigen." );
    }

    @Override
    public boolean onInteractKnownTrader( Player player, Entity entity, TraderNPCHandler info )
    {
        pullFromQueue( player );
        String prof = info.trader().getAttribute( TraderAttribute.VILLAGER_PROFESSION ).key().value();
        String type = info.trader().getAttribute( TraderAttribute.VILLAGER_TYPE ).key().value();
        Chat.send( player, "§7==> §9" + prof + " §a- Händler aus dem §9" + type + " §aBiom" );
        Chat.send( player, "§7==> §aEr hat §9" + info.trader().offers().size() + " §aTauschgeschäfte, die er alle §9" + info.trader().getAttribute( TraderAttribute.REFRESH_SECONDS ) + " §aSekunden auffrischt." );
        info.trader().offers().stream().map( this::tradeToText ).forEach( player::sendMessage );
        return true;
    }

    private Component tradeToText( TradeOffer offer )
    {
        Component discount = Component.text( "discount" ).color( offer.ignoreDiscounts() ? FancyTextColor.RED : FancyTextColor.LIGHT_GREEN );
        Component exp = Component.text( "experience" ).color( offer.experienceReward() ? FancyTextColor.LIGHT_GREEN : FancyTextColor.RED );
        Component info = Component.text( " | " + offer.tradeCapacity() ).append( Component.text( ", " ) )
                .append( discount ).append( Component.text( ", " ) ).append( exp );
        if ( offer.ingredient2().isEmpty() )
        {
            Component ing1 = offer.ingredient1().displayName();
            Component result = offer.result().displayName();
            return ing1.append( Component.text( " => " ).color( FancyTextColor.BLUEISH_GRAY_WHITE ) ).append( result )
                    .appendNewline().append( info );
        }
        else
        {
            Component ing1 = offer.ingredient1().displayName();
            Component ing2 = offer.ingredient2().displayName();
            Component result = offer.result().displayName();
            return ing1.append( Component.text( " + " ).color( FancyTextColor.BLUEISH_GRAY_WHITE ) ).append( ing2 )
                    .append( Component.text( " => " ).color( FancyTextColor.BLUEISH_GRAY_WHITE ) ).append( result )
                    .appendNewline().append( info );
        }
    }

}