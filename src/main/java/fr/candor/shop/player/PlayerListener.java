package fr.candor.shop.player;

import fr.candor.shop.module.ModuleListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
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
        PlayerData data = this.module.getPlayerCache().get(player.getUniqueId());
        if (data == null) {
            player.kickPlayer(ChatColor.RED + "Could not load your data, please reconnect");
            return;
        }

        data.setPlayer(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // Delay on next tick to tell that the player is being offline on the cache
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin,
                () -> this.module.getPlayerCache().getIfPresent(event.getPlayer().getUniqueId()), 1L);
    }
}
