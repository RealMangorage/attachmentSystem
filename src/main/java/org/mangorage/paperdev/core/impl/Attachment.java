package org.mangorage.paperdev.core.impl;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.mangorage.paperdev.core.attachment.AttachmentSystem;
import org.mangorage.paperdev.core.attachment.DetachReason;
import java.lang.ref.WeakReference;

import static org.mangorage.paperdev.core.Utils.reached;

public abstract class Attachment<T> {
    private final Plugin plugin;
    private final NamespacedKey id;
    private final WeakReference<T> object;
    private int ticks;

    public Attachment(Plugin plugin, NamespacedKey id, T object) {
        this.plugin = plugin;
        this.id = id;
        this.object = new WeakReference<>(object, null);
    }

    public T getObject() {
        return object.get();
    }

    public int getTicks() {
        return ticks;
    }

    public void baseTick() {
        ticks++;
        if (isValid()) {
            tick();
        } else {
            AttachmentSystem.detachStatic(getObject(), getID(), DetachReason.DISCARDED);
        }
    }

    public void save() {}
    public void load() {}
    public abstract boolean isValid();
    public abstract void tick();

    public void invalidate() {
        onRemove(DetachReason.REMOVED);
    }

    public void onRemove(DetachReason reason) {
        System.out.println("Removed Attachment [%s] [%s]".formatted(reason, getObject().getClass().getName()));
    }

    public final boolean equalsAttachment(Object object) {
        return object.equals(getObject());
    }

    protected final void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    protected final void unregister(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public NamespacedKey getID() {
        return id;
    }
}
