package org.mangorage.paperdev.core.attachment;

import net.minecraft.world.phys.AABB;
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
import org.mangorage.paperdev.core.misc.ObjectHolder;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.bukkit.Bukkit.getServer;

public final class AttachmentSystem {
    private static final NamespacedKey TAG = new NamespacedKey("attachmentapi", "attachments");
    private static final HashMap<Plugin, AttachmentSystem> ATTACHMENTS = new HashMap<>();

    public static AttachmentSystem create(Supplier<Plugin> plugin) {
        return ATTACHMENTS.computeIfAbsent(plugin.get(), p -> new AttachmentSystem(plugin));
    }

    public static List<String> getAttachmentIDs(boolean spawnableOnly) {
        List<String> attachments = new ArrayList<>();
        ATTACHMENTS.forEach((k, v) -> {
            v.registryObjects.forEach((k2, v2) -> {
                if (spawnableOnly && v2.canSpawn()) attachments.add(v2.getID().toString());
                if (!spawnableOnly) attachments.add(v2.getID().toString());
            });
        });
        return attachments;
    }

    @SuppressWarnings("all")
    static RegistryObject findAttachment(String id) {
        AtomicReference<RegistryObject> reference = new AtomicReference<>();
        ATTACHMENTS.forEach((k, v) -> {
            v.registryObjects.forEach((k2, v2) -> {
                if (v2.getID().toString().equals(id)) {
                    reference.set(v2);
                    return;
                };
            });
        });
        return reference.get();
    }

    public static void detachStatic(Object object, NamespacedKey attachmentID, DetachReason reason) {
        for (AttachmentSystem value : ATTACHMENTS.values()) {
            if (value.detach(object, attachmentID, reason)) break;
        }
    }

    public static void detachAllStatic(Object object, DetachReason reason) {
        for (AttachmentSystem value : ATTACHMENTS.values()) {
            if (value.detachAll(object, reason)) break;
        }
    }

    public static NamespacedKey getAttachmentsTag() {
        return TAG;
    }

    private final Map<ObjectHolder, AttachmentHolder> attachmentData = new WeakHashMap<>();
    private final Map<NamespacedKey, RegistryObject<?, ?>> registryObjects = new HashMap<>();

    private final Plugin plugin;

    private AttachmentSystem(Supplier<Plugin> plugin) {
        this.plugin = plugin.get();
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
        Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 0, 1);
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

    public <T, P> RegistryObject<T, P> createRegistry(Class<T> type, String id, IAttachmentSupplier<T, P> supplier, IAttachmentSupplier.ISpawner<T> spawner) {
        var ro = new RegistryObject<>(this, type, plugin, id, supplier, spawner);
        registryObjects.put(new NamespacedKey(plugin, id), ro);
        return ro;
    }

    public <T, P> RegistryObject<T, P> createRegistry(Class<T> type, String id, IAttachmentSupplier<T, P> supplier) {
        return createRegistry(type, id, supplier, null);
    }

    public void tick() {
        List<Runnable> afterTask = new ArrayList<>();
        attachmentData.forEach((k, v) -> checkAndTick(k, v, afterTask));
        afterTask.forEach(Runnable::run);
    }

    private void checkAndTick(ObjectHolder holder, AttachmentHolder attachment, List<Runnable> afterTask) {
        if (holder.isValid()) {
            attachment.tick();
        } else {
            afterTask.add(() -> {
                holder.invalidate();
                attachment.invalidate();
            });
        }
    }

    private AttachmentHolder getHolder(Object object) {
        return attachmentData.computeIfAbsent(ObjectHolder.create(object, this::remove), a -> new AttachmentHolder());
    }

    private void remove(ObjectHolder holder) {

    }

    private AttachmentHolder findHolder(Object object) {
        return attachmentData.get(ObjectHolder.of(object));
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
