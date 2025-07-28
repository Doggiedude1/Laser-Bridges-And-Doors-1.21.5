package com.mars.laserbridges.events.server;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class FlamingBlockCollision extends Event implements ICancellableEvent
{
    private final Entity entity;
    private final Vec3 vec3;
    private final BlockPos blockPos;
    private final int delay;
    private final double doub;
    private final ServerLevel sl;

    public FlamingBlockCollision(Entity entity, Vec3 pos, int delay, double doub, ServerLevel sl, BlockPos blockPos) {
        this.blockPos = blockPos;
        this.doub = doub;
        this.sl = sl;
        this.entity = entity;
        this.vec3 = pos;
        this.delay = delay;
    }
    public ServerLevel getServerLevel(){
        return sl;
    }

    public Entity getEntity(){
        return entity;
    }
    public Vec3 getVec3(){
        return this.vec3;
    }
    public BlockPos getBlockPos(){
        return this.blockPos;
    }
    public Vec3 getEntityPos(){
        return this.entity.position();
    }
    public Double getDouble(){
        return doub;
    }

    public int getDelay(){
        return this.delay;
    }
}
