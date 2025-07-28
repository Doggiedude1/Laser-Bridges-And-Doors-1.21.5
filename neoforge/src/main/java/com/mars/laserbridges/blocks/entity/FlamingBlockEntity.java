package com.mars.laserbridges.blocks.entity;

import com.mars.laserbridges.util.IOwnable;
import com.mars.laserbridges.util.ITickingBlockEntity;
import com.mars.laserbridges.util.Owner;
import com.mars.laserbridges.util.RewindEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.mars.laserbridges.Laserbridges.FLAMING_BLOCK_ENTITY;

public class FlamingBlockEntity extends BlockEntity implements IOwnable {

    private Owner owner = new Owner(); // or however you set it

    public FlamingBlockEntity(BlockPos pos, BlockState blockState) {
        super(FLAMING_BLOCK_ENTITY.get(), pos, blockState);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        owner.load(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (owner != null)
            owner.save(tag, needsValidation());
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);

        owner.save(tag, true);

        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public Owner getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String uuid, String name) {
        owner.set(uuid, name);
    }
}
