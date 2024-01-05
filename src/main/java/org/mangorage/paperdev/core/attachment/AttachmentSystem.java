package org.mangorage.paperdev.core.attachment;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.mangorage.paperdev.core.impl.Attachment;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public class AttachmentSystem {
    private static final AttachmentSystem INSTANCE = new AttachmentSystem();
    public static AttachmentSystem getInstance() {
        return INSTANCE;
    }

    private final List<Attachment<?>> attachments = new CopyOnWriteArrayList<>();

    private AttachmentSystem() {

    }

    public void tick() {
        attachments.forEach(Attachment::baseTick);
    }

    public void attach(Attachment<?> attachment) {
        attachments.add(attachment);
    }

    /*
    Ensures this will be run on main thread... and not the event thread
     */
    public void attach(Supplier<Attachment<?>> attachmentSupplier, Plugin plugin) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            attach(attachmentSupplier.get());
        });
    }

    public <T> void detachAll(T object, DetachReason reason) {
        attachments.stream().filter(a -> a.equalsAttachment(object)).forEach(a -> {
            a.onRemove(reason);
            attachments.remove(a);
        });
    }

    public void detachAll(DetachReason reason) {
        attachments.forEach(a -> {
            a.onRemove(reason);
            attachments.remove(a);
        });
    }

    public void detach(Attachment<?> attachment, DetachReason reason) {
        attachment.onRemove(reason);
        attachments.remove(attachment);
    }
}
