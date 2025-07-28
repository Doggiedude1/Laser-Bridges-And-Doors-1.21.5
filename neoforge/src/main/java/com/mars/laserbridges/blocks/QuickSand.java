package com.mars.laserbridges.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class QuickSand extends HugeMushroomBlock {
    public QuickSand(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(context instanceof EntityCollisionContext entityContext){
            final Entity entity = entityContext.getEntity();

            if(entity != null){
                if(entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.ITEM){
                    return Shapes.empty();
                }
            }
        }
        return super.getCollisionShape(state, level, pos, context);
    }
    @SuppressWarnings("rawtypes")
    public void entrap(Entity entity, BlockState state, Vec3 motionMultiplier, EntityType filter){
        if(entity.getType() != filter){
            entity.makeStuckInBlock(state, motionMultiplier);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier p_405359_) {
        super.entityInside(state, level, pos, entity, p_405359_);
        entrap(entity, state, new Vec3((double)0.25F, (double)0.05F, (double)0.25F), EntityType.PLAYER);

    }
}
