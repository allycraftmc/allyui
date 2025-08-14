package de.allycraft.minestom.ui.demo;

import de.allycraft.minestom.ui.Menu;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class DemoMenuCommand extends Command {
    public DemoMenuCommand() {
        super("menu");

        setCondition((sender, command) -> sender instanceof Player);
        setDefaultExecutor((sender, context) -> {
            if(!(sender instanceof Player player)) return;
            Menu menu = new DemoMenu(player);
            menu.open();
        });
    }
}
