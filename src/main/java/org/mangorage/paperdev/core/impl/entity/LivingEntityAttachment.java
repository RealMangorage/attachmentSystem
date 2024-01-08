package org.mangorage.paperdev.core.impl.entity;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public abstract class LivingEntityAttachment<T extends LivingEntity> extends EntityAttachment<T> {
    public LivingEntityAttachment(Plugin plugin, NamespacedKey id, T wrappedObject) {
        super(plugin, id, wrappedObject);
    }
}
