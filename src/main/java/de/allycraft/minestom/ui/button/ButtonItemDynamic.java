package de.allycraft.minestom.ui.button;

import de.allycraft.minestom.ui.InvMenu;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ButtonItemDynamic extends Button {
    private final Supplier<ItemStack> itemSupplier;

    public ButtonItemDynamic(@NotNull InvMenu menu, Supplier<ItemStack> itemSupplier) {
        super(menu);
        this.itemSupplier = itemSupplier;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return this.itemSupplier.get();
    }

    @Override
    public void onClick(Click click) {
        this.menu.render();
    }
}
