package org.mangorage.paperdev.core.misc;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;

public final class ObjectHolder {
    private static final Map<Object, ObjectHolder> objects = new WeakHashMap<>();

    public static ObjectHolder create(Object o) {
        return objects.computeIfAbsent(o, a -> new ObjectHolder(a, List.of(objects::remove)));
    }

    public static ObjectHolder create(Object o, Consumer<ObjectHolder> onRemove) {
        return objects.computeIfAbsent(o, a -> new ObjectHolder(a, List.of(onRemove)));
    }

    public static ObjectHolder of(Object o) {
        return objects.computeIfAbsent(o, a -> new ObjectHolder(a, List.of(objects::remove)));
    }


    private final WeakReference<Object> object;
    private final List<Consumer<ObjectHolder>> consumers;

    private ObjectHolder(Object object, List<Consumer<ObjectHolder>> consumers) {
        this.object = new WeakReference<>(object);
        this.consumers = consumers;
    }

    public void invalidate() {
        for (Consumer<ObjectHolder> consumer : consumers) {
            consumer.accept(this);
        }
    }

    public boolean isValid() {
        return object.get() != null;
    }
}
