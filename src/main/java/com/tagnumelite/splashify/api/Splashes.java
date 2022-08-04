package com.tagnumelite.splashify.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class Splashes {
    public static final Map<ResourceLocation, Splash> SPLASHES = new HashMap<>();
}
