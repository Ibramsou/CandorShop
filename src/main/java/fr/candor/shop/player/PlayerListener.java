package fr.candor.shop.player;

import fr.candor.shop.module.ModuleListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener extends ModuleListener<PlayerManager> {

    public PlayerListener(PlayerManager manager) {
        super(manager);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.module.getPlayerCache().get(player.getUniqueId()).thenAccept(data -> {
            data.setPlayer(player);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // Delay on next tick to tell that the player is being offline on the cache
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin,
                () -> this.module.getPlayerCache().getIfPresent(event.getPlayer().getUniqueId()), 1L);
    }
}
