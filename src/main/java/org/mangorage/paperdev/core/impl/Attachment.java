package org.mangorage.paperdev.core.impl;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.mangorage.paperdev.core.attachment.DetachReason;

public abstract class Attachment<T> {
    private final Plugin plugin;
    private final T object;
    private int ticks;

    public Attachment(Plugin plugin, T object) {
        this.plugin = plugin;
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public int getTicks() {
        return ticks;
    }

    public void baseTick() {
        ticks++;
        preTick();
    }

    public abstract void preTick();
    public abstract void tick();
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
}
