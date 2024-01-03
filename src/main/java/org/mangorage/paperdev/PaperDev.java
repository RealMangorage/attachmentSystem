package org.mangorage.paperdev;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mangorage.paperdev.core.attachment.AttachmentSystem;
import org.mangorage.paperdev.core.attachment.Attachments;
import org.mangorage.paperdev.core.impl.PlayerImpl;
import org.mangorage.paperdev.core.interfaces.ITicker;

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

    private final AttachmentSystem attachmentSystem = new AttachmentSystem();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            attachmentSystem.getAttachmentManager(Attachments.PLAYER).getAttachments().forEach(ITicker::tick);
        }, 0, 1);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        attachmentSystem.getAttachmentManager(Attachments.PLAYER).attach(event.getPlayer(), PlayerImpl::new);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        attachmentSystem.getAttachmentManager(Attachments.PLAYER).detachWrapped(event.getPlayer());
    }
}
