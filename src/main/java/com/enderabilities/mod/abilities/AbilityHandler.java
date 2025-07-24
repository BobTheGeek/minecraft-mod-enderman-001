package com.enderabilities.mod.abilities;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class AbilityHandler {
    private static long lastTeleportTime = 0;
    private static long lastInvisibilityTime = 0;
    private static long lastSummonTime = 0;
    
    private static final long TELEPORT_COOLDOWN = 3000; // 3 seconds
    private static final long INVISIBILITY_COOLDOWN = 10000; // 10 seconds
    private static final long SUMMON_COOLDOWN = 15000; // 15 seconds

    public static void handleTeleport() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Level level = mc.level;
        
        if (player == null || level == null) return;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTeleportTime < TELEPORT_COOLDOWN) {
            return;
        }
        
        Vec3 lookVec = player.getLookAngle();
        Vec3 startPos = player.getEyePosition();
        Vec3 targetPos = null;
        
        // Raycast to find a valid teleport location
        for (int i = 1; i <= 32; i++) {
            Vec3 checkPos = startPos.add(lookVec.scale(i));
            BlockPos blockPos = new BlockPos(checkPos.x, checkPos.y, checkPos.z);
            BlockPos groundPos = new BlockPos(checkPos.x, checkPos.y - 1, checkPos.z);
            
            if (!level.getBlockState(blockPos).isAir() || 
                !level.getBlockState(new BlockPos(checkPos.x, checkPos.y + 1, checkPos.z)).isAir()) {
                break;
            }
            
            if (!level.getBlockState(groundPos).isAir()) {
                targetPos = new Vec3(checkPos.x, checkPos.y, checkPos.z);
            }
        }
        
        if (targetPos != null) {
            // Play teleport sound
            level.playSound(player, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, 
                          SoundSource.PLAYERS, 1.0F, 1.0F);
            
            // Teleport player
            player.teleportTo(targetPos.x, targetPos.y, targetPos.z);
            
            // Play arrival sound
            level.playSound(player, new BlockPos(targetPos), SoundEvents.ENDERMAN_TELEPORT, 
                          SoundSource.PLAYERS, 1.0F, 1.0F);
            
            lastTeleportTime = currentTime;
        }
    }

    public static void handleInvisibility() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        
        if (player == null) return;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastInvisibilityTime < INVISIBILITY_COOLDOWN) {
            return;
        }
        
        // Apply invisibility effect for 30 seconds
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 600, 0, false, false));
        
        // Play sound effect
        player.level.playSound(player, player.blockPosition(), SoundEvents.ENDERMAN_AMBIENT, 
                             SoundSource.PLAYERS, 0.5F, 1.5F);
        
        lastInvisibilityTime = currentTime;
    }

    public static void handleSummonEnderman() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Level level = mc.level;
        
        if (player == null || level == null || level.isClientSide) return;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSummonTime < SUMMON_COOLDOWN) {
            return;
        }
        
        // Find a suitable spawn location
        Vec3 playerPos = player.position();
        BlockPos spawnPos = null;
        
        for (int attempts = 0; attempts < 10; attempts++) {
            double x = playerPos.x + (level.random.nextDouble() - 0.5) * 10;
            double z = playerPos.z + (level.random.nextDouble() - 0.5) * 10;
            double y = playerPos.y;
            
            BlockPos testPos = new BlockPos(x, y, z);
            
            // Find ground level
            while (y > level.getMinBuildHeight() && level.getBlockState(testPos).isAir()) {
                y--;
                testPos = new BlockPos(x, y, z);
            }
            y++; // One block above ground
            testPos = new BlockPos(x, y, z);
            
            if (level.getBlockState(testPos).isAir() && 
                level.getBlockState(new BlockPos(x, y + 1, z)).isAir()) {
                spawnPos = testPos;
                break;
            }
        }
        
        if (spawnPos != null) {
            EnderMan enderman = new EnderMan(EntityType.ENDERMAN, level);
            enderman.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
            
            // Make the enderman friendly to the player
            enderman.setTarget(null);
            
            level.addFreshEntity(enderman);
            
            // Play spawn sound
            level.playSound(player, spawnPos, SoundEvents.ENDERMAN_TELEPORT, 
                          SoundSource.NEUTRAL, 1.0F, 0.8F);
            
            lastSummonTime = currentTime;
        }
    }
}