package de.allycraft.minestom.ui.button;

import de.allycraft.minestom.ui.InvMenu;
import de.allycraft.minestom.ui.Menu;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class ButtonMenuOpen implements Button {
    private final @NotNull Function<Player, Menu> menuSupplier;
    private final @NotNull ItemStack item;

    public ButtonMenuOpen(@NotNull Function<Player, Menu> menuSupplier, @NotNull ItemStack item) {
        this.menuSupplier = menuSupplier;
        this.item = item;
    }

    public ButtonMenuOpen(@NotNull Supplier<Menu> menuSupplier, @NotNull ItemStack item) {
        this(player -> menuSupplier.get(), item);
    }

    @Override
    public @NotNull ItemStack getItem(InvMenu menu) {
        return this.item;
    }

    @Override
    public void onClick(InvMenu menu, Click click) {
        Menu newMenu = this.menuSupplier.apply(menu.getPlayer());
        newMenu.setParent(menu);
        newMenu.open();
    }
}
