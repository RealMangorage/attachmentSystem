package org.mangorage.paperdev.core.attachment;

public abstract class Attachment<T> {
    private final T wrappedObject;

    public Attachment(T wrappedObject) {
        this.wrappedObject = wrappedObject;
    }

    public T getWrappedObject() {
        return wrappedObject;
    }
}
