package org.mangorage.paperdev.core.attachment;

import org.bukkit.NamespacedKey;
import org.mangorage.paperdev.core.impl.Attachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public final class AttachmentHolder {
    private final Map<NamespacedKey, Attachment<?>> attachments = new ConcurrentHashMap<>();

    boolean attach(NamespacedKey attachmentID, Attachment<?> attachment) {
        return attachments.putIfAbsent(attachmentID, attachment) == null; // true if it was successful at attaching it, false if it already exists
    }
    void tick() {
        attachments.forEach((k, v) -> {
            v.baseTick();
        });
    }

    void saveAll() {
        attachments.forEach((k, v) -> v.save());
    }

    void loadAll() {
        attachments.forEach((k, v) -> v.load());
    }

    void detach(NamespacedKey attachmentID, DetachReason reason) {
        var attachment = attachments.get(attachmentID);
        if (attachment == null) return;
        attachment.onRemove(reason);
        attachments.remove(attachmentID);
    }

    void detachAll(DetachReason reason) {
        List<Runnable> runnables = new ArrayList<>();
        attachments.forEach((k, v) -> {
            runnables.add(() -> detach(k, reason));
        });
        runnables.forEach(Runnable::run);
    }
}
