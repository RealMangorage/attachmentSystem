package org.mangorage.paperdev.core.attachment;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public final class RegistryObject<T> {
    private final Class<T> tClass;
    private final Plugin plugin;
    private final NamespacedKey key;
    private final IAttachmentSupplier<T> supplier;
    RegistryObject(Class<T> tClass, Plugin plugin, String id, IAttachmentSupplier<T> supplier) {
        this.tClass = tClass;
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, id);
        this.supplier = supplier;
    }

    public Class<T> getClassType() {
        return tClass;
    }

    @SuppressWarnings("unchecked")
    void createCast(Object object) {
        create((T) object);
    }

    public void create(T object) {
        AttachmentSystem.getInstance().attach(
                () -> supplier.create(
                        plugin,
                        key,
                        object
                ),
                plugin
        );
    }
}
