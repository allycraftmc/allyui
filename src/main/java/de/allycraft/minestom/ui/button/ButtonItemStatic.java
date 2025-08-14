package de.allycraft.minestom.ui.button;

import de.allycraft.minestom.ui.InvMenu;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ButtonItemStatic extends Button {
    private final @NotNull ItemStack item;

    public ButtonItemStatic(@NotNull InvMenu menu, @NotNull ItemStack item) {
        super(menu);
        this.item = item;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return this.item;
    }
}
