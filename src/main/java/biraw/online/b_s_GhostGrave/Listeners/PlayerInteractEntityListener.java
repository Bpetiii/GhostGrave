package biraw.online.b_s_GhostGrave.Listeners;

import biraw.online.b_s_GhostGrave.B_s_GhostGrave;
import biraw.online.b_s_GhostGrave.Utils.SerializationUtils;
import org.bukkit.*;
import org.bukkit.entity.Bee;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import java.io.IOException;
import java.util.UUID;

public class PlayerInteractEntityListener implements Listener {

    @EventHandler
    private void playerInteractEntity(PlayerInteractAtEntityEvent event) throws IOException {

        // Check if the entity is a ghost if not, return
        if (event.getRightClicked().isDead()) return;
        if (event.getRightClicked().getType() != EntityType.BEE) return;
        NamespacedKey player_inventory_key = B_s_GhostGrave.getPlugin().GetNamespaceKey("player_inventory");
        if (!event.getRightClicked().getPersistentDataContainer().has(player_inventory_key)) return;

        // Getting the ghost and it's world into a variable
        Bee ghost = (Bee) event.getRightClicked();
        World ghostsWorld = ghost.getWorld();

        // Checking if the player has access to the ghost
        if (!B_s_GhostGrave.getConfigManager().canAllPlayerOpen)
        {
            NamespacedKey player_key = B_s_GhostGrave.getPlugin().GetNamespaceKey("player");
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(ghost.getPersistentDataContainer().get(player_key,PersistentDataType.STRING)));
            if (player.isOnline())
            {
                if (!player.equals(event.getPlayer())) return;
            }
            else return;
        }

        // Get inventory from PDC
        String serializedInventory = ghost.getPersistentDataContainer().get(player_inventory_key, PersistentDataType.STRING);

        // Convert the string back to ItemStack[]
        ItemStack[] inventory = SerializationUtils.itemStackArrayFromBase64(serializedInventory);

        // Drop the items
        for (ItemStack itemStack : inventory) {
            try { // The try is necessary because some ItemStack could be empty.
                ghostsWorld.dropItemNaturally(ghost.getLocation(), itemStack);
            } catch (Exception e) {}
        }

        // Give back XP
        NamespacedKey player_xp_key = B_s_GhostGrave.getPlugin().GetNamespaceKey("player_xp");
        int xp_points = ghost.getPersistentDataContainer().get(player_xp_key,PersistentDataType.INTEGER);
        if (B_s_GhostGrave.getConfigManager().vanillaExperience) xp_points = Math.clamp(xp_points,0,100); // Add the option of vanilla xp regain
        event.getPlayer().giveExp(xp_points);

        //Play sound and particles
        ghostsWorld.playSound(ghost.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1.0f, 1.0f);
        ghostsWorld.spawnParticle(Particle.END_ROD, ghost.getLocation(), 50);

        // Kill the ghost
        ghost.getPersistentDataContainer().remove(player_inventory_key);
        ghost.remove();
    }
}
