package org.mangorage.paperdev.core.attachment;

import org.mangorage.paperdev.core.interfaces.IAttachment;

import java.util.HashMap;
import java.util.Map;

public class AttachmentSystem {
    private final Map<AttachmentType<?, ?>, AttachmentManager<?, ?>> MANAGERS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <W, T extends IAttachment<W>> AttachmentManager<W, T> getAttachmentManager(AttachmentType<W, T> type) {
        return (AttachmentManager<W, T>) MANAGERS.computeIfAbsent(type, a -> new AttachmentManager<W, T>(type));
    }

    public void tickChildren() {
        MANAGERS.forEach((k, v) -> v.tickChildren());
    }
}
