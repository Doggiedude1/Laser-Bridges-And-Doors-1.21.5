package com.mars.laserbridges.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PowerfulQuickSandBlock extends HugeMushroomBlock
{
    public PowerfulQuickSandBlock(Properties p_54136_) {
        super(p_54136_);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(context instanceof EntityCollisionContext entityContext){
            final Entity entity = entityContext.getEntity();
            if(entity != null){
                if(entity.getType() != EntityType.PLAYER && entity.getType().getCategory() != MobCategory.MISC){
                    return Shapes.empty();
                }
            }

        }
        return super.getCollisionShape(state, level, pos, context);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier p_405359_) {
        super.entityInside(state, level, pos, entity, p_405359_);
        if(entity.getType() != EntityType.PLAYER && entity.getType().getCategory() != MobCategory.MISC){
            entity.makeStuckInBlock(state, new Vec3((double)0.125F, (double)0.025F, (double)0.125F));
        }

    }
}
