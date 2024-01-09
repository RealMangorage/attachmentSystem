package org.mangorage.paperdev;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mangorage.paperdev.core.Registers;
import org.mangorage.paperdev.core.Utils;
import org.mangorage.paperdev.core.attachment.AttachmentCommand;
import org.mangorage.paperdev.core.attachment.AttachmentSystem;
import org.mangorage.paperdev.core.attachment.RegistryObject;
import org.mangorage.paperdev.core.impl.CreeperImpl;
import org.mangorage.paperdev.core.impl.PlayerImpl;

import java.util.TimerTask;

import static org.mangorage.paperdev.core.Registers.*;

public final class PaperDev extends JavaPlugin implements Listener {

    public static TimerTask task(Runnable runnable) {
        return new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }



    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Registers.init();
        var cmd = new AttachmentCommand();
        Bukkit.getPluginCommand("attachment").setExecutor(cmd);
        Bukkit.getPluginCommand("attachment").setTabCompleter(cmd);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onEntityJoin(EntityAddToWorldEvent event) {
        if (event.getEntity() instanceof Creeper creeper) {
            //creeperRO.create(creeper);
        } else if (event.getEntity() instanceof Player player) {
            playerRO.create(player);
        }
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        blockRO.create(event.getBlock());
    }
}
