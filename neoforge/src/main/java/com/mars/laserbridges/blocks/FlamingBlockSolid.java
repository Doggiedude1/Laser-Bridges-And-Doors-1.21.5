package com.mars.laserbridges.blocks;

import com.mars.laserbridges.blocks.entity.FlamingSolidBlockEntity;
import com.mars.laserbridges.util.Owner;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class FlamingBlockSolid extends HugeMushroomBlock implements EntityBlock {
    private static BlockPos blockPos;
    static boolean shouldSavePos;
    static boolean shouldTick = true;
    static AABB trigger = Shapes.block().bounds().setMaxX(5).setMaxY(5).setMaxZ(5).setMinX(-5).setMinY(-5).setMinZ(-5);
    public FlamingBlockSolid(Properties p_54136_) {
        super(p_54136_);
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
    public void setBlockPos(BlockPos blockPos){
        FlamingBlockSolid.blockPos = blockPos;
    }
    public static void setShouldSavePos(boolean bool){
        shouldSavePos = bool;
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


    @Override
    public void setPlacedBy(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        setBlockPos(pos);
        if(level.getBlockEntity(pos) instanceof FlamingSolidBlockEntity blockEntity){
            if(placer == null) return;
            if(placer instanceof Player plr){
                blockEntity.setOwner(plr.getGameProfile().getId().toString(), plr.getName().getString());
            }
            //NeoForge.EVENT_BUS.post(new FlamingBlockPlace(placer, pos, level));
        }
    }

    public static List<Entity> getNearbyEntities(Level level, BlockPos blockPos, double radius){
        if(blockPos == null) return null;
        AABB box = AABB.ofSize(blockPos.getCenter(), radius * 2, radius*2, radius*2);
        return level.getEntities((Entity) null, box, entity -> true);
    }
    public static boolean nearbyEntities(Level level, BlockPos blockPos, double radius){
        if(blockPos == null) return false;
        AABB box = AABB.ofSize(blockPos.getCenter(), radius * 2, radius*2, radius*2);
        return level.getEntities((Entity) null, box, entity -> true)
                .stream()
                .anyMatch(entity -> true);
    }



    void targetEntity(ServerLevel level,Entity entity, double radius, int delay, BlockPos blockPos){
        teleportRand(level, entity, radius*2);
        //if(EntityMotionQueue.currentlyContainsEntity(entity,new Vec3(blockPos.getX(), blockPos.getY(),blockPos.getZ()))) return;
        //NeoForge.EVENT_BUS.post(new FlamingBlockCollision(entity,new Vec3(blockPos.getX(), blockPos.getY(),blockPos.getZ()), delay, radius,level, blockPos));
        //NeoForge.EVENT_BUS.post(new FlamingBlockCollision(entity));
    }
    @Override
    protected void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity, @NotNull InsideBlockEffectApplier p_405359_) {
        super.entityInside(state, level, pos, entity, p_405359_);
        BlockEntity be = level.getBlockEntity(pos);
        if(be instanceof FlamingSolidBlockEntity blockEntity)
        {
            Owner owner = blockEntity.getOwner();
            if(entity instanceof Player player){
                if(!Objects.equals(player.getGameProfile().getId().toString(), owner.getUUID())){
                    if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
                        if(getBlockPos() != null) {
                            targetEntity(serverLevel, entity, 64, 120, getBlockPos());
                        }
                        else {
                            setBlockPos(pos);
                        }

                    }
                }
            }
            else {
                if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
                    if(getBlockPos() != null) {
                        targetEntity(serverLevel, entity, 64, 120, getBlockPos());
                    }
                    else {
                        setBlockPos(pos);
                    }
                }
            }

        }
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (context instanceof EntityCollisionContext entityContext){
            final Entity entity = entityContext.getEntity();

            if(entity != null){
                if(level.getBlockEntity(pos) instanceof FlamingSolidBlockEntity blockEntity){
                    Owner owner = blockEntity.getOwner();
                    if(entity instanceof Player plr){
                        System.out.println("Plr: " + plr.getGameProfile().getId().toString() + ", Owner: " + owner.getUUID());
                        if(Objects.equals(plr.getGameProfile().getId().toString(), owner.getUUID())){
                            return super.getCollisionShape(state, level, pos, context);
                        }
                    }
                }
            }
        }
        return Shapes.empty();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {

        return new FlamingSolidBlockEntity(blockPos, blockState);
    }
    @Override
    public float getDestroyProgress(@NotNull BlockState state, @NotNull Player player, BlockGetter level, @NotNull BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if(be instanceof FlamingSolidBlockEntity blockEntity) {
            Owner owner = blockEntity.getOwner();
            if (Objects.equals(owner.getUUID(), player.getGameProfile().getId().toString())) {
                return defaultDestroyProgress(state, player, level, pos);
            }
        }
        return 0;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        shouldTick = false;
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    public float defaultDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public void onBlockExploded(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Explosion explosion) {
    }

    @Override
    protected void onExplosionHit(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Explosion explosion, @NotNull BiConsumer<ItemStack, BlockPos> dropConsumer) {
    }
}
