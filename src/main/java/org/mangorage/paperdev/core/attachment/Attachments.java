package org.mangorage.paperdev.core.attachment;

import org.bukkit.entity.Player;
import org.mangorage.paperdev.core.interfaces.IPlayer;

public class Attachments {
    public static final AttachmentType<Player, IPlayer> PLAYER = new AttachmentType<>(Player.class, IPlayer.class);

}
