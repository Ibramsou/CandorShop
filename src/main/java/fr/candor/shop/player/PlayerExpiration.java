package fr.candor.shop.player;

import com.github.benmanes.caffeine.cache.Expiry;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerExpiration implements Expiry<UUID, PlayerData> {
    
    @Override
    public long expireAfterCreate(@Nonnull UUID key, @Nonnull PlayerData value, long currentTime) {
        return currentTime;
    }

    @Override
    public long expireAfterUpdate(@Nonnull UUID key, @Nonnull PlayerData value, long currentTime, long currentDuration) {
        return value.isOnline() ? currentTime : TimeUnit.SECONDS.toNanos(30);
    }

    @Override
    public long expireAfterRead(@Nonnull UUID key, @Nonnull PlayerData value, long currentTime, long currentDuration) {
        return value.isOnline() ? currentTime : TimeUnit.MINUTES.toNanos(1);
    }
}
