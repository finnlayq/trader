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
        if (getCommand("addtrade") != null) getCommand("addtrade").setExecutor(new AddTrade());
        if (getCommand("addrawtrade") != null) getCommand("addrawtrade").setExecutor(new AddRawTrade());
        if (getCommand("adjusttrader") != null) getCommand("adjusttrader").setExecutor(new AdjustTrader());
        if (getCommand("changecooldown") != null) getCommand("changecooldown").setExecutor(new ChangeCooldown());
        if (getCommand("changename") != null) getCommand("changename").setExecutor(new ChangeName());

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