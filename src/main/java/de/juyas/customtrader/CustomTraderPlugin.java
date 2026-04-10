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
        registerCommands();

        // Listener registrieren
        Bukkit.getPluginManager().registerEvents(new DeletionListener(), this);

        // Trader beim Start spawnen
        manager.spawn();
    }

    private void registerCommands() {
        // --- ADD RAW TRADE (mit Tab) ---
        AddRawTrade addRawTrade = new AddRawTrade();
        registerCommand("addrawtrade", addRawTrade, addRawTrade);

        // --- APPLY PRESET (mit Tab) ---
        ApplyPreset applyPreset = new ApplyPreset();
        registerCommand("applypreset", applyPreset, applyPreset);

        // --- CHANGE TYPE (mit Tab) ---
        ChangeType changeType = new ChangeType();
        registerCommand("changetype", changeType, changeType);

        // --- CHANGE PROFESSION (mit Tab) ---
        ChangeProfession changeProfession = new ChangeProfession();
        registerCommand("changeprofession", changeProfession, changeProfession);

        // --- Standard Commands (ohne speziellen Tab-Completer) ---
        registerCommand("createtrader", new CreateTrader(), null);
        registerCommand("removetrader", new RemoveTrader(), null);
        registerCommand("addtrade", new AddTrade(), null);
        registerCommand("deletetrade", new DeleteTrade(), null);
        registerCommand("defaulttrades", new DefaultTrades(), null);
        registerCommand("adjusttrader", new AdjustTrader(), null);
        registerCommand("changecooldown", new ChangeCooldown(), null);
        registerCommand("changename", new ChangeName(), null);
        registerCommand("changeskin", new ChangeSkin(), null);
        registerCommand("movetrader", new MoveTrader(), null);
        registerCommand("showinfo", new ShowInfo(), null);
        registerCommand("toggleanimation", new ToggleAnimation(), null);
        registerCommand("reloadtrader", new ReloadTrader(), null);
        registerCommand("spawnall", new SpawnAll(), null);
        registerCommand("wipeall", new WipeAll(), null);
    }

    /**
     * Hilfsmethode um Commands sauber mit Executor und TabCompleter zu registrieren
     */
    private void registerCommand(String name, org.bukkit.command.CommandExecutor executor, org.bukkit.command.TabCompleter completer) {
        var cmd = getCommand(name);
        if (cmd != null) {
            cmd.setExecutor(executor);
            if (completer != null) {
                cmd.setTabCompleter(completer);
            }
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