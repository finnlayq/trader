package de.juyas.customtrader.villager;

import de.juyas.customtrader.api.TraderAttribute;
import de.juyas.customtrader.model.TraderEntry;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;

import java.util.List;
import java.util.stream.Collectors;

public class VillagerHandler {

    private final TraderEntry traderEntry;
    private final Villager villager;

    public VillagerHandler(TraderEntry traderEntry, Villager villager) {
        this.traderEntry = traderEntry;
        this.villager = villager;
    }

    /**
     * Wendet alle gespeicherten Attribute und Trades auf den Villager in der Welt an.
     */
    public void apply() {
        if (villager == null || !villager.isValid()) return;

        // Namen setzen
        if (traderEntry.getName() != null) {
            villager.setCustomName(traderEntry.getName().replace("&", "§"));
            villager.setCustomNameVisible(true);
        }

        // Berufe und Typen setzen (falls im Modell vorhanden)
        Villager.Profession profession = traderEntry.getAttribute(TraderAttribute.VILLAGER_PROFESSION);
        if (profession != null) villager.setProfession(profession);

        Villager.Type type = traderEntry.getAttribute(TraderAttribute.VILLAGER_TYPE);
        if (type != null) villager.setVillagerType(type);

        // KI deaktivieren, damit der Händler stehen bleibt
        villager.setAI(false);

        // Trades laden und setzen
        updateTrades();
    }

    /**
     * Aktualisiert nur die Handelsangebote des Villagers.
     */
    public void updateTrades() {
        List<MerchantRecipe> recipes = traderEntry.getOffers().stream()
                .map(offer -> offer.getRecipe())
                .collect(java.util.stream.Collectors.toList());

        villager.setRecipes(recipes);
    }

    public TraderEntry getEntry() {
        return traderEntry;
    }

    public Villager getVillager() {
        return villager;
    }
}