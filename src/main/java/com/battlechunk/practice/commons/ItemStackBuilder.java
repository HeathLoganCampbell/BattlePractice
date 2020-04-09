package com.battlechunk.practice.commons;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ItemStackBuilder {
	private ItemStack itemStack;


	public ItemStackBuilder(Material material, short data) {
		this.itemStack = new ItemStack(material, data);
	}

	public ItemStackBuilder(Material material, int amount, short data) {
		this.itemStack = new ItemStack(material, amount, data);
	}

	public ItemStackBuilder(Material material, int amount) {
		this.itemStack = new ItemStack(material, amount);
	}
	
	public ItemStackBuilder(Material material) {
		this.itemStack = new ItemStack(material);
	}

	public ItemStackBuilder(ItemStack itemStack)
	{
		this.itemStack = itemStack;
	}

	public ItemStackBuilder amount(int amount) {
		this.itemStack.setAmount(amount);
		return this;
	}
	
	public ItemStackBuilder durability(int amount) 
    {
        this.build().setDurability((short)amount);
        return this;
    }

    public ItemStackBuilder durabilityLeft(int amount)
    {
        this.build().setDurability((short)(this.build().getType().getMaxDurability() - amount));
        return this;
    }

	public ItemStackBuilder leatherColour(Color color) {
		try {
			LeatherArmorMeta itemMeta = (LeatherArmorMeta)this.build().getItemMeta();
			itemMeta.setColor(color);
			this.build().setItemMeta((ItemMeta)itemMeta);
			return this;
		}
		catch (ClassCastException ex) {
			return this;
		}
	}
    
    /**
     * On use on LEATHER_ARMOR
     * @param color
     * @return
     */
    public ItemStackBuilder setLeatherColour(Color color)
    {
        try 
        {
            LeatherArmorMeta itemMeta = (LeatherArmorMeta)this.build().getItemMeta();
            itemMeta.setColor(color);
            this.build().setItemMeta((ItemMeta)itemMeta);
            return this;
        }
        catch (ClassCastException ex)
        {
           ex.printStackTrace();
        }
        return this;
    }

	public ItemStackBuilder lore(String... lore) {
		ItemMeta itemMeta = build().getItemMeta();
		itemMeta.setLore(Arrays.asList(lore));
		build().setItemMeta(itemMeta);
		return this;
	}

	public ItemStackBuilder lore(List<String> lore) {
		ItemMeta itemMeta = build().getItemMeta();
		itemMeta.setLore(lore);
		build().setItemMeta(itemMeta);
		return this;
	}


	public ItemStackBuilder addLore(String lore)
	{
		ItemMeta itemMeta = build().getItemMeta();
		List<String> newLore = new ArrayList<String>(itemMeta.getLore());
		newLore.add(lore);
		itemMeta.setLore(newLore);
		build().setItemMeta(itemMeta);
		return this;
	}

	public ItemStackBuilder addLore(String... lore)
	{
		ItemMeta itemMeta = build().getItemMeta();
		List<String> newLore = new ArrayList<String>(itemMeta.getLore());
		for (String s : lore) {
			newLore.add(s);
		}
		itemMeta.setLore(newLore);
		build().setItemMeta(itemMeta);
		return this;
	}

	public ItemStackBuilder displayName(String displayName) {
		ItemMeta itemMeta = build().getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		build().setItemMeta(itemMeta);
		return this;
	}

	public ItemStackBuilder enchantment(Enchantment enchantment, int level) {
		this.itemStack.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemStackBuilder setSkullOwner(String owner)
	{
		SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		meta.setOwner(owner);
		itemStack.setItemMeta(meta);
		return this;
	}
	
	public ItemMeta getItemMeta() 
	{
		return this.itemStack.getItemMeta(); 
	}

	public ItemStack build() {
		return this.itemStack;
	}

	public ItemStackBuilder clone()
	{
		return new ItemStackBuilder(this.build().clone());
	}


	//weird old code
	public String serialize()
	{
		String itemStr = "";
		itemStr += this.itemStack.getType().name() + " " + ( this.itemStack.getData().getData()) + " " + this.itemStack.getAmount();
		if(this.itemStack.hasItemMeta())
		{
			if(this.itemStack.getItemMeta().hasDisplayName())
				itemStr += " Name=" + this.itemStack.getItemMeta().getDisplayName().replaceAll(" ", "_").replace("§", "&");
			if(this.itemStack.getItemMeta().hasLore())
			{
				String lore = "";
				for(int i = 0; i < this.itemStack.getItemMeta().getLore().size(); i++)
				{
					lore += (i == 0 ? "" : "\\n") + this.itemStack.getItemMeta().getLore().get(i);
					lore = lore.replaceAll(" ", "_").replace("§", "&");
				}
				itemStr += " Lore=" + lore;
			}
		}
		return itemStr;
	}

	public ItemStackBuilder glow()
	{
		ItemMeta meta = itemStack.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemStackBuilder hideAttributess()
	{
		ItemMeta meta = itemStack.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemStack.setItemMeta(meta);
		return this;
	}
}