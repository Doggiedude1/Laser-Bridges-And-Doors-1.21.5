package com.mars.laserbridges.events.server;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.ICancellableEvent;

public class FlamingBlockPlace extends Event implements ICancellableEvent
{
    private final Entity approacher;
    private final BlockPos blockPos;
    private final Level level;

    public FlamingBlockPlace(Entity approacher, BlockPos blockPos, Level level){
        this.approacher = approacher;
        this.blockPos = blockPos;
        this.level = level;
    }

    public Entity getApproacher(){
        return this.approacher;
    }
    public BlockPos getBlockPos(){
        return this.blockPos;
    }
    public Level getLevel(){
        return this.level;
    }
}
