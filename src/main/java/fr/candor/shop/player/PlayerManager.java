package fr.candor.shop.player;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import fr.candor.shop.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

public class PlayerManager extends Module {

    private static final ExecutorService OFFLINE_POOL = Executors.newCachedThreadPool();
    private static final Pattern PLAYER_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,16}$");

    private final LoadingCache<UUID, PlayerData> playerCache = Caffeine.newBuilder()
            .expireAfter(new PlayerExpiration())
            .build(this::queueLoad);

    public PlayerManager() {
        this.plugin.getServer().getOnlinePlayers().forEach(this::loadData);
        this.listener(new PlayerListener(this));
    }

    public void loadData(Player player) {
        PlayerData data = this.playerCache.get(player.getUniqueId());
        if (data == null) {
            player.sendMessage("§cCould not load your data, please reconnect.");
            return;
        }
        data.setPlayer(player);
    }

    private PlayerData queueLoad(UUID uuid) {
        PlayerData data = new PlayerData(this.plugin, uuid);
        this.plugin.getDatabase().getLoadQueue().add(data);
        return data;
    }

    public LoadingCache<UUID, PlayerData> getPlayerCache() {
        return playerCache;
    }

    public void saveData(PlayerData data) {
        if (data.isSaving()) return;
        data.setSaving(true);
        this.plugin.getDatabase().getSaveQueue().add(data);
    }

    private PlayerData directLoad(UUID uuid) {
        return this.plugin.getDatabase().loadUser(uuid);
    }

    public void modifyBalance(UUID uniqueId, Function<Double, Double> difference) {
        OFFLINE_POOL.execute(() -> {
            PlayerData data = this.playerCache.get(uniqueId, this::directLoad);
            if (data == null) return;
            data.modifyBalance(difference);
        });
    }

    public void getByName(@Nonnull String playerName, @Nonnull BiConsumer<OfflinePlayer, PlayerData> consumer) {
        this.getByName(playerName, consumer, null, null);
    }

    public void getByName(@Nonnull String playerName, @Nonnull BiConsumer<OfflinePlayer, PlayerData> found, @Nullable Consumer<OfflinePlayer> notFound) {
        this.getByName(playerName, found, notFound, null);
    }

    @SuppressWarnings("deprecation")
    public void getByName(@Nonnull String playerName, @Nonnull BiConsumer<OfflinePlayer, PlayerData> found, @Nullable Consumer<OfflinePlayer> notFound, @Nullable Runnable invalidName) {
        if (invalidName != null && !PLAYER_NAME_PATTERN.matcher(playerName).matches()) {
            invalidName.run();
            return;
        }

        OFFLINE_POOL.execute(() -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
            if (player.hasPlayedBefore() || player.isOnline()) {
                PlayerData data = this.playerCache.get(player.getUniqueId(), this::directLoad);
                if (data == null) {
                    if (notFound != null) notFound.accept(player);
                    return;
                }

                found.accept(player, data);
            } else {
                if (notFound != null) {
                    notFound.accept(player);
                }
            }
        });
    }
}
