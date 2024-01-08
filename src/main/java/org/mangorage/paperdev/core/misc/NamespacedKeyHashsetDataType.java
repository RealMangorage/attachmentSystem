package org.mangorage.paperdev.core.misc;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

public class NamespacedKeyHashsetDataType implements PersistentDataType<String, HashSet<NamespacedKey>> {

    public static final NamespacedKeyHashsetDataType TYPE = new NamespacedKeyHashsetDataType();

    private NamespacedKeyHashsetDataType() {
    }

    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public Class<HashSet<NamespacedKey>> getComplexType() {
        return (Class<HashSet<NamespacedKey>>) (Object) HashSet.class;
    }

    @Override
    public String toPrimitive(HashSet<NamespacedKey> complex, PersistentDataAdapterContext context) {
        StringBuilder stringBuilder = new StringBuilder();

        for (NamespacedKey key : complex) {
            stringBuilder.append(key.toString()).append(",");
        }

        return stringBuilder.toString();
    }

    @Override
    public HashSet<NamespacedKey> fromPrimitive(String primitive, PersistentDataAdapterContext context) {
        String[] keyStrings = primitive.split(",");
        HashSet<NamespacedKey> keys = new HashSet<>();

        for (String keyString : keyStrings) {
            keys.add(NamespacedKey.fromString(keyString));
        }

        return keys;
    }
}

