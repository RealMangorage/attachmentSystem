package org.mangorage.paperdev.core.interfaces;

import org.mangorage.paperdev.core.attachment.DetachReason;

public interface IAttachment<T> {
    T getWrappedObject();
    default void tick() {}
    default void removed(DetachReason reason) {}
}
