package org.mangorage.paperdev.core.impl;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.mangorage.paperdev.core.attachment.AttachmentSystem;
import org.mangorage.paperdev.core.attachment.DetachReason;

import java.util.HashMap;

import static org.mangorage.paperdev.core.Utils.reached;

public class PlayerImpl extends LivingEntityAttachment<Player> implements Listener {

    @FunctionalInterface
    private interface IAction {
        IAction EMPTY = (a, b) -> {};
        void run(PlayerImpl instance, Object dataObject);
    }

    private static final HashMap<Material, IAction> actions = new HashMap<>();

    static {
        actions.put(Material.STICK, (p, o) -> {
            if (o instanceof Entity entity) {
                AttachmentSystem.getInstance().detachAll(o, DetachReason.REMOVED);
            }
        });
    }



    public PlayerImpl(Plugin plugin, Player wrappedObject) {
        super(plugin, wrappedObject);
        register(this);
    }

    @Override
    public void onRemove(DetachReason reason) {
        unregister(this);
    }

    @Override
    public void tick() {
        if (reached(getTicks(), 10)) getWrappedObject().giveExp(3);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (equalsAttachment(event.getDamager())) {
            var item = getWrappedObject().getInventory().getItemInMainHand().getType();
            actions.getOrDefault(item, IAction.EMPTY).run(this, event.getEntity());
        }
    }
}
