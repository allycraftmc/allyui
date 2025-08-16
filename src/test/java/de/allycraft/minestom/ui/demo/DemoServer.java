package de.allycraft.minestom.ui.demo;

import de.allycraft.minestom.ui.AllyUIMinestom;
import de.allycraft.minestom.ui.AnvilMenu;
import de.allycraft.minestom.ui.InvMenu;
import de.allycraft.minestom.ui.PagedMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

public class DemoServer {
    public static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();

        MojangAuth.init();

        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkSupplier(LightingChunk::new);
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 1, Block.GRASS_BLOCK));

        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(instance);

            Player player = event.getPlayer();
            player.setRespawnPoint(new Pos(0, 1, 0));
            player.setGameMode(GameMode.CREATIVE);

            player.getInventory().addItemStack(InvMenu.DEFAULT_FILLER_ITEM);
            player.getInventory().addItemStack(InvMenu.DEFAULT_CLOSE_ITEM);
            player.getInventory().addItemStack(InvMenu.DEFAULT_BACK_ITEM);
            player.getInventory().addItemStack(PagedMenu.DEFAULT_PREVIOUS_PAGE_ITEM);
            player.getInventory().addItemStack(PagedMenu.DEFAULT_NEXT_PAGE_ITEM);
        });

        AllyUIMinestom.register(MinecraftServer.getGlobalEventHandler());

        CommandManager commandManager = MinecraftServer.getCommandManager();
        commandManager.register(new DemoMenuCommand());
        commandManager.setUnknownCommandCallback((sender, command) -> sender.sendMessage(Component.text("Unknown command", NamedTextColor.RED)));

        minecraftServer.start("0.0.0.0", 25565);
    }
}
