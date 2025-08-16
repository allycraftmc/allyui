package de.allycraft.minestom.ui.button;

import de.allycraft.minestom.ui.InvMenu;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ButtonMenuClose implements Button {
    private final @NotNull ItemStack item;

    public ButtonMenuClose(@NotNull ItemStack item) {
        this.item = item;
    }

    @Override
    public @NotNull ItemStack getItem(InvMenu menu) {
        return this.item;
    }

    @Override
    public void onClick(InvMenu menu, Click click) {
        menu.close();
    }
}
