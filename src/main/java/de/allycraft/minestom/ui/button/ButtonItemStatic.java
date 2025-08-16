package de.allycraft.minestom.ui.button;

import de.allycraft.minestom.ui.InvMenu;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ButtonItemStatic implements Button {
    private final @NotNull ItemStack item;

    public ButtonItemStatic(@NotNull ItemStack item) {
        this.item = item;
    }

    @Override
    public @NotNull ItemStack getItem(InvMenu menu) {
        return this.item;
    }
}
