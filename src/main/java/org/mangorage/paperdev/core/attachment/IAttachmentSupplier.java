package org.mangorage.paperdev.core.attachment;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.mangorage.paperdev.core.impl.Attachment;

@FunctionalInterface
public interface IAttachmentSupplier<T, P> {
    Attachment<T> create(Plugin plugin, NamespacedKey id, T object, P parameter);
    default Attachment<T> create(Plugin plugin, NamespacedKey id, T object) {
        return create(plugin, id, object, null);
    }
    @FunctionalInterface
    public interface ISpawner<T> {
        T create(Location location, Class<T> entityClass);
    }
}
