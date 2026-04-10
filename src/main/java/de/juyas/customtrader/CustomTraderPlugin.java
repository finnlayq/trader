package de.juyas.customtrader;

import de.juyas.customtrader.command.*;
import de.juyas.customtrader.listener.DeletionListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomTraderPlugin extends JavaPlugin {

    private static CustomTraderPlugin instance;
    private TraderManager manager;

    public static CustomTraderPlugin getInstance() {
        return instance;
    }

    public TraderManager getManager() {
        return manager;
    }

    @Override
    public void onEnable() {
        instance = this;
        manager = new TraderManager();

        // Commands registrieren
        if (getCommand("createtrader") != null) getCommand("createtrader").setExecutor(new CreateTrader());
        if (getCommand("removetrader") != null) getCommand("removetrader").setExecutor(new RemoveTrader());
        if (getCommand("addtrade") != null) getCommand("addtrade").setExecutor(new AddTrade());
        if (getCommand("addrawtrade") != null) getCommand("addrawtrade").setExecutor(new AddRawTrade());
        if (getCommand("deletetrade") != null) getCommand("deletetrade").setExecutor(new DeleteTrade());
        if (getCommand("defaulttrades") != null) getCommand("defaulttrades").setExecutor(new DefaultTrades());
        if (getCommand("adjusttrader") != null) getCommand("adjusttrader").setExecutor(new AdjustTrader());
        if (getCommand("changecooldown") != null) getCommand("changecooldown").setExecutor(new ChangeCooldown());
        if (getCommand("changename") != null) getCommand("changename").setExecutor(new ChangeName());
        if (getCommand("changeprofession") != null) getCommand("changeprofession").setExecutor(new ChangeProfession());
        if (getCommand("changeskin") != null) getCommand("changeskin").setExecutor(new ChangeSkin());
        if (getCommand("changetype") != null) getCommand("changetype").setExecutor(new ChangeType());
        if (getCommand("movetrader") != null) getCommand("movetrader").setExecutor(new MoveTrader());
        if (getCommand("showinfo") != null) getCommand("showinfo").setExecutor(new ShowInfo());
        if (getCommand("toggleanimation") != null) getCommand("toggleanimation").setExecutor(new ToggleAnimation());
        if (getCommand("reloadtrader") != null) getCommand("reloadtrader").setExecutor(new ReloadTrader());

        // NEU: Spawnen und Wipen
        if (getCommand("spawnall") != null) getCommand("spawnall").setExecutor(new SpawnAll());
        if (getCommand("wipeall") != null) getCommand("wipeall").setExecutor(new WipeAll());

        // Listener registrieren
        Bukkit.getPluginManager().registerEvents(new DeletionListener(), this);

        manager.spawn();
    }

    @Override
    public void onDisable() {
        if (manager != null) {
            manager.save();
            manager.despawnAll();
        }
    }
}