package org.mangorage.paperdev.core.attachment;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.mangorage.paperdev.core.impl.Attachment;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class AttachmentSystem {
    private static final AttachmentSystem INSTANCE = new AttachmentSystem();
    public static AttachmentSystem getInstance() {
        return INSTANCE;
    }

    private final WeakHashMap<Object, AttachmentHolder> attachmentData = new WeakHashMap<>();

    private AttachmentSystem() {

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

    public boolean attach(NamespacedKey attachmentID, Attachment<?> attachment) {
        var holder = getHolder(attachment.getObject());
        return holder.attach(attachmentID, attachment);
    }

    /*
    Ensures this will be run on main thread... and not the event thread
     */
    public void attach(Supplier<Attachment<?>> attachmentSupplier, NamespacedKey attachmentID, Plugin plugin) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            attach(attachmentID, attachmentSupplier.get());
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
