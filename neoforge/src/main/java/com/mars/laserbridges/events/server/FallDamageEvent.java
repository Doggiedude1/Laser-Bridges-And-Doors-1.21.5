package com.mars.laserbridges.events.server;


import com.mars.laserbridges.component.BoostState;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;

import static com.mars.laserbridges.Constants.MOD_ID;
import static com.mars.laserbridges.Laserbridges.BOOSTED;

@EventBusSubscriber(modid = MOD_ID)
public class FallDamageEvent
{
    /*@SubscribeEvent
    public static void onFallDamage(LivingIncomingDamageEvent event) {
        Entity entity = event.getEntity();
        BoostState state = entity.getData(BOOSTED.get());
        if(state.isBoosted())
        {
            System.out.println("Falling or Fell");

            event.setCanceled(true);

            entity.setData(BOOSTED.get(), new BoostState());

        }
    }*/
    @SubscribeEvent
     public static void onFall(LivingFallEvent event) {

        Entity entity = event.getEntity();
        BoostState state = entity.getData(BOOSTED.get());

        if(state.isBoosted() && event.getDistance() >= 0.125)//entity.getDeltaMovement().y <= -0.8
        {
            System.out.println(event.getDistance());
            event.setCanceled(true);
            entity.setData(BOOSTED.get(), new BoostState());
        }
        else if(state.isBoosted())
        {
            System.out.println(event.getDistance());
        }
    }
}
