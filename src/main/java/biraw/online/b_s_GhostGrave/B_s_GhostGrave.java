package biraw.online.b_s_GhostGrave;

import biraw.online.b_s_GhostGrave.Listeners.EntityLoadedListener;
import biraw.online.b_s_GhostGrave.Listeners.EntityUnloadedListener;
import biraw.online.b_s_GhostGrave.Listeners.PlayerDeathListener;
import biraw.online.b_s_GhostGrave.Listeners.PlayerInteractEntityListener;
import biraw.online.b_s_GhostGrave.Managers.ConfigManager;
import biraw.online.b_s_GhostGrave.Managers.TaskManager;
import biraw.online.b_s_GhostGrave.Managers.LoadManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public final class B_s_GhostGrave extends JavaPlugin {
    // Register main instances of classes
    private static B_s_GhostGrave plugin;
    private static TaskManager taskManager;
    private static LoadManager loadManager;
    private static ConfigManager configManager;

    // Easy method for getting a NamespacedKey
    public NamespacedKey GetNamespaceKey(String string){
        if (string == null || string.isEmpty()) return null;
        return new NamespacedKey(plugin,string);
    }

    // Getters for the main instances of classes
    public static TaskManager getTaskManager(){
        return taskManager;
    }
    public static B_s_GhostGrave getPlugin() {
        return plugin;
    }
    public static LoadManager getLoadManager()
    {
        return loadManager;
    }
    public static ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public void onEnable() {
        // Register cont variables
        plugin = this;
        taskManager = new TaskManager();
        loadManager = new LoadManager();
        configManager = new ConfigManager();

        // Register events
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(),plugin);
        Bukkit.getPluginManager().registerEvents(new EntityLoadedListener(),plugin);
        Bukkit.getPluginManager().registerEvents(new EntityUnloadedListener(),plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractEntityListener(),plugin);

        // Enable ghosts that are loaded within the spawn chunks
        plugin.getLogger().info("Enabling ghosts...");
        Bukkit.getScheduler().cancelTasks(B_s_GhostGrave.getPlugin());
        NamespacedKey key = plugin.GetNamespaceKey("is_ghost");
        for (World world : Bukkit.getWorlds()){
            for (Entity entity : world.getEntities())
            {
                if(entity.getType() == EntityType.BEE){
                    if (entity.getPersistentDataContainer().has(key))
                    {
                        loadManager.LoadGhost((Bee) entity);
                    }
                }
            }
        }
        plugin.getLogger().info("Every ghost is enabled.");

        // Print the motd
        plugin.getLogger().info(" ");
        plugin.getLogger().info("O=========================================================0");
        plugin.getLogger().info("     The B's GhostGrave plugin has loaded successfully");
        plugin.getLogger().info("  This is B's GhostGrave for Minecraft 1.20.x and 1.21.x!");
        plugin.getLogger().info("                       Author: BiRaw");
        plugin.getLogger().info("         Discord: https://discord.gg/XwFqu7uahX :>");
        plugin.getLogger().info("O=========================================================0");
        plugin.getLogger().info(" ");
    }

    @Override
    public void onDisable() {
        // Disable the task of any ghost that are still loaded to avoid tasks collisions
        plugin.getLogger().info("Disabling ghosts...");
        Bukkit.getScheduler().cancelTasks(B_s_GhostGrave.getPlugin());
        NamespacedKey key = plugin.GetNamespaceKey("is_ghost");
        for (World world : Bukkit.getWorlds()){
            for (Entity entity : world.getEntities())
            {
                if(entity.getType() == EntityType.BEE){
                    if (entity.getPersistentDataContainer().has(key))
                    {
                        loadManager.UnloadGhost((Bee) entity);
                    }
                }
            }
        }
        plugin.getLogger().info("Every ghost is disabled.");
        plugin.getLogger().info("The B's GhostGrave plugin is unloaded.");
    }
}
