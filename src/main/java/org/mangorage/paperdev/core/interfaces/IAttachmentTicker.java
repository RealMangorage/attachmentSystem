package org.mangorage.paperdev.core.interfaces;

import java.util.List;

@FunctionalInterface
public interface IAttachmentTicker<W, T> {
    void tick(List<T> attachments);
}
