package org.mangorage.paperdev.core.impl;

import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public abstract class LivingEntityAttachment<T extends LivingEntity> extends Attachment<T> {
    public LivingEntityAttachment(Plugin plugin, T wrappedObject) {
        super(plugin, wrappedObject);
    }

    @Override
    public void preTick() {
        if (getObject() != null && getObject().isInWorld()) tick();
    }
}
