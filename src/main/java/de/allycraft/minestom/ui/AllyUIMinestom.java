package de.allycraft.minestom.ui;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerAnvilInputEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class AllyUIMinestom {
    private static final EventNode<@NotNull Event> eventNode = EventNode.all("minestom-menu");

    static {
        eventNode.addListener(InventoryOpenEvent.class, AllyUIMinestom::onInventoryOpenEvent);
        eventNode.addListener(InventoryPreClickEvent.class, AllyUIMinestom::onInventoryPreClickEvent);
        eventNode.addListener(PlayerAnvilInputEvent.class, AllyUIMinestom::onAnvilInputEvent);
        eventNode.addListener(PlayerDisconnectEvent.class, AllyUIMinestom::onPlayerDisconnectEvent);
        eventNode.addListener(InventoryCloseEvent.class, AllyUIMinestom::onInventoryCloseEvent);
    }

    private static void onInventoryOpenEvent(@NotNull InventoryOpenEvent event) {
        AbstractInventory inventory = event.getInventory();
        AbstractInventory oldInventory = event.getPlayer().getOpenInventory();

        if(oldInventory != null) {
            Menu menu = oldInventory.getTag(Menu.MENU_TAG);
            menu.onClose();
        }

        Menu menu = inventory.getTag(Menu.MENU_TAG);
        if(menu != null) {
            menu.onOpen();
        }
    }

    private static void onInventoryPreClickEvent(@NotNull InventoryPreClickEvent event) {
        PlayerInventory playerInventory = event.getPlayer().getInventory();
        AbstractInventory openInventory = event.getPlayer().getOpenInventory();
        AbstractInventory inventory = event.getInventory();

        if(openInventory == null) return;
        Menu menu = openInventory.getTag(Menu.MENU_TAG);
        if(menu == null) return;
        if(inventory == openInventory) {
            if(menu instanceof InvMenu invMenu) {
                invMenu.onInventoryPreClickEvent(event);
            } else if(menu instanceof AnvilMenu anvilMenu) {
                anvilMenu.onInventoryPreClickEvent(event);
            }
        } else if(inventory == playerInventory) {
            if(menu instanceof InvMenu invMenu) {
                invMenu.onPlayerInventoryPreClickEvent(event);
            } else if(menu instanceof AnvilMenu anvilMenu) {
                anvilMenu.onPlayerInventoryPreClickEvent(event);
            }
        }
    }

    private static void onAnvilInputEvent(@NotNull PlayerAnvilInputEvent event) {
        Menu menu = event.getInventory().getTag(Menu.MENU_TAG);
        if(menu instanceof AnvilMenu anvilMenu) {
            anvilMenu.onInputEvent(event);
        }
    }

    private static void onInventoryCloseEvent(@NotNull InventoryCloseEvent event) {
        Menu menu = event.getInventory().getTag(Menu.MENU_TAG);
        if(menu != null) {
            menu.onClose();
        }
    }

    private static void onPlayerDisconnectEvent(@NotNull PlayerDisconnectEvent event) {
        AbstractInventory openInventory = event.getPlayer().getOpenInventory();
        if(openInventory == null) return;
        Menu menu = openInventory.getTag(Menu.MENU_TAG);
        if(menu != null) {
            menu.onClose();
        }
    }

    public static void register(EventNode<@NotNull Event> parentNode) {
        parentNode.addChild(eventNode);
    }

    public static void unregister(EventNode<@NotNull Event> parentNode) {
        parentNode.removeChild(eventNode);
    }
}
