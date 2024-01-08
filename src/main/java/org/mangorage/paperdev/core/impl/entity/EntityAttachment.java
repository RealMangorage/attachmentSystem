package org.mangorage.paperdev.core.impl.entity;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.mangorage.paperdev.core.attachment.AttachmentSystem;
import org.mangorage.paperdev.core.attachment.DetachReason;
import org.mangorage.paperdev.core.impl.Attachment;
import org.mangorage.paperdev.core.misc.NamespacedKeyHashsetDataType;

import java.util.HashSet;

public abstract class EntityAttachment<T extends Entity> extends Attachment<T> {

    public EntityAttachment(Plugin plugin, NamespacedKey id, T object) {
        super(plugin, id, object);
    }

    @Override
    public boolean isValid() {
        return getObject() != null && getObject().isValid();
    }

    @Override
    public void onRemove(DetachReason reason) {
        var ob = getObject();
        if (ob != null) {
            var data = ob.getPersistentDataContainer();
            if (data.has(AttachmentSystem.getInstance().getAttachmentsTag())) {
                var attachments = data.get(AttachmentSystem.getInstance().getAttachmentsTag(), NamespacedKeyHashsetDataType.TYPE);
                if (attachments != null) {
                    attachments.remove(getID());
                    data.set(AttachmentSystem.getInstance().getAttachmentsTag(), NamespacedKeyHashsetDataType.TYPE, attachments);
                }
            }
        }
        super.onRemove(reason);
    }

    @Override
    public void save() {
        var o = getObject();
        // Handle saving attachment data!
        if (o != null) {
            var data = o.getPersistentDataContainer();
            if (data.has(AttachmentSystem.getInstance().getAttachmentsTag())) {
                var list = data.get(AttachmentSystem.getInstance().getAttachmentsTag(), NamespacedKeyHashsetDataType.TYPE);
                if (list != null) list.add(getID());
            } else {
                HashSet<NamespacedKey> list = new HashSet<>();
                list.add(getID());
                data.set(AttachmentSystem.getInstance().getAttachmentsTag(), NamespacedKeyHashsetDataType.TYPE, list);
            }
        }
    }
}
