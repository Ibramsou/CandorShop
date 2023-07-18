package fr.candor.shop.player;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import fr.candor.shop.module.Module;

import java.util.UUID;

public class PlayerManager extends Module {

    private final LoadingCache<UUID, PlayerData> playerCache = Caffeine.newBuilder()
            .expireAfter(new PlayerExpiration())
            .build(PlayerData::new);

    public PlayerManager() {
        this.plugin.getServer().getOnlinePlayers().forEach(player -> this.playerCache.get(player.getUniqueId()));
        this.listener(new PlayerListener(this));
    }

    public LoadingCache<UUID, PlayerData> getPlayerCache() {
        return playerCache;
    }
}
