package com.mars.laserbridges.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import com.mars.laserbridges.entity.DecoyPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Command for spawning and managing decoy NPCs.
 */
public class DecoyCommand {
    private static final Map<UUID, DecoyPlayer> DECOYS = new HashMap<>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("decoy")
            .requires(cs -> cs.hasPermission(2))
            .then(Commands.literal("hide").executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                setHidden(player, true);
                return 1;
            }))
            .then(Commands.literal("unhide").executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                setHidden(player, false);
                return 1;
            }))
            .executes(ctx -> {
                ServerPlayer player = ctx.getSource().getPlayerOrException();
                spawnDecoy(player);
                return 1;
            }));
    }

    private static void spawnDecoy(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        GameProfile source = player.getGameProfile();
        GameProfile profile = new GameProfile(UUID.randomUUID(), source.getName());
        profile.getProperties().putAll(source.getProperties());

        DecoyPlayer decoy = new DecoyPlayer(level, profile);
        decoy.setPos(player.getX(), player.getY(), player.getZ());
        decoy.setYRot(player.getYRot());
        decoy.setXRot(player.getXRot());
        decoy.addTag("laserbridges_decoy");
        if (player.isCreative()) {
            decoy.addTag("laserbridges_no_hurt");
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST || slot == EquipmentSlot.LEGS ||
                slot == EquipmentSlot.FEET || slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
                ItemStack copy = player.getItemBySlot(slot).copy();
                decoy.setItemSlot(slot, copy);
            }
        }

        level.addFreshEntity(decoy);
        DECOYS.put(player.getUUID(), decoy);

        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 30, 0, false, false));
    }

    private static void setHidden(ServerPlayer player, boolean hide) {
        DecoyPlayer decoy = DECOYS.get(player.getUUID());
        if (decoy != null) {
            decoy.setInvisible(hide);
        }
    }

    public static DecoyPlayer getDecoy(Player player) {
        return DECOYS.get(player.getUUID());
    }
}
