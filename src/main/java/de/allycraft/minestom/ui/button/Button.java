package de.allycraft.minestom.ui.button;

import de.allycraft.minestom.ui.InvMenu;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class Button {
    protected final @NotNull InvMenu menu;

    public Button(@NotNull InvMenu menu) {
        this.menu = menu;
    }

    public abstract @NotNull ItemStack getItem();

    public void onClick(Click click) {

    }
}
