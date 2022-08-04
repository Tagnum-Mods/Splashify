package com.tagnumelite.splashify;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.tagnumelite.splashify.api.Splash;
import com.tagnumelite.splashify.api.Splashes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class SplashResourceListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public SplashResourceListener() {
        super(GSON, "splashes");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> jsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsonElementMap.entrySet()) {
            Splash.CODEC.parse(JsonOps.INSTANCE, entry.getValue().getAsJsonObject().get("splashes"))
                    .resultOrPartial(error -> Splashify.LOGGER.error("Failed to parse json {}: {}", entry.getKey(), error))
                    .ifPresent(a -> Splashes.SPLASHES.put(entry.getKey(), a));
        }
    }
}
