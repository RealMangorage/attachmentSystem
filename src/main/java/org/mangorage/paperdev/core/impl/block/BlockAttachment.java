package org.mangorage.paperdev.core.impl.block;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.mangorage.paperdev.core.impl.Attachment;

public abstract class BlockAttachment<T extends Block> extends Attachment<T> {
    public BlockAttachment(Plugin plugin, NamespacedKey id, T object) {
        super(plugin, id, object);
    }
}
