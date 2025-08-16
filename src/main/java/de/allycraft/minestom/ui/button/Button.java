package de.allycraft.minestom.ui.button;

import de.allycraft.minestom.ui.InvMenu;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Button {
    @NotNull ItemStack getItem(InvMenu menu);

    default void onClick(InvMenu menu, Click click) {

    };
}
