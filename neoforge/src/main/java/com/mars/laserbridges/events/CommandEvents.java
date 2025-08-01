package com.mars.laserbridges.events;

import com.mars.laserbridges.commands.DecoyCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static com.mars.laserbridges.Constants.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class CommandEvents {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        DecoyCommand.register(dispatcher);
    }
}
