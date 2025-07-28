package com.mars.laserbridges.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityMotionQueue {
    private static final List<List<MovementTask>> tasks = new ArrayList<>();
    //private static final List<Entity> entities = new ArrayList<>();

    /*public static void moveGradually(Entity entity, Vec3 target, int steps, int delayBetweenSteps) {
        Vec3 start = entity.position();
        for (int i = 1; i <= steps; i++) {
            Vec3 step = start.lerp(target, (float) i / steps);
            tasks.add(new MovementTask(entity, step, i * delayBetweenSteps));
        }
    }*/

    public static void retriveData(Entity entity, Vec3 vec3, int delay, double radius, ServerLevel serverLevel, BlockPos blockPos){
        //if(!entities.contains(entity))entities.add(entity);

        List<MovementTask> newEntityValues = new ArrayList<>();
        System.out.println("blockPos: " + blockPos);
        if(getEntity(entity) != null) newEntityValues = getEntity(entity);
        MovementTask move = new MovementTask(entity, vec3, blockPos, radius, serverLevel, delay);
        assert newEntityValues != null;
        newEntityValues.add(move);
        tasks.add(newEntityValues);
        log();


    }
    public static boolean teleportRand(Level serverlevel, Entity entity, double diameter) {
        boolean $$3 = false;

        for(int $$4 = 0; $$4 < 16; ++$$4) {
            double $$5 = entity.getX() + (entity.getRandom().nextDouble() - (double)0.5F) * diameter;
            double $$6 = Mth.clamp(entity.getY() + (entity.getRandom().nextDouble() - (double)0.5F) * diameter, (double)serverlevel.getMinY(), (double)(serverlevel.getMinY() + ((ServerLevel)serverlevel).getLogicalHeight() - 1));
            double $$7 = entity.getZ() + (entity.getRandom().nextDouble() - (double)0.5F) * diameter;
            if (entity.isPassenger()) {
                entity.stopRiding();
            }

            Vec3 $$8 = entity.position();
            if (randomTeleport(entity,$$5, $$6, $$7, true)) {
                serverlevel.gameEvent(GameEvent.TELEPORT, $$8, GameEvent.Context.of(entity));
                SoundSource $$10;
                SoundEvent $$9;
                if (entity instanceof Fox) {
                    $$9 = SoundEvents.FOX_TELEPORT;
                    $$10 = SoundSource.NEUTRAL;
                } else {
                    $$9 = SoundEvents.CHORUS_FRUIT_TELEPORT;
                    $$10 = SoundSource.PLAYERS;
                }
                serverlevel.playSound((Entity) null, entity.getX(), entity.getY(), entity.getZ(), $$9, $$10);
                entity.resetFallDistance();
                $$3 = true;
                break;
            }
        }

        if ($$3 && entity instanceof Player $$13) {
            $$13.resetCurrentImpulseContext();
        }

        return $$3;
    }

    public static boolean randomTeleport(Entity entity, double x, double y, double z, boolean broadcastTeleport) {
        double d0 = entity.getX();
        double d1 = entity.getY();
        double d2 = entity.getZ();
        double d3 = y;
        boolean flag = false;
        BlockPos blockpos = BlockPos.containing(x, y, z);
        Level level = entity.level();
        if (level.hasChunkAt(blockpos)) {
            boolean flag1 = false;

            while(!flag1 && blockpos.getY() > level.getMinY()) {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = level.getBlockState(blockpos1);
                if (blockstate.blocksMotion()) {
                    flag1 = true;
                } else {
                    --d3;
                    blockpos = blockpos1;
                }
            }

            if (flag1) {
                entity.teleportTo(x, d3, z);

                if (level.noCollision(entity) && !level.containsAnyLiquid(entity.getBoundingBox())) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            entity.teleportTo(d0, d1, d2);
            return false;
        } else {
            if (broadcastTeleport) {
                level.broadcastEntityEvent(entity, (byte)46);
            }

            return true;
        }
    }

    public static void tick(ServerLevel level) {
        Iterator<List<MovementTask>> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            //Next layer
            Iterator<MovementTask> movementTaskIterator = iterator.next().iterator();

            while (movementTaskIterator.hasNext()){
                MovementTask taskMovement = movementTaskIterator.next();
                taskMovement.delay--;
                if (taskMovement.delay <= 0) {
                    if (!taskMovement.entity.isRemoved()) {
                        teleportRand(taskMovement.sl, taskMovement.entity, taskMovement.radius);
                    }
                    movementTaskIterator.remove();
                    //entities.remove(taskMovement.entity);
                }
            }

        }
    }

    public static boolean currentlyContains(Entity entity, Vec3 blockPos){
        Iterator<List<MovementTask>> iterator = tasks.iterator();
        while (iterator.hasNext()){
            Iterator<MovementTask> containerIterator = iterator.next().iterator();

            while (containerIterator.hasNext()){
                MovementTask taskMovement = containerIterator.next();
                if(taskMovement.entity == entity){
                    System.out.println("Entity True");
                    if(taskMovement.pos == blockPos){
                        System.out.println("Block Pos is: " + blockPos);
                        return true;
                    }
                    else {
                        System.out.println("Entity's Block Pos is not " + blockPos + " but " + taskMovement.blockPos);
                    }
                }
            }

        }
        return false;
    }
    public static boolean currentlyContainsEntity(Entity entity, Vec3 blockPos){
        Iterator<List<MovementTask>> iterator = tasks.iterator();
        while (iterator.hasNext()){
            Iterator<MovementTask> containerIterator = iterator.next().iterator();
            while (containerIterator.hasNext()){
                MovementTask taskMovement = containerIterator.next();
                if(taskMovement.entity == entity){
                    return true;
                }
            }

        }
        return false;
    }

    public static void log(){
        Iterator<List<MovementTask>> iterator = tasks.iterator();
        while (iterator.hasNext()){
            Iterator<MovementTask> containerIterator = iterator.next().iterator();
            System.out.println(containerIterator);
            while (containerIterator.hasNext()){
                MovementTask taskMovement = containerIterator.next();
                System.out.println(taskMovement.entity + ", " + taskMovement.blockPos);
            }

        }
    }

    public static List<MovementTask> getEntity(Entity entity){
        for (List<MovementTask> task : tasks) {

            for (MovementTask taskMovement : task) {
                if (taskMovement.entity == entity) {
                    return task;
                }
            }

        }
        return null;
    }

    private static class MovementTask {
        final Entity entity;
        final Vec3 pos;
        final BlockPos blockPos;
        final double radius;
        final ServerLevel sl;
        int delay;

        MovementTask(Entity entity, Vec3 pos, BlockPos blockPos, double radius, ServerLevel sl, int delay) {
            this.entity = entity;
            this.pos = pos;
            this.blockPos = blockPos;
            this.radius = radius;
            this.sl = sl;
            this.delay = delay;
        }
    }
}

