package de.allycraft.minestom.ui;

import de.allycraft.minestom.ui.button.Button;
import de.allycraft.minestom.ui.button.ButtonArea;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PagedMenu extends InvMenu {
    public static final ItemStack DEFAULT_PREVIOUS_PAGE_ITEM = ItemStack.builder(Material.ARROW)
            .customName(Component.text("Previous Page", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
            .customModelData(List.of(), List.of(), List.of("button_previous_page"), List.of())
            .build();

    public static final ItemStack DEFAULT_NEXT_PAGE_ITEM = ItemStack.builder(Material.ARROW)
            .customName(Component.text("Next Page", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
            .customModelData(List.of(), List.of(), List.of("button_next_page"), List.of())
            .build();

    private final List<Integer> pageSlots;
    private List<Button> buttons = List.of(); // TODO rename?
    private int pageCount = 1;
    private int currentPage = 0;

    public PagedMenu(@NotNull Player player, InventoryType inventoryType, Component title, ButtonArea pageArea) {
        super(player, inventoryType, title);
        this.pageSlots = pageArea.getButtonPositions(this.getWidth(), this.getHeight());
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
        this.pageCount = Math.max(1, Math.ceilDiv(this.buttons.size(), this.pageSlots.size()));

        if(this.currentPage >= this.pageCount) {
            this.changePage(this.pageCount - 1);
        } else if(this.isOpen()) {
            this.render();
        }
    }

    @Override
    public void render() {
        int offset = this.currentPage * this.pageSlots.size();
        for(int i = 0; i < this.pageSlots.size(); i++) {
            if(offset + i < this.buttons.size()) {
                this.add(this.pageSlots.get(i), this.buttons.get(i + offset));
            } else {
                this.add(this.pageSlots.get(i), this.getFiller());
            }
        }
        super.render();
    }

    protected void changePage(int page) {
        if(page < 0 || page >= this.pageCount) return;
        this.currentPage = page;
        if(this.isOpen()) {
            this.render();
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isFirstPage() {
        return this.currentPage == 0;
    }

    public boolean isLastPage() {
        return this.currentPage >= this.pageCount - 1;
    }

    public Button createPreviousPageButton() {
        return new Button() {
            @Override
            public @NotNull ItemStack getItem(InvMenu menu) {
                if(PagedMenu.this.isFirstPage()) return menu.getFillerItem();
                return PagedMenu.DEFAULT_PREVIOUS_PAGE_ITEM;
            }

            @Override
            public void onClick(InvMenu menu, Click click) {
                PagedMenu.this.changePage(PagedMenu.this.currentPage - 1);
            }
        };
    }

    public Button createNextPageButton() {
        return new Button() {
            @Override
            public @NotNull ItemStack getItem(InvMenu menu) {
                if(PagedMenu.this.isLastPage()) return menu.getFillerItem();
                return PagedMenu.DEFAULT_NEXT_PAGE_ITEM;
            }

            @Override
            public void onClick(InvMenu menu, Click click) {
                PagedMenu.this.changePage(PagedMenu.this.currentPage + 1);
            }
        };
    }
}
