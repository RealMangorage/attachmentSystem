package org.mangorage.paperdev.core.attachment;

import org.mangorage.paperdev.core.interfaces.IAttachment;
import org.mangorage.paperdev.core.interfaces.IAttachmentTicker;

import java.util.function.BiPredicate;

public record AttachmentType<W, T>(Class<W> wrappedType, Class<T> realType, BiPredicate<W, W> predicate, IAttachmentTicker<W, T> ticker) {
    public static class Builder<W, T> {
        public static <W, T> Builder<W, T> create(Class<W> wrappedType, Class<T> realType) {
            return new Builder<>(wrappedType, realType);
        }

        private final Class<W> wClass;
        private final Class<T> tClass;
        private BiPredicate<W, W> predicate = Object::equals;
        private IAttachmentTicker<W, T> ticker = l -> {};

        protected Builder(Class<W> wrappedType, Class<T> realType) {
            this.wClass = wrappedType;
            this.tClass = realType;
        }

        public Builder<W, T> predicate(BiPredicate<W, W> predicate) {
            this.predicate = predicate;
            return this;
        }

        public Builder<W, T> ticker(IAttachmentTicker<W, T> ticker) {
            this.ticker = ticker;
            return this;
        }

        public AttachmentType<W, T> build() {
            return new AttachmentType<>(wClass, tClass, predicate, ticker);
        }
    }
}
