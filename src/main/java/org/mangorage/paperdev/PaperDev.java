package org.mangorage.paperdev;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mangorage.paperdev.core.attachment.AttachmentSystem;
import org.mangorage.paperdev.core.attachment.DetachReason;
import org.mangorage.paperdev.core.attachment.RegistryObject;
import org.mangorage.paperdev.core.impl.CreeperImpl;
import org.mangorage.paperdev.core.impl.PlayerImpl;

import java.util.TimerTask;

public final class PaperDev extends JavaPlugin implements Listener {

    public static TimerTask task(Runnable runnable) {
        return new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    private final AttachmentSystem attachmentSystem = AttachmentSystem.register(this);
    public RegistryObject<Creeper> creeperRO = attachmentSystem.createRegistry(Creeper.class, this, "test", CreeperImpl::new);
    public RegistryObject<Player> playerRO = attachmentSystem.createRegistry(Player.class, this, "test2", PlayerImpl::new);

    @Override
    public void onEnable() {
        attachmentSystem.init();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getScheduler().runTaskTimer(this, attachmentSystem::tick, 0, 1);

    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {

    }

    @EventHandler
    public void onEntityJoin(EntityAddToWorldEvent event) {
        if (event.getEntity() instanceof Creeper creeper) {
            creeperRO.create(creeper);
        } else if (event.getEntity() instanceof Player player) {
            playerRO.create(player);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        attachmentSystem.detachAll(event.getEntity(), DetachReason.KILLED);
    }

    @EventHandler
    public void onEntityDie(EntityRemoveFromWorldEvent event) {

    }
}
