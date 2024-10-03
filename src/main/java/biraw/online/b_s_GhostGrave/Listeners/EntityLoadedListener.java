package biraw.online.b_s_GhostGrave.Listeners;

import biraw.online.b_s_GhostGrave.B_s_GhostGrave;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;

import java.util.List;


public class EntityLoadedListener implements Listener {
    // If a ghost is loaded start its task.
    @EventHandler
    private void EntityLoaded(EntitiesLoadEvent event){
        List<Entity> entities = event.getEntities();
        NamespacedKey key = B_s_GhostGrave.getPlugin().GetNamespaceKey("is_ghost");
        entities.forEach(entity -> {
            if(entity.getType() == EntityType.BEE){
                if (entity.getPersistentDataContainer().has(key))
                {
                    B_s_GhostGrave.getLoadManager().LoadGhost((Bee) entity);
                }
            }
        });
    }
}