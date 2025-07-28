package com.mars.laserbridges.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public interface RewindEffect
{

    default void tick(Level level, BlockPos pos, BlockState state) {}
}
