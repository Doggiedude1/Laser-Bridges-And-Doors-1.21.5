package com.mars.laserbridges.blocks;

import com.mars.laserbridges.blocks.entity.LegacyQuickSandBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.UUID;

public class RegularQuickSandBlock extends HugeMushroomBlock {
    public RegularQuickSandBlock(Properties p_54136_) {
        super(p_54136_);
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
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier p_405359_) {
        super.entityInside(state, level, pos, entity, p_405359_);
        /*if (level instanceof ServerLevel) {
            ServerLevel serverlevel = (ServerLevel)level;
            Vec3 vec3 = entity.isClientAuthoritative() ? entity.getKnownMovement() : entity.oldPosition().subtract(entity.position());
            if (vec3.horizontalDistanceSqr() > (double)0.0F) {
                double d0 = Math.abs(vec3.x());
                double d1 = Math.abs(vec3.z());
                if (d0 >= (double)0.003F || d1 >= (double)0.003F) {
                    entity.hurtServer(serverlevel, entity.damageSources().sweetBerryBush(), 1.0F);
                }
            }
        }*/
        if(entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.ITEM){
            Vec3 vec3 = new Vec3((double)0.25F, (double)0.05F, (double)0.25F);
            entity.makeStuckInBlock(state, vec3);
        }
    }
}
