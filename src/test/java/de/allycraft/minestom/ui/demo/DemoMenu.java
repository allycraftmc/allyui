package de.allycraft.minestom.ui.demo;

import de.allycraft.minestom.ui.AnvilMenu;
import de.allycraft.minestom.ui.InvMenu;
import de.allycraft.minestom.ui.PagedMenu;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DemoMenu extends InvMenu {
    public DemoMenu(@NotNull Player player) {
        super(player, InventoryType.CHEST_4_ROW, Component.text("Demo Menu"));

        this.add(ButtonPosition.offset(ButtonPosition.centerOfRow(0), -1), new ButtonItemStatic(ItemStack.builder(Material.ITEM_FRAME)
                .customName(Component.text("TODO", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                .build()));
        this.add(ButtonPosition.offset(ButtonPosition.centerOfRow(0), 1), new  ButtonItemStatic(ItemStack.of(Material.AIR)));
        this.add(ButtonPosition.offset(ButtonPosition.centerOfRow(1), -2), new CounterButton());
        this.add(ButtonPosition.centerOfRow(1), new ButtonItemDynamic(() ->
                ItemStack.builder(Material.OAK_HANGING_SIGN)
                        .customName(Component.text("Time").decoration(TextDecoration.ITALIC, false))
                        .lore(Component.text("Millis: " + System.currentTimeMillis()).decoration(TextDecoration.ITALIC, false))
                        .build()
        ));
        this.add(ButtonPosition.offset(ButtonPosition.centerOfRow(1), 2), new ButtonMenuOpen(PreferenceMenu::new, ItemStack.builder(Material.COMPARATOR)
                .customName(Component.text("Preferences", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                .build()));

        Predicate<String> nameValidator = name -> {
            if(name.length() >= 3) return true;
            this.player.sendMessage(Component.text("Name must be at least 3 characters long", NamedTextColor.RED));
            return false;
        };
        this.add(ButtonPosition.offset(ButtonPosition.centerOfRow(2), -1), new ButtonMenuOpen(() -> new AnvilMenu(
                this.player,
                Component.text("Enter name:"),
                nameValidator,
                name -> this.getPlayer().sendMessage(Component.text("Name: " + name)),
                true
        ), ItemStack.of(Material.ANVIL)));

        this.add(ButtonPosition.offset(ButtonPosition.centerOfRow(2), 1), new ButtonItemStatic(ItemStack.builder(Material.CHERRY_HANGING_SIGN)
                .customName(Component.text("TODO", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
                .build()));

        this.add(ButtonPosition.centerOfRow(3), new ButtonMenuOpen(LibraryMenu::new, ItemStack.builder(Material.BOOKSHELF)
                .customName(Component.text("Library", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                .build()));

        this.add(ButtonPosition.last(), new ButtonMenuClose(InvMenu.DEFAULT_CLOSE_ITEM));
    }

    static class CounterButton implements Button {
        private int count = 0;

        @Override
        public @NotNull ItemStack getItem(InvMenu menu) {
            return ItemStack.builder(Material.COMMAND_BLOCK)
                    .customName(Component.text("Click Me", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                    .lore(Component.text("Count: ").append(Component.text(this.count)).decoration(TextDecoration.ITALIC, false))
                    .build();
        }

        @Override
        public void onClick(InvMenu menu, Click click) {
            if(click instanceof Click.Left) {
                this.count++;
            } else if(click instanceof Click.Right) {
                this.count--;
            }
            menu.render();
        }
    }

    static class PreferenceMenu extends InvMenu {
        public PreferenceMenu(@NotNull Player player) {
            super(player, InventoryType.CHEST_6_ROW, Component.text("Preferences", NamedTextColor.GREEN));

            this.add(ButtonPosition.centerOfRow(0), new ButtonItemStatic(ItemStack.builder(Material.OAK_HANGING_SIGN)
                    .customName(Component.text("This is a preference menu", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                    .build()));

            this.fill(
                    ButtonArea.rect(
                            ButtonPosition.next(ButtonPosition.firstOfRow(1)),
                            ButtonPosition.previous(ButtonPosition.lastOfRow(2))
                    ),
                    new ButtonItemStatic(ItemStack.of(Material.DIRT))
            );

            this.fill(
                    ButtonArea.rect(
                            ButtonPosition.offset(ButtonPosition.firstOfRow(4), 2),
                            ButtonPosition.offset(ButtonPosition.lastOfRow(5), -2)
                    ),
                    (i, count) -> new ButtonItemStatic(ItemStack.of(Material.DIAMOND).withAmount(i + 1))
            );

            this.add(ButtonPosition.last(), new ButtonMenuBack(InvMenu.DEFAULT_BACK_ITEM));
        }
    }

    static class LibraryMenu extends PagedMenu {
        public LibraryMenu(@NotNull Player player) {
            super(player, InventoryType.CHEST_3_ROW, Component.text("Library"), ButtonArea.rect(
                    ButtonPosition.next(ButtonPosition.firstOfRow(0)),
                    ButtonPosition.previous(ButtonPosition.lastOfRow(1))
            ));

            List<Button> bookButtons = new ArrayList<>();
            for(int i = 0; i < 50; i++) {
                bookButtons.add(new ButtonItemStatic(ItemStack.builder(Material.BOOK)
                        .customName(Component.text("Book " + (i + 1)).decoration(TextDecoration.ITALIC, false))
                        .glowing(i % 3 == 0)
                        .build()));
            }
            this.setButtons(bookButtons);

            this.add(ButtonPosition.previous(ButtonPosition.centerOfRow(2)), this.createPreviousPageButton());
            this.add(ButtonPosition.next(ButtonPosition.centerOfRow(2)), this.createNextPageButton());
            this.add(ButtonPosition.last(), new ButtonMenuBack(InvMenu.DEFAULT_BACK_ITEM));
        }
    }
}
