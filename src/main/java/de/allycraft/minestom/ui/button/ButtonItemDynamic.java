package de.allycraft.minestom.ui.button;

import de.allycraft.minestom.ui.InvMenu;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ButtonItemDynamic implements Button {
    private final Supplier<ItemStack> itemSupplier;

    public ButtonItemDynamic(Supplier<ItemStack> itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    @Override
    public @NotNull ItemStack getItem(InvMenu menu) {
        return this.itemSupplier.get();
    }

    @Override
    public void onClick(InvMenu menu, Click click) {
        menu.render();
    }
}
