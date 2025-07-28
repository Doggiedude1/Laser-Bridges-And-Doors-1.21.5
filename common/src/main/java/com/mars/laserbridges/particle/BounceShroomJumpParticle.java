package com.mars.laserbridges.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class BounceShroomJumpParticle extends TextureSheetParticle
{
    public BounceShroomJumpParticle(SpriteSet spriteSet, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed) {
        super(level, x, y, z);
        setSize(0.01F, 0.01F);
        pickSprite(spriteSet);
        quadSize *= random.nextFloat() * 0.6F + 0.4F;
        hasPhysics = false;
        xd = 0;
        yd = ySpeed;
        zd = 0;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BounceShroomJumpParticle particle = new BounceShroomJumpParticle(spriteSet, level, x, y, z, 0, 1 * 0.0075D);

            particle.lifetime = Mth.randomBetweenInclusive(level.random, 10, 40);
            particle.gravity = 0.0F;
            return particle;
        }
    }

}
