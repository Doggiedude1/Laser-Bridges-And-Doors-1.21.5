package com.mars.laserbridges.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.mars.laserbridges.Laserbridges.FLAMING_BLOCK;

public class RewindEntity
{
    private static List<BlockWEntityData> tasks = new ArrayList<>();

    public static void retriveData(Entity entity, BlockPos blockPos, Level level){
        //if(!entities.contains(entity))entities.add(entity);

        BlockWEntityData data = new BlockWEntityData(new EntityData(entity, Vec3.ZERO, Double.MIN_VALUE, false));
        tasks.add(data);
    }

    public static void addBlock(BlockPos pos, Level level){
        BlockWEntityData data = new BlockWEntityData(new BlockData(pos, level));;
        System.out.println("adding");
        tasks.add(data);
    }

    public static void rewindEntity(Entity entity){
        BlockWEntityData ent = null;
        for (BlockWEntityData data : tasks) {
            if(data.getEntityData().isPresent()){
                if (data.getEntityData().get().entity == entity) {
                    System.out.println("here");
                    entity.teleportTo(data.getEntityData().get().pos.x, data.getEntityData().get().pos.y, data.getEntityData().get().pos.z);
                    ent = data;
                }
            }

        }
        if(tasks != null)tasks.remove(ent);

    }
    public static List<Entity> getNearbyEntities(Level level, BlockPos center, double radius) {
        AABB box = AABB.ofSize(center.getCenter(), radius * 2, radius * 2, radius * 2);
        return level.getEntities((Entity) null, box, entity -> true); // true = accept all
    }
    public static boolean blockStillThere(BlockPos pos, Level level){
        if(level.getBlockState(pos) == FLAMING_BLOCK.get().defaultBlockState()){
            return true;
        }

        return false;
    }

    public static void tick(ServerLevel level) {
        List<BlockWEntityData> toAdd = new ArrayList<>(tasks);
        for (BlockWEntityData taskMovement : tasks) {

            if(taskMovement.blockData == null) continue;
            if(!blockStillThere(taskMovement.blockData.blockPos, taskMovement.blockData.level)) continue;
            List<Entity> nearbyEntities = getNearbyEntities(level, taskMovement.blockData.blockPos, 4);
            System.out.println(nearbyEntities);
            if(taskMovement.getEntityData().isPresent()) {
                EntityData entityData = taskMovement.getEntityData().get();
                BlockWEntityData data = new BlockWEntityData(new EntityData(taskMovement.entityData.entity, taskMovement.entityData.entity.position(), Double.MIN_VALUE, false));
                int index = getIndex(taskMovement.entityData.entity);
                if(!nearbyEntities.contains(entityData.entity)){
                    System.out.println("Trying to remove");
                    toAdd.remove(index);
                    toAdd.add(index, data);
                }
            }

            for (Entity entity : nearbyEntities) {
                if(currentlyContainsEntity(entity)) continue;
                System.out.println("here");
                Vec3 blockpos = new Vec3(taskMovement.blockData.blockPos.getX(), taskMovement.blockData.blockPos.getY(), taskMovement.blockData.blockPos.getZ());
                double dist = blockpos.distanceTo(entity.position());
                EntityData entityData;
                if(taskMovement.getEntityData().isPresent()){
                    entityData = taskMovement.getEntityData().get();
                    if(dist > entityData.dist) {
                        BlockWEntityData data = new BlockWEntityData(new EntityData(entity, entity.position(), dist, false));
                        toAdd.add(getIndex(entity), data);
                    }
                    System.out.println(toAdd.size() + ", " + entity + ", " + entity.position() + ", " + false + ", " + dist  + ", " + entityData.dist);
                }
                else{
                    BlockWEntityData data = new BlockWEntityData(new EntityData(entity, entity.position(), dist, false));
                    toAdd.add(getIndex(entity), data);
                }
            }
        }

        tasks = toAdd;
    }
    public static boolean currentlyContainsEntity(Entity entity){
        Iterator<BlockWEntityData> iterator = tasks.iterator();
        while (iterator.hasNext()){

            BlockWEntityData taskMovement = iterator.next();
            if(taskMovement.getEntityData().isPresent()){
                if(taskMovement.getEntityData().get().entity == entity){
                    return true;
                }
            }


        }
        return false;
    }

    public static int getIndex(Entity entity){
        Iterator<BlockWEntityData> iterator = tasks.iterator();
        while (iterator.hasNext()){

            BlockWEntityData taskMovement = iterator.next();
            if(taskMovement.getEntityData().isPresent()){
                if(taskMovement.getEntityData().get().entity == entity){
                    System.out.println("entity exists: " + taskMovement.getEntityData().get().entity);
                    return tasks.indexOf(taskMovement);
                }
            }


        }
        return tasks.indexOf(tasks.getLast())-1;
    }
    public static int getIndex(EntityData entity){
        Iterator<BlockWEntityData> iterator = tasks.iterator();
        while (iterator.hasNext()){

            BlockWEntityData taskMovement = iterator.next();
            if(taskMovement.getEntityData().isPresent()){
                if(taskMovement.getEntityData().get() == entity){
                    System.out.println("entity data exists: " + taskMovement.getEntityData().get());
                    return tasks.indexOf(taskMovement);
                }
            }


        }
        return tasks.indexOf(tasks.getLast())-1;
    }

    private static class EntityData {
        final Entity entity;
        final Vec3 pos;
        double dist = Double.MIN_VALUE;
        boolean collided;


        EntityData(Entity entity, Vec3 pos, double dist, boolean collided) {
            this.entity = entity;
            this.pos = pos;
            this.dist = dist;
            this.collided = collided;
        }
    }

    private static class BlockData{
        final BlockPos blockPos;
        final Level level;

        BlockData(BlockPos blockPos, Level level){
            this.blockPos = blockPos;
            this.level = level;
        }
    }
    private static class BlockWEntityData{
        final BlockData blockData;
        final EntityData entityData;

        private BlockWEntityData(BlockData blockData, EntityData entityData) {
            this.blockData = blockData;
            this.entityData = entityData;
        }
        private BlockWEntityData(BlockData blockData){
            this(blockData, null);
        }
        private BlockWEntityData(EntityData entityData){
            this(null, entityData);
        }
        public Optional<EntityData> getEntityData() {
            return Optional.ofNullable(entityData);
        }
        public Optional<BlockData> getBlockData() {
            return Optional.ofNullable(blockData);
        }
    }
}
