package com.tagnumelite.splashify;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Mod(Splashify.MODID)
public class Splashify {
    public static final String MODID = "splashify";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Boolean REFRESH_SPLASH = false;

    public Splashify() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        modBus.addListener(this::onMessageProcess);
        modBus.addListener(this::onMessageReceived);
    }

    @SubscribeEvent
    public void onKeyEvent(final InputEvent.Key event) {
        if (event.getKey() == GLFW.GLFW_KEY_R
                && event.getAction() == GLFW.GLFW_PRESS
                && event.getModifiers() == GLFW.GLFW_MOD_CONTROL + GLFW.GLFW_MOD_SHIFT
                && Minecraft.getInstance().screen instanceof TitleScreen) {
            REFRESH_SPLASH = true;
        }
    }

    @SubscribeEvent
    public void onAddResourceListeners(AddReloadListenerEvent event) {
    }
}
