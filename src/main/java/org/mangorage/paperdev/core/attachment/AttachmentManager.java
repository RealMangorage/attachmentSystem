package org.mangorage.paperdev.core.attachment;

import org.mangorage.paperdev.core.interfaces.IAttachmentFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AttachmentManager<W, T> {
    private final Class<W> wrappedClass;
    private final Class<T> type;
    private final List<T> attachments = new CopyOnWriteArrayList<>();

    protected AttachmentManager(AttachmentType<W, T> AttachmentType) {
        this.wrappedClass = AttachmentType.wrappedType();
        this.type = AttachmentType.realType();
    }

    public void attach(T attachment) {
        attachments.add(attachment);
    }

    public void attach(W object, IAttachmentFactory<W, T> factory) {
        attach(factory.create(object));
    }

    public void detach(T attachment) {
        attachments.remove(attachment);
    }

    public void detachWrapped(W wrappedObject) {
        var result = findByWrappedType(wrappedObject);
        if (result != null) detach(result);
    }

    public T findByWrappedType(W wrappedObject) {
        return attachments.stream().filter(a -> a == wrappedObject).findAny().orElse(null);
    }

    public List<T> getAttachments() {
        return attachments;
    }
}
