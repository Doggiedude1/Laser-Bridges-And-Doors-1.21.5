package com.mars.laserbridges.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber
public class DecoyEvents {
    @SubscribeEvent
    public static void onAttack(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player pl && pl.getTags().contains("laserbridges_decoy")) {
            if (!pl.getTags().contains("laserbridges_no_hurt")) {
                Level level = pl.level();
                level.broadcastEntityEvent(pl, (byte)2); // hurt animation
            }
            event.setCanceled(true);
        }
    }
}
