package de.juyas.customtrader.citizens;

import de.juyas.customtrader.api.Trader;
import de.juyas.customtrader.model.TradeOfferEntry;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.inventory.Merchant;
import java.util.stream.Collectors;

public class TraderTrait extends Trait {

    private Trader traderData;

    public TraderTrait() {
        super("customtrader");
    }

    public void updateMerchant(Merchant merchant) {
        if (traderData == null) return;

        // Nutzt die korrekten Getter getOffers() und getRecipe()
        merchant.setRecipes(traderData.getOffers().stream()
                .map(TradeOfferEntry::getRecipe)
                .collect(Collectors.toList()));
    }
}