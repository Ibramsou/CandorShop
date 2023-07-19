package fr.candor.shop.storage;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// TODO: Use a cache to deserialize only requested items
public class ItemByteSerializer {

    public static byte[] serialize(ItemStack itemStack) {
        try (ByteArrayOutputStream array = new ByteArrayOutputStream()) {
            try (BukkitObjectOutputStream output = new BukkitObjectOutputStream(array)) {
                output.writeObject(itemStack);
                return array.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ItemStack deserialize(byte[] bytes) {
        try (BukkitObjectInputStream input = new BukkitObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (ItemStack) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
