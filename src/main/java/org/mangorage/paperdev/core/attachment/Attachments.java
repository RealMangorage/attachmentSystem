package org.mangorage.paperdev.core.attachment;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.mangorage.paperdev.core.interfaces.IAttachment;
import org.mangorage.paperdev.core.interfaces.entity.IEntity;
import org.mangorage.paperdev.core.interfaces.entity.IPlayer;

public class Attachments {
    public static final AttachmentType<Player, IPlayer> PLAYER =
            AttachmentType.Builder.create(Player.class, IPlayer.class)
                    .predicate((a, b) -> a.getUniqueId().equals(b.getUniqueId()))
                    .ticker(l -> l.forEach(IAttachment::tick))
                    .build();
    public static final AttachmentType<Entity, IEntity> ENTITIES =
            AttachmentType.Builder.create(Entity.class, IEntity.class)
                    .predicate((a, b) -> a.getUniqueId().equals(b.getUniqueId()))
                    .ticker(l -> l.forEach(IAttachment::tick))
                    .build();
}
