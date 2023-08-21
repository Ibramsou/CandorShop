package fr.candor.shop.player;

import fr.candor.shop.ShopPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class PlayerData {

    private final ShopPlugin plugin;
    private final UUID uniqueId;
    private double balance;
    private Player player;
    private boolean saving;
    private final Lock lock = new ReentrantLock();

    public PlayerData(ShopPlugin plugin, UUID uniqueId) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
    }

    public boolean isSaving() {
        return saving;
    }

    public void setSaving(boolean saving) {
        this.saving = saving;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setBalance(double balance) {
        setBalance(balance, true);
    }

    public void setBalance(double balance, boolean save) {
        this.balance = balance;
        if (save) {
            this.plugin.getPlayerManager().saveData(this);
        }
    }

    public void modifyBalance(Function<Double, Double> difference) {
        modifyBalance(difference, true);
    }

    public void modifyBalance(Function<Double, Double> difference, boolean save) {
        setBalance(difference.apply(this.balance), save);
    }

    // TODO: Add parsed balance amount method
    public double getBalance() {
        return balance;
    }

    public boolean isOnline() {
        return this.player != null && this.player.isOnline();
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(@Nonnull Player player) {
        this.player = player;
    }

    public Lock getLock() {
        return lock;
    }
}
