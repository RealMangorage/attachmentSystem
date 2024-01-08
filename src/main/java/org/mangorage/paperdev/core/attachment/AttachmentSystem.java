package org.mangorage.paperdev.core.attachment;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.Plugin;
import org.mangorage.paperdev.core.impl.Attachment;
import org.mangorage.paperdev.core.misc.NamespacedKeyHashsetDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Supplier;

import static org.bukkit.Bukkit.getServer;

public final class AttachmentSystem {
    private static AttachmentSystem INSTANCE;

    public static AttachmentSystem register(Plugin plugin) {
        if (INSTANCE == null) INSTANCE = new AttachmentSystem(plugin);
        return INSTANCE;
    }
    public static AttachmentSystem getInstance() {
        return INSTANCE;
    }

    private final Map<Object, AttachmentHolder> attachmentData = new WeakHashMap<>();
    private final Map<NamespacedKey, RegistryObject<?>> registryObjects = new HashMap<>();

    private final Plugin plugin;
    private final NamespacedKey attachmentsTag;

    private boolean loaded;
    private AttachmentSystem(Plugin plugin) {
        this.plugin = plugin;
        this.attachmentsTag = new NamespacedKey(plugin, "attachments");
    }

    public void init() {
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onSave(WorldSaveEvent event) {
                save();
            }

            @EventHandler
            public void onEntitiesLoad(EntitiesLoadEvent event) {
                load(event.getEntities());
            }

            @EventHandler
            public void onServerLoad(ServerLoadEvent event) {
                if (event.getType() == ServerLoadEvent.LoadType.RELOAD) load(false);
            }
        }, plugin);
    }

    public NamespacedKey getAttachmentsTag() {
        return attachmentsTag;
    }

    private void save() {
        attachmentData.forEach((k, v) -> v.saveAll());
    }

    private void load(List<Entity> entities) {
        entities.forEach(entity -> {
            var data = entity.getPersistentDataContainer();
            if (data.has(getAttachmentsTag())) {
                var attachments = data.get(getAttachmentsTag(), NamespacedKeyHashsetDataType.TYPE);
                if (attachments != null) {
                    attachments.forEach(id -> {
                        var ro = registryObjects.get(id);
                        if (ro != null) ro.createCast(entity);
                        System.out.println("Created Attachment");
                    });
                }
            }
        });
    }

    private void load(boolean checkIfLoaded) {
        getServer().getWorlds().forEach(world -> {
            load(world.getEntities());
        });
    }

    public <T> RegistryObject<T> createRegistry(Class<T> type, Plugin plugin, String id, IAttachmentSupplier<T> supplier) {
        var ro = new RegistryObject<>(type, plugin, id, supplier);
        registryObjects.put(new NamespacedKey(plugin, id), ro);
        return ro;
    }

    public void tick() {
        attachmentData.forEach((k, v) -> v.tick());
    }

    private AttachmentHolder getHolder(Object object) {
        return attachmentData.computeIfAbsent(object, o -> new AttachmentHolder());
    }

    private AttachmentHolder findHolder(Object object) {
        return attachmentData.get(object);
    }

    private boolean attach(Attachment<?> attachment) {
        var holder = getHolder(attachment.getObject());
        return holder.attach(attachment.getID(), attachment);
    }

    /*
    Ensures this will be run on main thread... and not the event thread
     */
    void attach(Supplier<Attachment<?>> attachmentSupplier, Plugin plugin) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            attach(attachmentSupplier.get());
        });
    }

    public boolean detachAll(Object object, DetachReason reason) {
        var holder = findHolder(object);
        if (holder == null) return false;
        holder.detachAll(reason);
        return true;
    }

    public boolean detach(Object object, NamespacedKey attachmentID, DetachReason reason) {
        var holder = findHolder(object);
        if (holder == null) return false;
        holder.detach(attachmentID, reason);
        return true;
    }
}
