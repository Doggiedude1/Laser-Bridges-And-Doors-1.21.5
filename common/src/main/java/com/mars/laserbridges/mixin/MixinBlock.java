package com.mars.laserbridges.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Makes every block behave like a falling block by scheduling a tick and
 * spawning a {@link FallingBlockEntity} whenever the block is unsupported.
 */
@Mixin(Block.class)
public abstract class MixinBlock {

    @Inject(method = "onPlace", at = @At("TAIL"))
    private void laserbridges$onPlace(BlockState state, Level level, BlockPos pos,
                                     BlockState oldState, boolean notify, CallbackInfo ci) {
        scheduleFallCheck(level, pos);
    }

    @Inject(method = "neighborChanged", at = @At("TAIL"))
    private void laserbridges$neighborChanged(BlockState state, Level level, BlockPos pos,
                                             Block neighbor, BlockPos fromPos, boolean notify,
                                             CallbackInfo ci) {
        scheduleFallCheck(level, pos);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void laserbridges$tick(BlockState state, ServerLevel level, BlockPos pos,
                                   RandomSource random, CallbackInfo ci) {
        if (isFree(level.getBlockState(pos.below()))) {
            FallingBlockEntity.fall(level, pos, state);
        }
    }

    private void scheduleFallCheck(Level level, BlockPos pos) {
        if (!level.isClientSide()) {
            level.scheduleTick(pos, (Block)(Object)this, 2);
        }
    }

    private boolean isFree(BlockState state) {
        return state.isAir() || state.liquid() || state.canBeReplaced();
    }
}
