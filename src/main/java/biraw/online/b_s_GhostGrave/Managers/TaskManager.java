package biraw.online.b_s_GhostGrave.Managers;

import biraw.online.b_s_GhostGrave.B_s_GhostGrave;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TaskManager {

    public int PlayTask(Bee ghost, Player player){

        int particle_count = B_s_GhostGrave.getConfigManager().particleCount;
        int[] taskId = new int[1];
        // Schedule a task to run every 2 ticks (1/10 second)
        taskId[0] = Bukkit.getScheduler().scheduleSyncRepeatingTask(B_s_GhostGrave.getPlugin(), () -> {

            if (ghost.isDead()) Bukkit.getScheduler().cancelTask(taskId[0]);

            Location ghostLocation = ghost.getLocation().add(0,0.3,0);

            // Create particle effects
            Particle.DustOptions smoke = new Particle.DustOptions(Color.BLACK, 8f);


            // Setting ai false
            ghost.setAI(false);
            Particle.DustOptions eye = new Particle.DustOptions(Color.WHITE, 0.6f);

            // Spawn smoke particle at ghost's location
            ghost.getWorld().spawnParticle(Particle.DUST, ghostLocation, 2*particle_count, smoke);
            // Check distance between ghost and player
            if (ghostLocation.distance(player.getLocation()) < 20 && ghostLocation.distance(player.getLocation()) > 3) {
                // Disable "bee" behavior
                ghost.setFlower(null);
                ghost.setCannotEnterHiveTicks(100);
                ghost.setHive(null);
                //ghost.setAI(true);

                // Move the ghost toward the player
                //ghost.getPathfinder().moveTo(player.getLocation().add(player.getFacing().getModX() * 2.0, 1.0, player.getFacing().getModZ() * 2.0));
            }

            // GHOST EYES

            // Getting the ghosts looking direction
            Vector ghostVN = player.getLocation().add(0,1,0).toVector().subtract(ghostLocation.toVector()).normalize();

            // Rotate the vector by +5 degrees and -5 degrees for each eye
            Vector ghostEyeOneDirection = rotateVector(ghostVN, 45);
            Vector ghostEyeTwoDirection = rotateVector(ghostVN, -45);

            // Calculate the positions for the eyes
            Location ghostEyeOne = ghostLocation.clone().add(ghostEyeOneDirection.multiply(0.7));
            Location ghostEyeTwo = ghostLocation.clone().add(ghostEyeTwoDirection.multiply(0.7));

            // Spawn eye particles
            // Also add a little elevation, it looks better
            ghost.getWorld().spawnParticle(Particle.DUST, ghostEyeOne.add(0,0.2,0), particle_count, eye);
            ghost.getWorld().spawnParticle(Particle.DUST, ghostEyeTwo.add(0,0.2,0), particle_count, eye);

        }, 0L, 2L);

        return taskId[0];
    }

    public Vector rotateVector(Vector vector, double degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double x = vector.getX() * cos - vector.getZ() * sin;
        double z = vector.getX() * sin + vector.getZ() * cos;

        return new Vector(x, vector.getY(), z);
    }
}
