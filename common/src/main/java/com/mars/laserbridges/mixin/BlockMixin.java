package com.mars.laserbridges.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "onPlace", at = @At("TAIL"))
    private void laserbridges_onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving, CallbackInfo ci) {
        scheduleFall(level, pos, (Block) (Object) this);
    }

    @Inject(method = "neighborChanged", at = @At("TAIL"))
    private void laserbridges_neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving, CallbackInfo ci) {
        scheduleFall(level, pos, (Block) (Object) this);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void laserbridges_tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (FallingBlock.isFree(level.getBlockState(pos.below()))) {
            FallingBlockEntity.fall(level, pos, state);
        }
    }

    private void scheduleFall(Level level, BlockPos pos, Block block) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, block, 2);
        }
    }
}
