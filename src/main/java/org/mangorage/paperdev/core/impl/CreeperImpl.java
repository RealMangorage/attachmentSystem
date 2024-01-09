package org.mangorage.paperdev.core.impl;

import com.destroystokyo.paper.entity.ai.PaperVanillaGoal;
import com.destroystokyo.paper.event.entity.CreeperIgniteEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.mangorage.paperdev.core.Utils;
import org.mangorage.paperdev.core.attachment.DetachReason;
import org.mangorage.paperdev.core.impl.entity.LivingEntityAttachment;

import java.util.Random;
import java.util.UUID;

import static org.mangorage.paperdev.core.Utils.reached;

public class CreeperImpl extends LivingEntityAttachment<Creeper> implements Listener {
    private static final String[] names = new String[]{"Creeper", "Super Creeper", "Mini Creeper"};
    private static final Random random = new Random();
    private final ArmorStand stand;

    private final String name;
    private final double maxHealth = 10;
    private double health = maxHealth;

    public CreeperImpl(Plugin plugin,NamespacedKey id, Creeper wrappedObject) {
        super(plugin, id, wrappedObject);
        this.name = names[random.nextInt(names.length)];
        this.stand = Utils.spawnTextAboveHead(wrappedObject.getLocation(), "Name");

        NamespacedKey key = new NamespacedKey(plugin, "name-tag");
        var data = wrappedObject.getPersistentDataContainer();

        if (data.has(key)) {
            var result = data.get(key, PersistentDataType.STRING);
            if (result == null) return;
            var eid = UUID.fromString(result);
            var entity = Bukkit.getServer().getEntity(eid);
            if (entity == null) return;
            entity.remove();
            System.out.println("Removed Stand");
        }

        data.set(key, PersistentDataType.STRING, this.stand.getUniqueId().toString());

        Bukkit.getServer().getMobGoals().removeGoal(getObject(), PaperVanillaGoal.SWELL);

        register(this);
    }

    public void updateName() {
        var name = Component.text(this.name).color(TextColor.color(Color.RED.asRGB()));
        var health = Component.text(" %s/%s".formatted(Math.floor(this.health), this.maxHealth)).color(TextColor.color(Color.ORANGE.asRGB()));
        this.stand.customName(name.append(health));
    }

    @Override
    public void tick() {
        if (reached(getTicks(), 20)) {
            health = Math.min(health + 5, maxHealth);
        }

        this.stand.teleport(getObject().getLocation().add(0, 1.5, 0));
        if (!this.stand.isCustomNameVisible()) this.stand.setCustomNameVisible(true);
        updateName();

        if (health <= 0) {
            getObject().remove();
        }
        if (health <= 50 && !getObject().isPowered()) {
            getObject().setPowered(true);
        } else if (health > 50 && getObject().isPowered()) {
            getObject().setPowered(false);
        }

        if (reached(getTicks(), 10)) {
            var player = Utils.getNearestPlayer(getObject().getLocation(), 8);
            if (player != null) getObject().setTarget(player);
        }

        var maxHealthAttribute = getObject().getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttribute == null) return;
        var maxHealth = maxHealthAttribute.getValue();
        var damage = maxHealth - getObject().getHealth();
        var entHealth = maxHealth <= 0 ? 0 : maxHealth;
        health -= damage;
        getObject().setHealth(entHealth);
    }

    @Override
    public void onRemove(DetachReason reason) {
        super.onRemove(reason);
        this.stand.remove();
        unregister(this);
    }

    @EventHandler
    public void onExplode(CreeperIgniteEvent event) {
        if (equalsAttachment(event.getEntity())) event.setCancelled(true);
    }

    @EventHandler
    public void onExplode2(ExplosionPrimeEvent event) {
        if (equalsAttachment(event.getEntity())) event.setCancelled(true);
    }
}
