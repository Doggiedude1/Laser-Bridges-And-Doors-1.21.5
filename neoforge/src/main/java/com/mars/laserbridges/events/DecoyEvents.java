package com.mars.laserbridges.events;

import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class DecoyEvents {
    @SubscribeEvent
    public static void onAttack(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof ArmorStand stand && stand.getTags().contains("laserbridges_decoy")) {
            Level level = stand.level();
            level.broadcastEntityEvent(stand, (byte)2); // hurt animation
            event.setCanceled(true);
        }
    }
}
