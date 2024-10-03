package biraw.online.b_s_GhostGrave.Managers;

import biraw.online.b_s_GhostGrave.B_s_GhostGrave;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Bee;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class LoadManager {

    public static NamespacedKey is_ghost_key = B_s_GhostGrave.getPlugin().GetNamespaceKey("is_ghost");

    public  void UnloadGhost(Bee ghost)
    {
        try {
            Bukkit.getScheduler().cancelTask(ghost.getPersistentDataContainer().get(is_ghost_key, PersistentDataType.INTEGER));
        }catch (Exception ignored){}
    }

    public void LoadGhost(Bee ghost)
    {
        try {
            int taskId = ghost.getPersistentDataContainer().get(is_ghost_key, PersistentDataType.INTEGER);
            if(!Bukkit.getScheduler().isCurrentlyRunning(taskId))
            {
                NamespacedKey player_key = B_s_GhostGrave.getPlugin().GetNamespaceKey("player");
                String playerUuid = ghost.getPersistentDataContainer().get(player_key, PersistentDataType.STRING);

                int newTaskID = B_s_GhostGrave.getTaskManager().PlayTask(ghost,Bukkit.getPlayer(UUID.fromString(playerUuid)));
                ghost.getPersistentDataContainer().set(is_ghost_key,PersistentDataType.INTEGER,newTaskID);
            }
        }catch (Exception ignored){}
    }
}
