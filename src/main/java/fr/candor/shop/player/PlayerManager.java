package fr.candor.shop.player;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import fr.candor.shop.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PlayerManager extends Module {

    private final ExecutorService offlinePool = Executors.newCachedThreadPool();
    private final AsyncLoadingCache<UUID, PlayerData> playerCache = Caffeine.newBuilder()
            .expireAfter(new PlayerExpiration())
            .buildAsync(PlayerData::new); // TODO: Run database load

    public PlayerManager() {
        this.plugin.getServer().getOnlinePlayers().forEach(player -> this.playerCache.get(player.getUniqueId()));
        this.listener(new PlayerListener(this));
    }

    public AsyncLoadingCache<UUID, PlayerData> getPlayerCache() {
        return playerCache;
    }

    public void getOffline(String playerName, BiConsumer<OfflinePlayer, PlayerData> consumer) {
        this.getOffline(playerName, consumer, null);
    }

    @SuppressWarnings("deprecation")
    public void getOffline(String playerName, BiConsumer<OfflinePlayer, PlayerData> found, Consumer<OfflinePlayer> notFound) {
        this.offlinePool.execute(() -> {
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
