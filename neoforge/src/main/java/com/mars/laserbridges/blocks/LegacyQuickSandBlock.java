package com.mars.laserbridges.blocks;

import com.mars.laserbridges.blocks.entity.LegacyQuickSandBlockEntity;
import com.mars.laserbridges.util.Owner;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiConsumer;

public class LegacyQuickSandBlock extends HugeMushroomBlock implements EntityBlock {
    public LegacyQuickSandBlock(Properties p_54136_) {
        super(p_54136_);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(level.getBlockEntity(pos) instanceof LegacyQuickSandBlockEntity blockEntity){
            if(placer == null) return;
            if(placer instanceof Player plr){
                blockEntity.setOwner(plr.getGameProfile().getId().toString(), plr.getName().getString());
            }
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier p_405359_) {
        super.entityInside(state, level, pos, entity, p_405359_);
        BlockEntity be = level.getBlockEntity(pos);
        if (level instanceof ServerLevel) {
            ServerLevel serverlevel = (ServerLevel)level;
            Vec3 vec3 = entity.isClientAuthoritative() ? entity.getKnownMovement() : entity.oldPosition().subtract(entity.position());
            if (vec3.horizontalDistanceSqr() > (double)0.0F) {
                System.out.println("now");
                entity.hurtServer(serverlevel, entity.damageSources().sweetBerryBush(), 1.0F);
            }
        }
        if(be instanceof LegacyQuickSandBlockEntity blockEntity)
        {
            Owner owner = blockEntity.getOwner();
            if(entity instanceof Player player){
                if(owner.getUUID() != player.getGameProfile().getId().toString()){
                    Vec3 vec3 = new Vec3((double)0.25F, (double)0.05F, (double)0.25F);
                    entity.makeStuckInBlock(state, vec3);
                }
            }
            else {
                if(owner.getUUID() != entity.getUUID().toString()){
                    Vec3 vec3 = new Vec3((double)0.25F, (double)0.05F, (double)0.25F);
                    entity.makeStuckInBlock(state, vec3);
                }
            }

        }
    }

    /*@Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState p_371936_, BlockGetter p_399929_, BlockPos p_371292_, Entity p_399852_) {
        AABB returnShape = Shapes.block().bounds();
        returnShape.inflate(0,0.2d, 0);
        return Shapes.create(returnShape);
    }*/

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if(context instanceof EntityCollisionContext entityContext){
            final Entity entity = entityContext.getEntity();

            if(entity != null){
                if(level.getBlockEntity(pos) instanceof LegacyQuickSandBlockEntity blockEntity){
                    Owner owner = blockEntity.getOwner();
                    if(entity instanceof Player plr){
                        if(!Objects.equals(plr.getGameProfile().getId().toString(), owner.getUUID())){
                            return Shapes.empty();
                        }
                    }
                    else{
                        if(!Objects.equals(entity.getUUID().toString(), owner.getUUID())) {
                            return Shapes.empty();
                        }
                    }
                }
            }
        }
        return super.getCollisionShape(state, level, pos, context);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {

        return new LegacyQuickSandBlockEntity(blockPos, blockState);
    }
    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if(be instanceof LegacyQuickSandBlockEntity blockEntity) {
            Owner owner = blockEntity.getOwner();
            if (Objects.equals(owner.getUUID(), player.getGameProfile().getId().toString())) {
                return defaultDestroyProgress(state, player, level, pos);
            }
        }
        return 0;
    }
    public float defaultDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public void onBlockExploded(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion) {
    }

    @Override
    protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
    }
}
