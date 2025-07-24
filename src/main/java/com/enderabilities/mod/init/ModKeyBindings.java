package com.enderabilities.mod.init;

import com.enderabilities.mod.abilities.AbilityHandler;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

public class ModKeyBindings {
    public static final String KEY_CATEGORY = "key.categories.enderabilities";
    
    public static final KeyMapping TELEPORT_KEY = new KeyMapping(
            "key.enderabilities.teleport",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_R,
            KEY_CATEGORY
    );
    
    public static final KeyMapping INVISIBILITY_KEY = new KeyMapping(
            "key.enderabilities.invisibility",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_I,
            KEY_CATEGORY
    );
    
    public static final KeyMapping SUMMON_ENDERMAN_KEY = new KeyMapping(
            "key.enderabilities.summon_enderman",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_E,
            KEY_CATEGORY
    );

    public static void register() {
        Minecraft.getInstance().options.keyMappings = addKeyMapping(Minecraft.getInstance().options.keyMappings, TELEPORT_KEY);
        Minecraft.getInstance().options.keyMappings = addKeyMapping(Minecraft.getInstance().options.keyMappings, INVISIBILITY_KEY);
        Minecraft.getInstance().options.keyMappings = addKeyMapping(Minecraft.getInstance().options.keyMappings, SUMMON_ENDERMAN_KEY);
    }

    private static KeyMapping[] addKeyMapping(KeyMapping[] keyMappings, KeyMapping newMapping) {
        KeyMapping[] newArray = new KeyMapping[keyMappings.length + 1];
        System.arraycopy(keyMappings, 0, newArray, 0, keyMappings.length);
        newArray[keyMappings.length] = newMapping;
        return newArray;
    }

    public static void handleKeyInputs() {
        while (TELEPORT_KEY.consumeClick()) {
            AbilityHandler.handleTeleport();
        }
        
        while (INVISIBILITY_KEY.consumeClick()) {
            AbilityHandler.handleInvisibility();
        }
        
        while (SUMMON_ENDERMAN_KEY.consumeClick()) {
            AbilityHandler.handleSummonEnderman();
        }
    }
}