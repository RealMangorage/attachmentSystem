package org.mangorage.paperdev.core.interfaces;

public interface IAttachmentFactory<W, T> {
    T create(W wrappedObject);
}
