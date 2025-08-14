package de.allycraft.minestom.ui.button;

import de.allycraft.minestom.ui.InvMenu;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ButtonMenuBack extends Button {
    private final ItemStack item;

    public ButtonMenuBack(@NotNull InvMenu menu, ItemStack item) {
        super(menu);
        this.item = item;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return this.item;
    }

    @Override
    public void onClick(Click click) {
        if(this.menu.getParent() != null) {
            this.menu.getParent().open();
        }
    }
}
