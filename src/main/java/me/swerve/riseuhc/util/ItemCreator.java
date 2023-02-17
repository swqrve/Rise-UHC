package me.swerve.riseuhc.util;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Skull;

import java.util.ArrayList;
import java.util.List;

public class ItemCreator {
    @Getter private ItemStack item;
    @Getter private ItemMeta meta;

    public ItemCreator(Material mat, int size) {
        this.item = new ItemStack(mat, size);
        this.meta = item.getItemMeta();
    }


    public ItemCreator setName(String name) {
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        updateMeta();
        return this;
    }

    public ItemCreator addLore(List<String> list) {
        List<String> lore = new ArrayList<>();
        list.forEach(string -> lore.add(ChatColor.translateAlternateColorCodes('&', string)));
        meta.setLore(lore);

        updateMeta();
        return this;
    }

    public ItemCreator setOwner(String name) {
        setData(3);

        SkullMeta skullMeta = (SkullMeta) meta;
        skullMeta.setOwner(name);

        updateMeta(skullMeta);
        return this;
    }

    public ItemCreator setData(int data) {
        item = new ItemStack(item.getType(), item.getAmount(), (byte) data);

        return this;
    }

    public ItemCreator addEnchant(Enchantment enchant, int enchantValue, boolean override) {
        meta.addEnchant(enchant, enchantValue, override);

        updateMeta();
        return this;
    }

    public ItemCreator setDurability(int dura) {
        item.setDurability((short) dura);

        return this;
    }

    public ItemCreator setUnbreakable(boolean state) {
        meta.spigot().setUnbreakable(state);

        updateMeta();
        return this;
    }

    public ItemCreator setAmount(int amount) {
        item.setAmount(amount);

        return this;
    }

    public ItemCreator setMaterial(Material mat) {
        item.setType(mat);

        return this;
    }

    private void updateMeta() {
        this.item.setItemMeta(meta);
        this.meta = item.getItemMeta();
    }

    private void updateMeta(SkullMeta meta) {
        this.item.setItemMeta(meta);
        this.meta = item.getItemMeta();
    }
}
