package org.mangorage.paperdev.core.impl;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.mangorage.paperdev.core.impl.block.BlockAttachment;

import static org.mangorage.paperdev.core.Utils.reached;

public class SimpleBlockImpl extends BlockAttachment<Block> {
    public SimpleBlockImpl(Plugin plugin, NamespacedKey id, Block object) {
        super(plugin, id, object);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void tick() {
        if (reached(getTicks(), 10)) {
            var location = getObject().getLocation().add(0, 1, 0);
            if (location.getBlock().getBlockData().getMaterial() != Material.COBBLED_DEEPSLATE) {
                location.getWorld().setBlockData(location, Material.COBBLED_DEEPSLATE.createBlockData());
            }
        }
    }
}
