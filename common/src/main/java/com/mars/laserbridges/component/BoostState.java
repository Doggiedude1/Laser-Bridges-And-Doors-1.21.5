package com.mars.laserbridges.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import static com.mars.laserbridges.Constants.BOUNCESHROOM_BOOSTED_KEY;

public class BoostState
{
    private boolean boosted = false;

    public BoostState() {}


    public boolean isBoosted() {
        return boosted;
    }

    public void setBoosted(Boolean b){
        this.boosted = b;
    }

    /**
     * Codec for (de)serializing this into NBT under the hood.
     * You only need this if you passed `.codec(BoostState.CODEC)` to your AttachmentType builder.
     */
    public static final Codec<BoostState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.BOOL.fieldOf(BOUNCESHROOM_BOOSTED_KEY).forGetter(BoostState::isBoosted)
    ).apply(inst, (_boosted) -> {
        BoostState st = new BoostState();
        st.setBoosted(_boosted);
        return st;
    }));
}
