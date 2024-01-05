package org.mangorage.paperdev;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mangorage.paperdev.core.attachment.AttachmentSystem;
import org.mangorage.paperdev.core.attachment.DetachReason;
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

    private final AttachmentSystem attachmentSystem = AttachmentSystem.getInstance();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getScheduler().runTaskTimer(this, attachmentSystem::tick, 0, 1);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        attachmentSystem.attach(new PlayerImpl(this, event.getPlayer()));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        attachmentSystem.detachAll(event.getPlayer(), DetachReason.REMOVED);
    }

    @EventHandler
    public void onEntityJoin(EntityAddToWorldEvent event) {
        if (event.getEntity() instanceof Creeper creeper) attachmentSystem.attach(() -> new CreeperImpl(this, creeper), this);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        attachmentSystem.detachAll(event.getEntity(), DetachReason.KILLED);
    }

    @EventHandler
    public void onEntityDie(EntityRemoveFromWorldEvent event) {
        attachmentSystem.detachAll(event.getEntity(), DetachReason.REMOVED);
    }
}
