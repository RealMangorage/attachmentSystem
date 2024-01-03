package org.mangorage.paperdev.core.impl;

import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.mangorage.paperdev.core.attachment.Attachment;
import org.mangorage.paperdev.core.interfaces.entity.IPlayer;

public class PlayerImpl extends Attachment<Player> implements IPlayer {
    private int ticks = 0;

    public PlayerImpl(Player wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Player getBukkitPlayer() {
        return getWrappedObject();
    }

    @Override
    public void tick() {
        ticks++;
        if (ticks % 50 == 0) {
            getBukkitPlayer().setExperienceLevelAndProgress(0);
            getBukkitPlayer().playSound(Sound.sound().type(org.bukkit.Sound.BLOCK_NOTE_BLOCK_COW_BELL).volume(1.0f).build());
        }
        getBukkitPlayer().giveExp(1);
    }
}
