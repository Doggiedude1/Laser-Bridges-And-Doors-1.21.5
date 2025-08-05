package com.mars.laserbridges.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Makes every block fall like sand or gravel when unsupported.
 */
@Mixin(Block.class)
public class MixinBlock {
    private static final int FALL_DELAY = 2;

    @Inject(method = "onPlace", at = @At("TAIL"))
    private void laserbridges$onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved, CallbackInfo ci) {
        level.scheduleTick(pos, (Block)(Object)this, FALL_DELAY);
    }

    @Inject(method = "neighborChanged", at = @At("TAIL"))
    private void laserbridges$neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moved, CallbackInfo ci) {
        level.scheduleTick(pos, (Block)(Object)this, FALL_DELAY);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void laserbridges$tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (FallingBlock.isFree(level.getBlockState(pos.below()))) {
            FallingBlockEntity.fall(level, pos, state);
        }
    }
}
