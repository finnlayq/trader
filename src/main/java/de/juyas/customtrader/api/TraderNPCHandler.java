package de.juyas.customtrader.api;

import de.juyas.customtrader.model.TraderEntry;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;
import java.util.ArrayList;
import java.util.List;

public class TraderNPCHandler {

    private final TraderEntry trader;
    private final TraderResetTimer resetTimer;
    private final List<MerchantRecipe> recipes = new ArrayList<>();

    private NPC npc;
    private Entity entity;

    public TraderNPCHandler(TraderEntry trader) {
        this.trader = trader;
        this.resetTimer = new TraderResetTimer(this);
    }

    public TraderEntry trader() {
        return trader;
    }

    public void spawn() {
        Location loc = trader.getLocation();
        if (loc == null || loc.getWorld() == null) return;

        if (trader.isNpc()) {
            if (npc == null) {
                npc = CitizensAPI.getNPCRegistry().createNPC(trader.getType(), trader.getName());
            }
            npc.spawn(loc);
            this.entity = npc.getEntity();
        } else {
            if (entity == null || !entity.isValid()) {
                entity = loc.getWorld().spawnEntity(loc, trader.getType());
            }
            this.entity = entity;
        }

        updateName();
        applyAttributes();
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
        Entity current = (npc != null && npc.isSpawned()) ? npc.getEntity() : entity;
        if (current instanceof Villager villager) {
            villager.setProfession(trader.getAttribute(TraderAttribute.VILLAGER_PROFESSION));
            villager.setVillagerType(trader.getAttribute(TraderAttribute.VILLAGER_TYPE));
        }
    }

    // --- Diese Methoden braucht der TraderResetTimer ---

    public void resetOffers() {
        // Hier werden die Trades aktualisiert
        applyAttributes();
    }

    public void playAnimation() {
        // Platzhalter für Animationen beim Reset (z.B. Partikel)
    }

    public NPC getNpc() { return npc; }
    public Entity getEntity() { return entity; }
}