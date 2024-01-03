package org.mangorage.paperdev.core.attachment;

import org.mangorage.paperdev.core.interfaces.IAttachment;
import org.mangorage.paperdev.core.interfaces.IAttachmentFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AttachmentManager<W, T extends IAttachment<W>> {
    private final AttachmentType<W, T> type;
    private final List<T> attachments = new CopyOnWriteArrayList<>();

    protected AttachmentManager(AttachmentType<W, T> attachmentType) {
        this.type = attachmentType;
    }

    public void tickChildren() {
        type.ticker().tick(getAttachments());
    }

    public void attach(T attachment) {
        attachments.add(attachment);
    }

    public void attach(W object, IAttachmentFactory<W, T> factory) {
        attach(factory.create(object));
    }

    public void detach(T attachment, DetachReason reason) {
        attachments.remove(attachment);
        attachment.removed(reason);
    }

    public void detachWrapped(W wrappedObject, DetachReason reason) {
        var result = findByWrappedType(wrappedObject);
        if (result != null) detach(result, reason);
    }

    public T findByWrappedType(W wrappedObject) {
        return attachments.stream().filter(a -> type.predicate().test(a.getWrappedObject(), wrappedObject)).findAny().orElse(null);
    }

    public List<T> getAttachments() {
        return attachments;
    }
}
