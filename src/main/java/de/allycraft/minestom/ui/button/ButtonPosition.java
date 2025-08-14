package de.allycraft.minestom.ui.button;

public interface ButtonPosition {
    int getSlot(int width, int height);

    static ButtonPosition of(int slot) {
        return (width, height) -> slot;
    }

    static ButtonPosition of(int row, int column) {
        return (width, height) -> row * width + column;
    }

    static ButtonPosition center() {
        return (width, height) -> (height / 2) * width + (width / 2);
    }

    static ButtonPosition rowCenter(int row) {
        return (width, height) -> row * width + (width / 2);
    }

    static ButtonPosition offset(ButtonPosition pos, ButtonPosition offset) {
        return (width, height) -> pos.getSlot(width, height) + offset.getSlot(width, height);
    }

    static ButtonPosition offset(ButtonPosition pos, int offset) {
        return (width, height) -> pos.getSlot(width, height) + offset;
    }

    static ButtonPosition rowLast(int row) {
        return (width, height) -> (row + 1) * width - 1;
    }

    static ButtonPosition last() {
        return (width, height) -> (width * height) - 1;
    }
}
