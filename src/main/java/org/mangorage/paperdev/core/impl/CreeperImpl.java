package org.mangorage.paperdev.core.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.mangorage.paperdev.core.attachment.Attachment;
import org.mangorage.paperdev.core.attachment.DetachReason;
import org.mangorage.paperdev.core.interfaces.entity.ICreeper;

public class CreeperImpl extends Attachment<Creeper> implements ICreeper {
    private int ticks = 0;
    public CreeperImpl(Creeper wrappedObject) {
        super(wrappedObject);
    }

    public double getDistanceToNearestPlayer(Entity entity) {
        double minDistance = Double.MAX_VALUE;

        for (Player player : Bukkit.getOnlinePlayers()) {
            double distance = entity.getLocation().distance(player.getLocation());
            minDistance = Math.min(minDistance, distance);
        }

        return minDistance;
    }

    @Override
    public void tick() {
        ticks++;
        if (ticks % 10 == 0) {
            if (getDistanceToNearestPlayer(getWrappedObject()) <= 1.2) explode();
        }
    }

    @Override
    public void explode() {
        getWrappedObject().explode();
    }

    @Override
    public void removed(DetachReason reason) {
        var a = 1;
    }
}
