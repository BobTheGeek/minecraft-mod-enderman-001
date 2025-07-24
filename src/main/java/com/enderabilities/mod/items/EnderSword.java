package com.enderabilities.mod.items;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EnderSword extends SwordItem {
    private static final EnderSwordTier ENDER_TIER = new EnderSwordTier();

    public EnderSword(Properties properties) {
        super(ENDER_TIER, 8, -2.4F, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && !player.level.isClientSide) {
            // 25% chance to teleport behind the target when attacking
            if (player.getRandom().nextFloat() < 0.25f) {
                Vec3 targetPos = target.position();
                Vec3 behindTarget = targetPos.subtract(target.getLookAngle().scale(2));
                
                // Find safe teleport location
                BlockPos safePos = new BlockPos(behindTarget.x, behindTarget.y, behindTarget.z);
                if (player.level.getBlockState(safePos).isAir() && 
                    player.level.getBlockState(safePos.above()).isAir()) {
                    
                    player.teleportTo(behindTarget.x, behindTarget.y, behindTarget.z);
                    player.level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, 
                                         SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
            
            // Play enderman hurt sound on hit
            player.level.playSound(null, target.blockPosition(), SoundEvents.ENDERMAN_HURT, 
                                 SoundSource.NEUTRAL, 0.5F, 1.2F);
        }
        
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        
        if (!level.isClientSide && player.isCrouching()) {
            // Right-click while crouching to perform a short-range teleport
            Vec3 lookVec = player.getLookAngle();
            Vec3 targetPos = player.position().add(lookVec.scale(8));
            
            BlockPos blockPos = new BlockPos(targetPos.x, targetPos.y, targetPos.z);
            if (level.getBlockState(blockPos).isAir() && 
                level.getBlockState(blockPos.above()).isAir()) {
                
                player.teleportTo(targetPos.x, targetPos.y, targetPos.z);
                level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, 
                              SoundSource.PLAYERS, 1.0F, 1.0F);
                
                // Add small cooldown
                player.getCooldowns().addCooldown(this, 40); // 2 seconds
                
                return InteractionResultHolder.success(itemstack);
            }
        }
        
        return super.use(level, player, hand);
    }

    private static class EnderSwordTier implements Tier {
        @Override
        public int getUses() {
            return 2031; // Diamond-tier durability
        }

        @Override
        public float getSpeed() {
            return 8.0F; // Faster than diamond
        }

        @Override
        public float getAttackDamageBonus() {
            return 6.0F; // Higher than diamond
        }

        @Override
        public int getLevel() {
            return 3; // Diamond level
        }

        @Override
        public int getEnchantmentValue() {
            return 15; // Higher enchantability than diamond
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(net.minecraft.world.item.Items.ENDER_PEARL);
        }
    }
}