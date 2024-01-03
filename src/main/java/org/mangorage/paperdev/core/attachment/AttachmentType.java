package org.mangorage.paperdev.core.attachment;

public record AttachmentType<W, T>(Class<W> wrappedType, Class<T> realType){}
