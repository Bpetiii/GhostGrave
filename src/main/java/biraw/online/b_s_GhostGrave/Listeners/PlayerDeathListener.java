package biraw.online.b_s_GhostGrave.Listeners;

import biraw.online.b_s_GhostGrave.B_s_GhostGrave;
import biraw.online.b_s_GhostGrave.Utils.SerializationUtils;
import org.bukkit.*;
import org.bukkit.entity.Bee;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerDeathListener implements Listener {

    @EventHandler
    void playerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().isEmpty() && !B_s_GhostGrave.getConfigManager().noItemSpawns) return;
        event.setDroppedExp(0);
        event.getDrops().clear();

        // Spawn the ghost and set its properties
        Bee ghost = (Bee) player.getWorld().spawnEntity(player.getLocation().add(0, 2, 0), EntityType.BEE);
        ghost.setSilent(true);
        ghost.setInvisible(true);
        ghost.setInvulnerable(true);
        ghost.setPersistent(true);

        // Save the inventory to the PDC
        String serializedInventory = SerializationUtils.itemStackArrayToBase64(player.getInventory().getContents());
        NamespacedKey player_inventory_key = B_s_GhostGrave.getPlugin().GetNamespaceKey("player_inventory");
        ghost.getPersistentDataContainer().set(player_inventory_key, PersistentDataType.STRING, serializedInventory);

        // Save the xp
        NamespacedKey player_xp_key = B_s_GhostGrave.getPlugin().GetNamespaceKey("player_xp");
        int player_xp = B_s_GhostGrave.getConfigManager().vanillaExperience
                ? player.calculateTotalExperiencePoints()
                : Math.floorDiv(player.calculateTotalExperiencePoints(), 100) * B_s_GhostGrave.getConfigManager().experienceCount;
        ghost.getPersistentDataContainer().set(player_xp_key, PersistentDataType.INTEGER, player_xp);

        // Start the task of the ghost
        int ghostTaskId = B_s_GhostGrave.getTaskManager().PlayTask(ghost, player);

        // Save ghost's owner and its id
        NamespacedKey is_ghost_key = B_s_GhostGrave.getPlugin().GetNamespaceKey("is_ghost");
        ghost.getPersistentDataContainer().set(is_ghost_key, PersistentDataType.INTEGER, ghostTaskId);
        NamespacedKey player_key = B_s_GhostGrave.getPlugin().GetNamespaceKey("player");
        ghost.getPersistentDataContainer().set(player_key, PersistentDataType.STRING, player.getUniqueId().toString());
    }
}
