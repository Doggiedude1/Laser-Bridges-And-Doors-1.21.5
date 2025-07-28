package com.mars.laserbridges;


import com.google.common.base.Predicates;
import com.mars.laserbridges.blocks.*;
import com.mars.laserbridges.blocks.entity.FlamingBlockEntity;
import com.mars.laserbridges.blocks.entity.FlamingSolidBlockEntity;
import com.mars.laserbridges.blocks.entity.LegacyQuickSandBlockEntity;
import com.mars.laserbridges.component.BoostState;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static com.mars.laserbridges.Constants.*;

@Mod(MOD_ID)
public class Laserbridges {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MOD_ID);
    //public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Owner>> OWNER_SERIALIZER = DATA_SERIALIZERS.register("owner", () -> new OwnerDataSerializer());
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);




    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BoostState>> BOOSTED =
            ATTACHMENTS.register(BOUNCESHROOM_BOOSTED_KEY, () ->
                    AttachmentType.builder(BoostState::new).serialize(BoostState.CODEC).build()
            );


    public static final DeferredBlock<Block> REGULAR_QUICKSAND_BLOCK = BLOCKS.registerBlock("regular_quicksand", RegularQuickSandBlock::new, BlockBehaviour.Properties.of()
            .strength(0.5F)
            .noOcclusion());
    public static final DeferredBlock<Block> POWERFUL_QUICKSAND_BLOCK = BLOCKS.registerBlock("powerful_quicksand", PowerfulQuickSandBlock::new, BlockBehaviour.Properties.of()
            .strength(0.5F)
            .noOcclusion());
    public static final DeferredBlock<Block> LEGACY_QUICKSAND_BLOCK = BLOCKS.registerBlock("legacy_quicksand", LegacyQuickSandBlock::new, BlockBehaviour.Properties.of()
            .strength(0.5F)
            .noOcclusion()
            .pushReaction(PushReaction.BLOCK));
    public static final DeferredBlock<Block> FLAMING_BLOCK = BLOCKS.registerBlock("flaming_block", FlamingBlock::new, BlockBehaviour.Properties.of()
            .strength(0.5F)
            .noOcclusion()
            .pushReaction(PushReaction.BLOCK));
    public static final DeferredBlock<Block> FLAMING_SOLID_BLOCK = BLOCKS.registerBlock("flaming_solid_block", FlamingBlockSolid::new, BlockBehaviour.Properties.of()
            .strength(0.5F)
            .noOcclusion()
            .pushReaction(PushReaction.BLOCK));

    public static final DeferredBlock<Block> BOUNCESHROOM_BLOCK_BLOCK = BLOCKS.registerBlock(BOUNCESHROOM_BLOCK_NAME, BounceshroomBlock::new, Blocks.BROWN_MUSHROOM_BLOCK.properties());
    public static final DeferredItem<BlockItem> BOUNCESHROOM_BLOCK_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(BOUNCESHROOM_BLOCK_NAME, BOUNCESHROOM_BLOCK_BLOCK);
    public static final DeferredItem<BlockItem> LEGACY_QUICKSAND_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("legacy_quicksand",LEGACY_QUICKSAND_BLOCK);
    public static final DeferredItem<BlockItem> REGULAR_QUICKSAND_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("regular_quicksand",REGULAR_QUICKSAND_BLOCK);
    public static final DeferredItem<BlockItem> POWERFUL_QUICKSAND_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("powerful_quicksand",POWERFUL_QUICKSAND_BLOCK);
    public static final DeferredItem<BlockItem> FLAMING_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("flaming_block",FLAMING_BLOCK);
    public static final DeferredItem<BlockItem> FLAMING_SOLID_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("flaming_solid_block",FLAMING_SOLID_BLOCK);

    public static final Supplier<BlockEntityType<LegacyQuickSandBlockEntity>> LEGACY_QUICKSAND_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "legacy_quicksand_block_entity",
            () -> new BlockEntityType<>(
                    LegacyQuickSandBlockEntity::new,
                    false,
                    LEGACY_QUICKSAND_BLOCK.get()
            )
    );

    public static final Supplier<BlockEntityType<FlamingBlockEntity>> FLAMING_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "flaming_block_entity",
            () -> new BlockEntityType<>(
                    FlamingBlockEntity::new,
                    false,
                    FLAMING_BLOCK.get()
            )
    );
    public static final Supplier<BlockEntityType<FlamingSolidBlockEntity>> FLAMING_SOLID_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "flaming_solid_block_entity",
            () -> new BlockEntityType<>(
                    FlamingSolidBlockEntity::new,
                    false,
                    FLAMING_SOLID_BLOCK.get()
            )
    );



    public static final DeferredHolder<SoundEvent, SoundEvent> ON = registerSoundEvent(ON_NAME);
    public static final DeferredHolder<SoundEvent, SoundEvent> OFF = registerSoundEvent(OFF_NAME);
    public Laserbridges(IEventBus modEventBus) {

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);
        ATTACHMENTS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::clientSetup);
    }


    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
            //event.register((state, level, pos, tintIndex) -> (DyeColor.byId(state.getValue(COLOR))).getTextureDiffuseColor(), COLORED_BLOCK.value());
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(LEGACY_QUICKSAND_BLOCK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FLAMING_BLOCK.get(), RenderType.translucent());
        //ItemBlockRenderTypes.setRenderLayer(COLORED_BLOCK.get(), RenderType.translucent());
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS){
            event.accept(LEGACY_QUICKSAND_BLOCK_ITEM);
            event.accept(REGULAR_QUICKSAND_BLOCK_ITEM);
            event.accept(POWERFUL_QUICKSAND_BLOCK_ITEM);
            event.accept(FLAMING_BLOCK_ITEM);
            event.accept(FLAMING_SOLID_BLOCK_ITEM);
        }
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS){
            event.accept(BOUNCESHROOM_BLOCK_BLOCK_ITEM);
        }
    }




    public static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name){
        return SOUND_EVENTS.register(name, () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, name), 75f));
    }
    public static ResourceLocation resLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static ResourceLocation mcResLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }


}

