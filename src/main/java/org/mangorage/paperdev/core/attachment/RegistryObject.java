package org.mangorage.paperdev.core.attachment;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public final class RegistryObject<T, P> {
    private final AttachmentSystem system;
    private final Class<T> tClass;
    private final Plugin plugin;
    private final NamespacedKey key;
    private final IAttachmentSupplier<T, P> supplier;
    private final IAttachmentSupplier.ISpawner<T> spawner;

    RegistryObject(AttachmentSystem system, Class<T> tClass, Plugin plugin, String id, IAttachmentSupplier<T, P> supplier, IAttachmentSupplier.ISpawner<T> spawner) {
        this.system = system;
        this.tClass = tClass;
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, id);
        this.supplier = supplier;
        this.spawner = spawner;
    }

    public Class<T> getClassType() {
        return tClass;
    }

    @SuppressWarnings("unchecked")
    void createCast(Object object) {
        create((T) object);
    }

    public void create(T object) {
        system.attach(
                () -> supplier.create(
                        plugin,
                        key,
                        object
                ),
                plugin
        );
    }

    @SuppressWarnings("unchecked")
    public void spawn(Location location) {
        if (spawner == null) {
            throw new IllegalStateException("Cannot spawn %s due to registry object not supporting it. Make sure to set ISpawner to be not null".formatted(key));
        } else {
            Entity entity = (Entity) spawner.create(location, tClass);
            create((T) entity);
            location.getWorld().addEntity(entity);
        }
    }

    public NamespacedKey getID() {
        return key;
    }

    public boolean canSpawn() {
        return spawner != null;
    }
}
