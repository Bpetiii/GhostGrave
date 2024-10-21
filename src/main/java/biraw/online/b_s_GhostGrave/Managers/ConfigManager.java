package biraw.online.b_s_GhostGrave.Managers;

import biraw.online.b_s_GhostGrave.B_s_GhostGrave;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigManager {
    private File dataFolder = B_s_GhostGrave.getPlugin().getDataFolder();

    private final File configFile;

    public int particleCount = 10;
    public int experienceCount = 100;
    public boolean noItemSpawns = false;
    public boolean vanillaExperience = false;
    public boolean canAllPlayerOpen = false;

    public ConfigManager(){

        // Ensure that the dataFolder exists
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        configFile = new File(B_s_GhostGrave.getPlugin().getDataFolder(), "B's GhostGrave Config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

                // Default values
                config.set("particle_count", 10);
                config.setComments("particle_count",
                        List.of(" ",
                                "The number of particles that will be displayed.",
                                "Not representative of the actual number of particles, this is rather a multiplier.")
                );

                config.set("no-item_spawns", false);
                config.setComments("no-item_spawns",
                        List.of(" ",
                                "If set true, ghosts will spawn even if the player didn't have any item to save.",
                                "This can be good if you want to save the experience, even if the player didn't have any items.")
                );

                config.set("vanilla_experience", false);
                config.setComments("vanilla_experience",
                        List.of(" ",
                                "If set true, the returned amount of experience will resemble the vanilla game.",
                                "This means a maximum of 100 experience points or about 7 levels.",
                                "Also if true 'experience_count' will have no effect.")
                );

                config.set("experience_count", 100);
                config.setComments("experience_count",
                        List.of(" ",
                                "The percentage of experience that the player will get back from the ghost.",
                                "Setting this to more than '100' will result in an infinite experience glitch.",
                                "On this SPIGOT EDITION it's advised to keep this on 0, because this version operates with levels instead of points.")
                );

                config.set("can_all_player_open", false);
                config.setComments("can_all_player_open",
                        List.of(" ",
                                "If set true, any player can interact with the ghost, not just its owner.")
                );

                config.save(configFile);
            } catch (IOException e) {
                B_s_GhostGrave.getPlugin().getLogger().severe("Could not create configuration file!");
                e.printStackTrace();
            }
        } else {
            // Load config in a fail-safe way
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            try {
                particleCount = config.getInt("particle_count");
            } catch (Exception ex) {
                B_s_GhostGrave.getPlugin().getLogger().warning("'particle_count' is not properly set in the config, so it's been set to '10' as default!");
                config.set("particle_count", 10);
            }

            try {
                experienceCount = config.getInt("experience_count");
            } catch (Exception ex) {
                B_s_GhostGrave.getPlugin().getLogger().warning("'experience_count' is not properly set in the config, so it's been set to '0' as default!");
                config.set("experience_count", 0);
            }

            try {
                noItemSpawns = config.getBoolean("no-item_spawns");
            } catch (Exception ex) {
                B_s_GhostGrave.getPlugin().getLogger().warning("'no-item_spawns' is not properly set in the config, so it's been set to 'false' as default!");
                config.set("no-item_spawns", false);
            }

            try {
                vanillaExperience = config.getBoolean("vanilla_experience");
            } catch (Exception ex) {
                B_s_GhostGrave.getPlugin().getLogger().warning("'vanilla_experience' is not properly set in the config, so it's been set to 'false' as default!");
                config.set("vanilla_experience", false);
            }

            try {
                canAllPlayerOpen = config.getBoolean("can_all_player_open");
            } catch (Exception ex) {
                B_s_GhostGrave.getPlugin().getLogger().warning("'can_all_player_open' is not properly set in the config, so it's been set to 'false' as default!");
                config.set("can_all_player_open", false);
            }

            try {
                config.save(configFile);
            } catch (IOException e) {
                B_s_GhostGrave.getPlugin().getLogger().severe("Could not create configuration file!");
                e.printStackTrace();
            }
        }
    }
}

