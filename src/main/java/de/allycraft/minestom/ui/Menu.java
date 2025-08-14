package de.allycraft.minestom.ui;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.Player;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Menu {
    public static final Tag<Menu> MENU_TAG = Tag.Transient("allyui_menu");
    public static final Sound OPEN_SOUND = Sound.sound(SoundEvent.BLOCK_LEVER_CLICK, Sound.Source.UI, 1.0F, 1.0F);

    private @Nullable Menu parent;
    protected final @NotNull Player player;

    protected Menu(@NotNull Player player) {
        this.player = player;
    }

    abstract public void open();
    abstract public void close();

    abstract protected void onOpen();
    abstract protected void onClose();

    public @Nullable Menu getParent() {
        return this.parent;
    }

    public void setParent(@Nullable Menu parent) {
        this.parent = parent;
    }

    public @NotNull Player getPlayer() {
        return player;
    }
}
