package de.juyas.customtrader.api;

import de.juyas.customtrader.model.TraderEntry;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class TraderNPCHandler {

    private final TraderEntry trader;
    private final TraderResetTimer resetTimer;
    private final List<MerchantRecipe> recipes = new ArrayList<>();

    private NPC npc; // Falls Citizens genutzt wird
    private Entity entity; // Falls ein normaler Villager genutzt wird

    public TraderNPCHandler(TraderEntry trader) {
        this.trader = trader;
        this.resetTimer = new TraderResetTimer(this);
    }

    public TraderEntry trader() {
        return trader;
    }

    public TraderResetTimer getResetTimer() {
        return resetTimer;
    }

    public void spawn() {
        Location loc = trader.getLocation();
        if (loc == null) return;

        if (trader.isNpc()) {
            // Citizens NPC Logik
            if (npc == null) {
                npc = CitizensAPI.getNPCRegistry().createNPC(trader.getType(), trader.getName());
            }
            npc.spawn(loc);
            this.entity = npc.getEntity();
        } else {
            // Standard Bukkit Entity Logik
            if (entity == null || !entity.isValid()) {
                entity = loc.getWorld().spawnEntity(loc, trader.getType());
            }
            this.entity = entity;
        }

        updateName();
        applyAttributes();
        resetTimer.start();
    }

    public void despawn() {
        resetTimer.stop();
        if (npc != null) {
            npc.despawn();
        } else if (entity != null) {
            entity.remove();
        }
    }

    public void respawn() {
        despawn();
        spawn();
    }

    public void updateName() {
        String displayName = trader.getName().replace("&", "§");
        if (npc != null) {
            npc.setName(displayName);
        } else if (entity != null) {
            entity.setCustomName(displayName);
            entity.setCustomNameVisible(true);
        }
    }

    public void applyAttributes() {
        Entity current = getNpc() != null ? getNpc().getEntity() : entity;
        if (current instanceof Villager villager) {
            villager.setProfession(trader.getAttribute(TraderAttribute.VILLAGER_PROFESSION));
            villager.setVillagerType(trader.getAttribute(TraderAttribute.VILLAGER_TYPE));
            villager.setRecipes(recipes);
        }
    }

    public void addRecipe(MerchantRecipe recipe) {
        this.recipes.add(recipe);
        applyAttributes(); // Sofort anwenden
    }

    public void resetOffers() {
        // Logik zum Zurücksetzen der Trades (kann erweitert werden)
        applyAttributes();
    }

    public void playAnimation() {
        // Hier könnte eine Partikel-Animation beim Reset kommen
    }

    public NPC getNpc() {
        return npc;
    }

    public Entity getEntity() {
        return entity;
    }
}