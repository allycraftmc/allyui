package de.allycraft.minestom.ui.demo;

import de.allycraft.minestom.ui.AnvilMenu;
import de.allycraft.minestom.ui.InvMenu;
import de.allycraft.minestom.ui.button.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class DemoMenu extends InvMenu {
    public DemoMenu(@NotNull Player player) {
        super(player, InventoryType.CHEST_4_ROW, Component.text("Demo Menu"));

        this.add(ButtonPosition.offset(ButtonPosition.rowCenter(0), -1), new ButtonItemStatic(this, ItemStack.builder(Material.ITEM_FRAME)
                .customName(Component.text("TODO", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                .build()));
        this.add(ButtonPosition.offset(ButtonPosition.rowCenter(0), 1), new  ButtonItemStatic(this, ItemStack.of(Material.AIR)));
        this.add(ButtonPosition.offset(ButtonPosition.rowCenter(1), -2), CounterButton::new);
        this.add(ButtonPosition.rowCenter(1), new ButtonItemDynamic(this, () ->
                ItemStack.builder(Material.OAK_HANGING_SIGN)
                        .customName(Component.text("Time").decoration(TextDecoration.ITALIC, false))
                        .lore(Component.text("Millis: " + System.currentTimeMillis()).decoration(TextDecoration.ITALIC, false))
                        .build()
        ));
        this.add(ButtonPosition.offset(ButtonPosition.rowCenter(1), 2), new ButtonMenuOpen(this, PreferenceMenu::new, ItemStack.builder(Material.COMPARATOR)
                .customName(Component.text("Preferences", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                .build()));

        Predicate<String> nameValidator = name -> {
            if(name.length() >= 3) return true;
            this.player.sendMessage(Component.text("Name must be at least 3 characters long", NamedTextColor.RED));
            return false;
        };
        this.add(ButtonPosition.offset(ButtonPosition.rowCenter(2), -1), new ButtonMenuOpen(this, () -> new AnvilMenu(
                this.player,
                Component.text("Enter name:"),
                nameValidator,
                name -> this.getPlayer().sendMessage(Component.text("Name: " + name))
        ), ItemStack.of(Material.ANVIL)));

        this.add(ButtonPosition.offset(ButtonPosition.rowCenter(2), 1), new ButtonItemStatic(this, ItemStack.builder(Material.CHERRY_HANGING_SIGN)
                .customName(Component.text("TODO", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                .build()));

        this.add(ButtonPosition.rowCenter(3), new ButtonItemStatic(this, ItemStack.builder(Material.BOOKSHELF)
                .customName(Component.text("TODO", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                .build()));

        this.add(ButtonPosition.last(), new ButtonMenuClose(this, InvMenu.DEFAULT_CLOSE_ITEM));
    }

    static class CounterButton extends Button {
        private int count = 0;

        protected CounterButton(InvMenu menu) {
            super(menu);
        }

        @Override
        public @NotNull ItemStack getItem() {
            return ItemStack.builder(Material.COMMAND_BLOCK)
                    .customName(Component.text("Click Me", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                    .lore(Component.text("Count: ").append(Component.text(this.count)).decoration(TextDecoration.ITALIC, false))
                    .build();
        }

        @Override
        public void onClick(Click click) {
            if(click instanceof Click.Left) {
                this.count++;
            } else if(click instanceof Click.Right) {
                this.count--;
            }
            this.menu.render();
        }
    }

    static class PreferenceMenu extends InvMenu {
        public PreferenceMenu(@NotNull Player player) {
            super(player, InventoryType.CHEST_2_ROW, Component.text("Preferences", NamedTextColor.GREEN));

            this.add(ButtonPosition.rowCenter(0), new ButtonItemStatic(this, ItemStack.builder(Material.OAK_HANGING_SIGN)
                    .customName(Component.text("This is a preference menu", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                    .build()));

            this.add(ButtonPosition.last(), new ButtonMenuBack(this, InvMenu.DEFAULT_BACK_ITEM));
        }
    }
}
