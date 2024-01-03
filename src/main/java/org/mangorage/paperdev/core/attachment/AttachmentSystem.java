package org.mangorage.paperdev.core.attachment;

import java.util.HashMap;
import java.util.Map;

public class AttachmentSystem {
    private final Map<AttachmentType<?, ?>, AttachmentManager<?, ?>> MANAGERS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <W, T> AttachmentManager<W, T> getAttachmentManager(AttachmentType<W, T> type) {
        return (AttachmentManager<W, T>) MANAGERS.computeIfAbsent(type, AttachmentManager::new);
    }
}
