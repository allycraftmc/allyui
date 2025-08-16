package de.allycraft.minestom.ui;

import de.allycraft.minestom.ui.button.Button;
import de.allycraft.minestom.ui.button.ButtonArea;
import de.allycraft.minestom.ui.button.ButtonItemStatic;
import de.allycraft.minestom.ui.button.ButtonPosition;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class InvMenu extends Menu {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvMenu.class);
    public static final ItemStack DEFAULT_FILLER_ITEM = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
            .customModelData(List.of(), List.of(), List.of("empty_slot"), List.of())
            .set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(true, Set.of()))
            .build();

    public static final ItemStack DEFAULT_BACK_ITEM = ItemStack.builder(Material.ARROW)
            .customName(Component.text("Back", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
            .customModelData(List.of(), List.of(), List.of("button_back"), List.of())
            .build();

    public static final ItemStack DEFAULT_CLOSE_ITEM = ItemStack.builder(Material.BARRIER)
            .customName(Component.text("Close", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
            .customModelData(List.of(), List.of(), List.of("button_close"), List.of())
            .build();

    private final Inventory inventory;
    private final Map<Integer, Button> buttons;
    private final Button filler;

    public InvMenu(@NotNull Player player, InventoryType inventoryType, Component title) {
        super(player);
        this.inventory = new Inventory(inventoryType, title);
        this.inventory.setTag(Menu.MENU_TAG, this);
        this.buttons = new HashMap<>();
        this.filler = new ButtonItemStatic(this, DEFAULT_FILLER_ITEM);
    }

    protected final void add(ButtonPosition position, Button button) {
        int slot = position.getSlot(this.getWidth(), this.getHeight());
        if(slot < 0 || slot >= this.getSize()) {
            LOGGER.warn("Added button to invalid slot {} into a menu of size {}", slot, this.getSize());
        }
        this.buttons.put(slot, button);
    }

    protected final void add(ButtonPosition position, Function<InvMenu, Button> buttonConstructor) {
        this.add(position, buttonConstructor.apply(this));
    }

    protected final void add(int slot, Button button) {
        this.add(ButtonPosition.of(slot), button);
    }

    protected final void add(int slot, Function<InvMenu, Button> buttonConstructor) {
        this.add(ButtonPosition.of(slot), buttonConstructor);
    }

    protected final void add(int row, int column, Button button) {
        this.add(ButtonPosition.of(row, column), button);
    }

    protected final void add(int row, int column, Function<InvMenu, Button> buttonConstructor) {
        this.add(ButtonPosition.of(row, column), buttonConstructor);
    }

    protected final void fill(ButtonArea buttonArea, Button button) {
        for(int slot : buttonArea.getButtonPositions(this.getWidth(), this.getHeight())) {
            this.add(slot, button);
        }
    }

    protected final void fill(ButtonArea buttonArea, Function<InvMenu, Button> buttonConstructor) {
        this.fill(buttonArea, buttonConstructor.apply(this));
    }

    protected final void fill(ButtonArea buttonArea, BiFunction<Integer, Integer, Button> buttonConstructor) {
        int i = 0;
        List<Integer> slots = buttonArea.getButtonPositions(this.getWidth(), this.getHeight());
        for(int slot : slots) {
            this.add(slot, buttonConstructor.apply(i, slots.size()));
            i++;
        }
    }

    private @NotNull Button getButtonAt(int slot) {
        Button button = this.buttons.get(slot);
        if(button == null) return this.filler;
        return button;
    }

    private @NotNull ItemStack getItemAt(int slot) {
        return this.getButtonAt(slot).getItem();
    }

    public void render() {
        boolean changed = false;
        for(int slot = 0; slot < this.inventory.getSize(); slot++) {
            ItemStack item = this.getItemAt(slot);
            if(item.equals(this.inventory.getItemStack(slot))) continue;

            this.inventory.setItemStack(slot, item, false);
            changed = true;
        }

        if(changed) {
            this.inventory.update();
        }
    }

    protected void onInventoryPreClickEvent(InventoryPreClickEvent event) {
        int slot = event.getSlot();
        if(slot >= 0 && slot < this.getSize()) {
            Button button = this.getButtonAt(slot);
            button.onClick(event.getClick());
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

    protected boolean isOpen() {
        return this.inventory.equals(this.player.getOpenInventory());
    }

    @Override
    public void open() {
        this.render();
        this.player.openInventory(this.inventory);
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

    public final @NotNull Player getPlayer() {
        return player;
    }

    public int getSize() {
        return this.inventory.getSize();
    }

    public int getWidth() {
        return switch (this.inventory.getInventoryType()) {
            case CHEST_1_ROW, CHEST_2_ROW, CHEST_3_ROW, CHEST_4_ROW, CHEST_5_ROW, CHEST_6_ROW, SHULKER_BOX -> 9;
            case WINDOW_3X3, CRAFTER_3X3 -> 3;
            default -> this.inventory.getSize();
        };
    }

    public int getHeight() {
        return switch (this.inventory.getInventoryType()) {
            case CHEST_2_ROW -> 2;
            case CHEST_3_ROW, SHULKER_BOX, WINDOW_3X3, CRAFTER_3X3 -> 3;
            case CHEST_4_ROW -> 4;
            case CHEST_5_ROW -> 5;
            case CHEST_6_ROW -> 6;
            default -> 1;
        };
    }

    public Button getFiller() {
        return this.filler;
    }

    public ItemStack getFillerItem() {
        return this.filler.getItem();
    }
}
