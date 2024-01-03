package org.mangorage.paperdev.core.interfaces;

import org.bukkit.entity.Player;
import org.mangorage.paperdev.core.interfaces.ITicker;

public interface IPlayer extends ITicker {
    Player getBukkitPlayer();
}
