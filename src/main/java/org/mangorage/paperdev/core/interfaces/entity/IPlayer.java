package org.mangorage.paperdev.core.interfaces.entity;

import org.bukkit.entity.Player;
import org.mangorage.paperdev.core.interfaces.IAttachment;

public interface IPlayer extends IAttachment<Player> {
    Player getBukkitPlayer();
}
