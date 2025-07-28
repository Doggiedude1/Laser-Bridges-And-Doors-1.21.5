package com.mars.laserbridges.util;

import com.mars.laserbridges.blocks.FlamingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber
public interface ITickingBlockEntity
{
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            //EntityMotionQueue.tick(serverLevel);
            RewindEntity.tick(serverLevel);
            //FlamingBlock.tick(serverLevel);
        }
    }
}
