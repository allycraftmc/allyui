package de.allycraft.minestom.ui.button;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface ButtonArea {
    List<Integer> getButtonPositions(int width, int height);

    static ButtonArea all() {
        return (width, height) -> IntStream.range(0, width * height)
                .boxed()
                .toList();
    }

    static ButtonArea of(ButtonPosition position) {
        return (width, height) -> List.of(position.getSlot(width, height));
    }

    static ButtonArea range(ButtonPosition start, ButtonPosition end) {
        return (width, height) -> IntStream.range(start.getSlot(width, height), end.getSlot(width, height) + 1)
                .boxed()
                .toList();
    }

    static ButtonArea subtract(ButtonArea buttonArea, ButtonArea excluded) {
        return (width, height) -> {
            List<Integer> excludedSlots = excluded.getButtonPositions(width, height);
            return buttonArea.getButtonPositions(width, height)
                    .stream()
                    .filter(slot -> !excludedSlots.contains(slot))
                    .toList();
        };
    }

    static ButtonArea inverse(ButtonArea buttonArea) {
        return ButtonArea.subtract(ButtonArea.all(), buttonArea);
    }

    static ButtonArea union(ButtonArea buttonArea1, ButtonArea buttonArea2) {
        return (width, height) -> Stream.concat(
                buttonArea1.getButtonPositions(width, height).stream(),
                buttonArea2.getButtonPositions(width, height).stream()
        )
                .toList();
    }

    static ButtonArea rect(ButtonPosition start, int rectWidth, int rectHeight) {
        return (width, height) -> {
            List<Integer> slots = new ArrayList<>(rectWidth * rectHeight);

            int slot = start.getSlot(width, height);
            for(int yOffset = 0; yOffset < rectHeight; yOffset++) {
                for(int xOffset = 0; xOffset < rectWidth; xOffset++) {
                    slots.add(slot);
                    slot++;
                }
                slot += width - rectWidth;
            }

            return slots;
        };
    }

    static ButtonArea rect(ButtonPosition start, ButtonPosition end) {
        return (width, height) -> {
            int startSlot = start.getSlot(width, height);
            int endSlot = end.getSlot(width, height);
            int rectWidth = (endSlot % width) - (startSlot % width) + 1;
            int rectHeight = (endSlot / width) - (startSlot / width) + 1;
            return ButtonArea.rect(start, rectWidth, rectHeight).getButtonPositions(width, height);
        };
    }
}
