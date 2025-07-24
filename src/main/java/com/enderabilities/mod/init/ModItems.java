package com.enderabilities.mod.init;

import com.enderabilities.mod.EnderAbilitiesMod;
import com.enderabilities.mod.items.EnderSword;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = 
            DeferredRegister.create(ForgeRegistries.ITEMS, EnderAbilitiesMod.MOD_ID);

    public static final RegistryObject<Item> ENDER_SWORD = ITEMS.register("ender_sword",
            () -> new EnderSword(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}