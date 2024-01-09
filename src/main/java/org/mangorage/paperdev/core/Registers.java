package org.mangorage.paperdev.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.mangorage.paperdev.core.attachment.AttachmentSystem;
import org.mangorage.paperdev.core.attachment.RegistryObject;
import org.mangorage.paperdev.core.impl.CreeperImpl;
import org.mangorage.paperdev.core.impl.PlayerImpl;

public class Registers {
    private static final AttachmentSystem attachmentSystem = AttachmentSystem.create(() -> Bukkit.getPluginManager().getPlugin("paperDev"));
    public static RegistryObject<Creeper, Void> creeperRO = attachmentSystem.createRegistry(
            Creeper.class,
            "test",
            (pl, id, c, p) -> new CreeperImpl(pl, id, c),
            Utils::spawnEntity
    );
    public static RegistryObject<Player, Void> playerRO = attachmentSystem.createRegistry(
            Player.class,
            "test2",
            (pl, id, c, p) -> new PlayerImpl(pl, id, c)
    );

    public static void init() {
        attachmentSystem.init();
    }

}
