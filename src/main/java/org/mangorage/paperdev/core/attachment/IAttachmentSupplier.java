package org.mangorage.paperdev.core.attachment;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.mangorage.paperdev.core.impl.Attachment;

@FunctionalInterface
public interface IAttachmentSupplier<T> {
    Attachment<T> create(Plugin plugin, NamespacedKey id, T object);
}
