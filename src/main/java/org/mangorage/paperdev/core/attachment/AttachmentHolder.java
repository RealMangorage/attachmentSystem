package org.mangorage.paperdev.core.attachment;

import org.bukkit.NamespacedKey;
import org.mangorage.paperdev.core.impl.Attachment;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AttachmentHolder {
    private final Map<NamespacedKey, Attachment<?>> attachments = new ConcurrentHashMap<>();

    public boolean attach(NamespacedKey attachmentID, Attachment<?> attachment) {
        return attachments.putIfAbsent(attachmentID, attachment) == null; // true if it was successful at attaching it, false if it already exists
    }
    public void tick() {
        attachments.forEach((k, v) -> v.baseTick());
    }

    public void detach(NamespacedKey attachmentID, DetachReason reason) {
        var attachment = attachments.get(attachmentID);
        if (attachment == null) return;
        attachment.onRemove(reason);
        attachments.remove(attachmentID);
    }

    public void detachAll(DetachReason reason) {
        attachments.forEach((k, v) -> {
            detach(k, reason);
        });
    }
}
