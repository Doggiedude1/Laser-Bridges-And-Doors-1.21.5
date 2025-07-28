package com.mars.laserbridges.blocks;

import com.mars.laserbridges.component.BoostState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static com.mars.laserbridges.Laserbridges.BOOSTED;

public class BounceshroomBlock extends HugeMushroomBlock {

    public BounceshroomBlock(Properties p_54136_) {
        super(p_54136_);
    }
    /*@Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        AABB surface = Shapes.box(0,1,0,1,1.1,1).bounds();
        Iterator<Entity> entities = level.getAllEntities().iterator();
        while (entities.hasNext()){
            Entity entity = entities.next();
            System.out.println(entity);
            if(entity.getBoundingBox().intersects(surface)){
                boostEntity(entity, level);
                entities.remove();
            }
            else{
                System.out.println("not Colliding");
                entities.remove();
            }

        }
        super.tick(state, level, pos, random);
    }*/

    @Override
    public void fallOn(Level p_152426_, BlockState p_152427_, BlockPos p_152428_, Entity p_152429_, double p_397222_) {
        ParticleOptions po = ParticleTypes.GUST_EMITTER_SMALL;
        p_152426_.addParticle(po, p_152428_.getX(),p_152428_.getY() + 1, p_152428_.getZ(),1, 1, 1);
    }

    @Override
    public void updateEntityMovementAfterFallOn(BlockGetter p_56406_, Entity p_56407_) {
        if (p_56407_.isSuppressingBounce()) {
            this.boostEntity(p_56407_, false);
        } else if (!p_56407_.isSuppressingBounce()) {
            this.boostEntity(p_56407_, true);
        }

    }
    @Override
    public void stepOn(Level p_154573_, BlockPos p_154574_, BlockState p_154575_, Entity p_154576_) {
        ParticleOptions po = ParticleTypes.GUST_EMITTER_SMALL;

        if (!p_154576_.isSuppressingBounce()) {
            p_154573_.addParticle(po, p_154574_.getX(),p_154574_.getY() + 1, p_154574_.getZ(),1, 1, 1);
            this.boostEntity(p_154576_ , true);
        }
        else if (p_154576_.isSuppressingBounce()){
            p_154573_.addParticle(po, p_154576_.getX(),p_154574_.getY() + 1, p_154576_.getZ(),1, 1, 1);
            this.boostEntity(p_154576_, false);
        }
        super.stepOn(p_154573_, p_154574_, p_154575_, p_154576_);
    }

    public void boostEntity(Entity entity, boolean directional){
        BoostState boost;
        boost = entity.getData(BOOSTED.get());
        //if(boost.isBoosted()) return;
        boost.setBoosted(true);
        System.out.println("Boost");
        Vec3 vec3 = entity.getDeltaMovement();
        double xzMovement = 2;
        double yMovement = 1.5;
        System.out.println(Math.clamp(entity.getLookAngle().y + 1, 0, 1));
        double yMotion = Math.clamp(entity.getLookAngle().y + 1, 0, 1) * yMovement;
        double xzMotion = Math.clamp(entity.getLookAngle().y + 1, 0, 1)* xzMovement;
        double xMotion = entity.getLookAngle().x * xzMotion;
        double zMotion = entity.getLookAngle().z * xzMotion;
        //System.out.println(Math.clamp(entity.getLookAngle().y + 1, 0, 1));
        //System.out.println(xMotion + ", " + yMotion + ", " + zMotion);
        if(directional) {
            if (vec3.y <= (double) -0.1F) {
                if (yMotion == 0) yMotion = 0.5;
                //System.out.println(-vec3.y);
                entity.setDeltaMovement(vec3.x + xMotion, -0.0784000015258789 + yMotion, vec3.z + zMotion);
                //(-vec3.y * 1) + 0.5
            }
        }
        else
        {
            System.out.println((Math.clamp(Math.abs(vec3.y), 0, 10) * 1) + 1);
            entity.setDeltaMovement(vec3.x, (Math.clamp(Math.abs(vec3.y), 0, 10) * 1) + 0.2, vec3.z);
            //(-vec3.y * 1) + 0.5
        }
    }
}
