package fr.candor.shop.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Function;

public class PlayerData {

    private final UUID uniqueId;
    private double balance;
    private Player player;

    public PlayerData(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.player = Bukkit.getPlayer(uniqueId);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void modifyBalance(Function<Double, Double> difference) {
        this.balance = difference.apply(this.balance);
    }

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
}
