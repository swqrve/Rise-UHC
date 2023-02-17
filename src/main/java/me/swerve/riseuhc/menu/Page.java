package me.swerve.riseuhc.menu;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Page {

    @Getter private final String identifier;

    @Getter private final int pageSize;
    @Getter Map<Integer, ItemStack> pageContents = new HashMap<>();

    public Page(int size, String identifier) {
        this.pageSize = size;
        this.identifier = identifier;
    }

    public Page(int size, Map<Integer, ItemStack> pageContents, String identifier) {
        this.pageSize = size;
        this.pageContents = pageContents;
        this.identifier = identifier;
    }

    public Page(int size) {
        this.pageSize = size;
        this.identifier = null;
    }

    public Page(int size, Map<Integer, ItemStack> pageContents) {
        this.pageSize = size;
        this.pageContents = pageContents;
        this.identifier = null;
    }

    public void put(int index, ItemStack stack) {
        pageContents.put(index, stack);
    }
}
