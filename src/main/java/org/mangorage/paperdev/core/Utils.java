package org.mangorage.paperdev.core;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.UUID;

public class Utils {
    public static ArmorStand spawnTextAboveHead(Location location, String text) {
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setCustomNameVisible(false);
        armorStand.customName(Component.text(text));
        armorStand.setGravity(false);  // Disable gravity so it stays in the air
        armorStand.setVisible(false); // Make the armor stand invisible
        armorStand.setMarker(true);    // Make the armor stand a marker, so it doesn't have a hitbox
        return armorStand;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> T spawnEntity(Location location, Class<T> entityClass) {
        return (T) location.getWorld().createEntity(location, entityClass);
    }

    public static void runOnMain(Runnable runnable) {
        var pl = Bukkit.getPluginManager().getPlugin("paperDev");
        if (pl == null) return;
        Bukkit.getScheduler().runTask(pl, runnable);
    }

    public static UUID getSafeUUID(String input) {
        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static boolean reached(int amount, int required) {
        return amount % required == 0;
    }

    public static Player getNearestPlayer(Location location, int distance) {
        Player nearestPlayer = null;
        double nearestDistanceSquared = Double.MAX_VALUE;

        for (Player player : Bukkit.getOnlinePlayers()) {
            Location playerLocation = player.getLocation();
            double distanceSquared = location.distanceSquared(playerLocation);

            if (distanceSquared < nearestDistanceSquared && distanceSquared <= distance) {
                nearestPlayer = player;
                nearestDistanceSquared = distanceSquared;
            }
        }

        return nearestPlayer;
    }

}
