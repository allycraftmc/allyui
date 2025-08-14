package de.allycraft.minestom.ui;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerAnvilInputEvent;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.inventory.type.AnvilInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class AnvilMenu extends Menu {
    private final AnvilInventory anvil;
    private @NotNull String input = "";
    private final Predicate<String> validator;
    private final Consumer<String> callback;

    public AnvilMenu(@NotNull Player player, Component title, Predicate<String> validator, Consumer<String> callback) {
        super(player);
        this.anvil = new AnvilInventory(title);
        this.anvil.setTag(Menu.MENU_TAG, this);
        this.validator = validator;
        this.callback = callback;

        this.anvil.setItemStack(
                0,
                ItemStack.builder(Material.NAME_TAG)
                        .customName(Component.empty())
                        .build()
        );
    }

    protected void onInventoryPreClickEvent(InventoryPreClickEvent event) {
        if(event.getSlot() == 2) {
            if(this.validator.test(this.input)) {
                this.callback.accept(this.input);

                if(this.getParent() != null) {
                    this.getParent().open();
                } else {
                    this.close();
                }
            }
        }

        event.setCancelled(true);
    }

    protected void onPlayerInventoryPreClickEvent(InventoryPreClickEvent event) {
        if(!this.isOpen()) return;
        boolean cancel = switch (event.getClick()) {
            case Click.Double ignored -> true;
            case Click.LeftShift ignored -> true;
            case Click.RightShift ignored -> true;
            default -> false;
        };

        event.setCancelled(cancel);
    }

    protected void onInputEvent(PlayerAnvilInputEvent event) {
        this.input = event.getInput();
        this.anvil.setRepairCost((short) 0);
    }

    public @NotNull String getInput() {
        return input;
    }

    @Override
    public void open() {
        this.player.openInventory(anvil);
    }

    private boolean isOpen() {
        return this.anvil.equals(this.player.getOpenInventory());
    }

    @Override
    public void close() {
        if(this.isOpen()) {
            this.player.closeInventory();
        }
    }

    @Override
    protected void onOpen() {
        this.player.playSound(Menu.OPEN_SOUND);
    }

    @Override
    protected void onClose() {
    }
}
