package de.allycraft.minestom.ui.button;

import de.allycraft.minestom.ui.InvMenu;
import de.allycraft.minestom.ui.Menu;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class ButtonMenuOpen extends Button {
    private final @NotNull Function<Player, Menu> menuSupplier;
    private final @NotNull ItemStack item;

    public ButtonMenuOpen(@NotNull InvMenu menu, @NotNull Function<Player, Menu> menuSupplier, @NotNull ItemStack item) {
        super(menu);
        this.menuSupplier = menuSupplier;
        this.item = item;
    }

    public ButtonMenuOpen(@NotNull InvMenu menu, @NotNull Supplier<Menu> menuSupplier, @NotNull ItemStack item) {
        this(menu, player -> menuSupplier.get(), item);
    }

    @Override
    public @NotNull ItemStack getItem() {
        return this.item;
    }

    @Override
    public void onClick(Click click) {
        Menu newMenu = this.menuSupplier.apply(this.menu.getPlayer());
        newMenu.setParent(this.menu);
        newMenu.open();
    }
}
