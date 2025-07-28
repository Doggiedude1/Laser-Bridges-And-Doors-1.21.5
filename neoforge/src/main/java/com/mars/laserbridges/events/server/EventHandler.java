package com.mars.laserbridges.events.server;

import com.mars.laserbridges.util.EntityMotionQueue;
import com.mars.laserbridges.util.RewindEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class EventHandler
{
    @SubscribeEvent
    public static void flamingBlockCollision(FlamingBlockCollision event){
        EntityMotionQueue.retriveData(event.getEntity(), event.getVec3(), event.getDelay(), event.getDouble(), event.getServerLevel(), event.getBlockPos());
    }
    @SubscribeEvent
    public static void flamingBlockPlace(FlamingBlockPlace event){
        RewindEntity.addBlock(event.getBlockPos(), event.getLevel());
    }
}
