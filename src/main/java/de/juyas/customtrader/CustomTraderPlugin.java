package de.juyas.customtrader;

import de.juyas.customtrader.api.Presets;
import de.juyas.customtrader.command.*;
import de.juyas.customtrader.listener.DeletionListener;
import de.juyas.customtrader.listener.TraderDamageListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomTraderPlugin extends JavaPlugin {

    private static CustomTraderPlugin instance;
    private TraderManager manager;

    public static CustomTraderPlugin getInstance() { return instance; }
    public TraderManager getManager() { return manager; }

    @Override
    public void onEnable() {
        instance = this;
        manager = new TraderManager();

        // Dynamische Presets laden
        Presets.load();

        registerCommands();

        // Listener registrieren
        Bukkit.getPluginManager().registerEvents(new DeletionListener(), this);
        Bukkit.getPluginManager().registerEvents(new TraderDamageListener(), this); // NEU: Macht Trader unsterblich

        manager.spawn();
    }

    private void registerCommands() {
        AddRawTrade art = new AddRawTrade();
        registerCommand("addrawtrade", art, art);

        ApplyPreset ap = new ApplyPreset();
        registerCommand("applypreset", ap, ap);

        ChangeType ct = new ChangeType();
        registerCommand("changetype", ct, ct);

        ChangeProfession cp = new ChangeProfession();
        registerCommand("changeprofession", cp, cp);

        ChangePrice cpPrice = new ChangePrice();
        registerCommand("changeprice", cpPrice, cpPrice);

        registerCommand("createtrader", new CreateTrader(), null);
        registerCommand("removetrader", new RemoveTrader(), null);
        registerCommand("toggleanimation", new ToggleAnimation(), null);
        registerCommand("spawnall", new SpawnAll(), null);
        registerCommand("wipeall", new WipeAll(), null);
        registerCommand("showinfo", new ShowInfo(), null);
        registerCommand("changename", new ChangeName(), null);
        registerCommand("reloadtrader", new ReloadTrader(), null);

        MoveTrader moveCmd = new MoveTrader();
        registerCommand("movetrader", moveCmd, moveCmd);
    }

    private void registerCommand(String name, org.bukkit.command.CommandExecutor ex, org.bukkit.command.TabCompleter tab) {
        var cmd = getCommand(name);
        if (cmd != null) {
            cmd.setExecutor(ex);
            if (tab != null) cmd.setTabCompleter(tab);
        }
    }

    @Override
    public void onDisable() {
        if (manager != null) {
            manager.save();
            manager.despawnAll();
        }
    }
}