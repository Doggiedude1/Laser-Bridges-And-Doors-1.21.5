package com.mars.laserbridges.entity;

import com.mojang.authlib.GameProfile;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ServerboundBlockEntityTagQueryPacket;
import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ServerboundChatAckPacket;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.network.protocol.game.ServerboundEntityTagQueryPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket;
import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.stats.Stat;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Simple fake player used for decoy NPCs.
 */
public class DecoyPlayer extends ServerPlayer {
    public DecoyPlayer(ServerLevel level, GameProfile profile) {
        super(level.getServer(), level, profile, ClientInformation.createDefault());
        this.connection = new DecoyNetHandler(level.getServer(), this);
        this.setInvulnerable(true);
    }

    @Override
    public void displayClientMessage(Component chatComponent, boolean actionBar) {}

    @Override
    public void awardStat(Stat<?> stat, int amount) {}

    @Override
    public boolean canHarmPlayer(Player player) {
        return false;
    }

    @Override
    public void die(DamageSource source) {}

    @Override
    public void tick() {}

    @Override
    public void updateOptions(ClientInformation options) {}

    @Override
    public OptionalInt openMenu(@Nullable MenuProvider menuProvider, @Nullable Consumer<RegistryFriendlyByteBuf> extraDataWriter) {
        return OptionalInt.empty();
    }

    @Override
    public void openHorseInventory(AbstractHorse horse, Container container) {}

    @Override
    public boolean startRiding(Entity entity, boolean force) {
        return false;
    }

    @Override
    @Nullable
    public MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @Override
    public boolean isFakePlayer() {
        return true;
    }

    @ParametersAreNonnullByDefault
    private static class DecoyNetHandler extends ServerGamePacketListenerImpl {
        private static final net.minecraft.network.Connection DUMMY_CONNECTION = new DecoyConnection();

        public DecoyNetHandler(MinecraftServer server, ServerPlayer player) {
            super(server, DUMMY_CONNECTION, player, CommonListenerCookie.createInitial(player.getGameProfile(), false));
        }

        @Override public void tick() {}
        @Override public void resetPosition() {}
        @Override public void disconnect(Component message) {}
        @Override public void handlePlayerInput(ServerboundPlayerInputPacket packet) {}
        @Override public void handleMoveVehicle(ServerboundMoveVehiclePacket packet) {}
        @Override public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket packet) {}
        @Override public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket packet) {}
        @Override public void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket packet) {}
        @Override public void handleSeenAdvancements(ServerboundSeenAdvancementsPacket packet) {}
        @Override public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket packet) {}
        @Override public void handleSetCommandBlock(ServerboundSetCommandBlockPacket packet) {}
        @Override public void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket packet) {}
        @Override public void handleRenameItem(ServerboundRenameItemPacket packet) {}
        @Override public void handleSetBeaconPacket(ServerboundSetBeaconPacket packet) {}
        @Override public void handleSetStructureBlock(ServerboundSetStructureBlockPacket packet) {}
        @Override public void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket packet) {}
        @Override public void handleJigsawGenerate(ServerboundJigsawGeneratePacket packet) {}
        @Override public void handleSelectTrade(ServerboundSelectTradePacket packet) {}
        @Override public void handleEditBook(ServerboundEditBookPacket packet) {}
        @Override public void handleEntityTagQuery(ServerboundEntityTagQueryPacket packet) {}
        @Override public void handleBlockEntityTagQuery(ServerboundBlockEntityTagQueryPacket packet) {}
        @Override public void handleMovePlayer(ServerboundMovePlayerPacket packet) {}
        @Override public void teleport(double x, double y, double z, float yaw, float pitch) {}
        @Override public void handlePlayerAction(ServerboundPlayerActionPacket packet) {}
        @Override public void handleUseItemOn(ServerboundUseItemOnPacket packet) {}
        @Override public void handleUseItem(ServerboundUseItemPacket packet) {}
        @Override public void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket packet) {}
        @Override public void handleResourcePackResponse(ServerboundResourcePackPacket packet) {}
        @Override public void handlePaddleBoat(ServerboundPaddleBoatPacket packet) {}
        @Override public void onDisconnect(DisconnectionDetails details) {}
        @Override public void send(Packet<?> packet) {}
        @Override public void handleSetCarriedItem(ServerboundSetCarriedItemPacket packet) {}
        @Override public void handleChat(ServerboundChatPacket packet) {}
        @Override public void handleAnimate(ServerboundSwingPacket packet) {}
        @Override public void handlePlayerCommand(ServerboundPlayerCommandPacket packet) {}
        @Override public void handleInteract(ServerboundInteractPacket packet) {}
        @Override public void handleClientCommand(ServerboundClientCommandPacket packet) {}
        @Override public void handleContainerClose(ServerboundContainerClosePacket packet) {}
        @Override public void handleContainerClick(ServerboundContainerClickPacket packet) {}
        @Override public void handlePlaceRecipe(ServerboundPlaceRecipePacket packet) {}
        @Override public void handleContainerButtonClick(ServerboundContainerButtonClickPacket packet) {}
        @Override public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket packet) {}
        @Override public void handleSignUpdate(ServerboundSignUpdatePacket packet) {}
        @Override public void handleKeepAlive(ServerboundKeepAlivePacket packet) {}
        @Override public void handleCustomPayload(ServerboundCustomPayloadPacket packet) {}
        @Override public void handleClientInformation(ServerboundClientInformationPacket packet) {}
        @Override public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket packet) {}
        @Override public void handleChangeDifficulty(ServerboundChangeDifficultyPacket packet) {}
        @Override public void handleLockDifficulty(ServerboundLockDifficultyPacket packet) {}
        @Override public void teleport(PositionMoveRotation posMoveRot, Set<Relative> relatives) {}
        @Override public void ackBlockChangesUpTo(int sequence) {}
        @Override public void handleChatCommand(ServerboundChatCommandPacket packet) {}
        @Override public void handleChatAck(ServerboundChatAckPacket packet) {}
        @Override public void sendPlayerChatMessage(PlayerChatMessage message, ChatType.Bound boundChatType) {}
        @Override public void sendDisguisedChatMessage(Component content, ChatType.Bound boundChatType) {}
        @Override public void handleChatSessionUpdate(ServerboundChatSessionUpdatePacket packet) {}
    }

    private static final class DecoyConnection extends net.minecraft.network.Connection {
        public DecoyConnection() {
            super(PacketFlow.SERVERBOUND);
        }

        @Override
        public void setListenerForServerboundHandshake(PacketListener listener) {}
    }
}
