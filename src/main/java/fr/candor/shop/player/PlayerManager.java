package fr.candor.shop.player;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import fr.candor.shop.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class PlayerManager extends Module {

    private static final ExecutorService OFFLINE_POOL = Executors.newCachedThreadPool();
    private static final Pattern PLAYER_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,16}$");

    private final AsyncLoadingCache<UUID, PlayerData> playerCache = Caffeine.newBuilder()
            .expireAfter(new PlayerExpiration())
            .buildAsync(this::queueLoad); // TODO: Run database load

    public PlayerManager() {
        this.plugin.getServer().getOnlinePlayers().forEach(player -> this.playerCache.get(player.getUniqueId()));
        this.listener(new PlayerListener(this));
    }

    private PlayerData queueLoad(UUID uuid) {
        PlayerData data = new PlayerData(this.plugin, uuid);
        this.plugin.getDatabase().getLoadQueue().add(data);
        return data;
    }

    public AsyncLoadingCache<UUID, PlayerData> getPlayerCache() {
        return playerCache;
    }

    public void saveData(PlayerData data) {
        if (data.isSaving()) return;
        data.setSaving(true);
        this.plugin.getDatabase().getSaveQueue().add(data);
    }

    public void getOffline(@Nonnull String playerName, @Nonnull BiConsumer<OfflinePlayer, PlayerData> consumer) {
        this.getOffline(playerName, consumer, null, null);
    }

    public void getOffline(@Nonnull String playerName, @Nonnull BiConsumer<OfflinePlayer, PlayerData> found, @Nullable Consumer<OfflinePlayer> notFound) {
        this.getOffline(playerName, found, notFound, null);
    }

    @SuppressWarnings("deprecation")
    public void getOffline(@Nonnull String playerName, @Nonnull BiConsumer<OfflinePlayer, PlayerData> found, @Nullable Consumer<OfflinePlayer> notFound, @Nullable Runnable invalidName) {
        if (invalidName != null && !PLAYER_NAME_PATTERN.matcher(playerName).matches()) {
            invalidName.run();
            return;
        }

        OFFLINE_POOL.execute(() -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
            if (player.hasPlayedBefore() || player.isOnline()) {
                PlayerData data = this.playerCache.get(player.getUniqueId()).join();
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
