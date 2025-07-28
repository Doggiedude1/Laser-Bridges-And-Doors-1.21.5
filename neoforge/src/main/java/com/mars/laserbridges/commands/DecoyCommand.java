package com.mars.laserbridges.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.BlockPos;

public class DecoyCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("decoy")
                .requires(cs -> cs.hasPermission(2))
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    spawnDecoy(player);
                    return 1;
                })
        );
    }

    private static void spawnDecoy(ServerPlayer player) {
        ArmorStand stand = new ArmorStand(EntityType.ARMOR_STAND, player.serverLevel());
        stand.setPos(player.getX(), player.getY(), player.getZ());
        stand.setYRot(player.getYRot());
        stand.setXRot(player.getXRot());
        stand.setCustomName(player.getName());
        stand.setCustomNameVisible(true);
        stand.setInvulnerable(true);
        stand.setNoGravity(true);
        stand.addTag("laserbridges_decoy");

        // Copy armor
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor()) {
                ItemStack copy = player.getItemBySlot(slot).copy();
                stand.setItemSlot(slot, copy);
            }
        }
        // Copy helmet or leave empty
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD).copy();
        stand.setItemSlot(EquipmentSlot.HEAD, head);

        player.serverLevel().addFreshEntity(stand);

        // make player invisible
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 30, 0, false, false));
    }
}
