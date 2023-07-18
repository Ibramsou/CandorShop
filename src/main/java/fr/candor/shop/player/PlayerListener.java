package fr.candor.shop.player;

import fr.candor.shop.listener.ModuleListener;
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
        PlayerData data = this.module.getPlayerCache().get(player.getUniqueId());
        if (data == null) {
            player.kickPlayer(Component.text("An error occurred while loading your data").color(NamedTextColor.RED).content());
            return;
        }

        data.setPlayer(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        System.out.println(event.getPlayer().isOnline());
        this.module.getPlayerCache().get(event.getPlayer().getUniqueId());
    }
}
