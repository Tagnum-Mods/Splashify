package com.tagnumelite.splashify.mixin;

import com.tagnumelite.splashify.Splashify;
import com.tagnumelite.splashify.api.RandomWeightedMap;
import com.tagnumelite.splashify.api.Splash;
import com.tagnumelite.splashify.api.Splashes;
import com.tagnumelite.splashify.api.Util;
import net.minecraft.client.User;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.loading.FMLLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(SplashManager.class)
public abstract class MixinSplashManager {
    private static final RandomWeightedMap<Splash> SPLASHES = new RandomWeightedMap<>();

    @Shadow
    @Final
    private User user;

    @Inject(method = "apply(Ljava/util/List;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("RETURN"))
    private void onApply(List<String> vanillaSplashes, ResourceManager manager, ProfilerFiller profiler, CallbackInfo ci) {
        SPLASHES.clear();

        SPLASHES.addAll(0.0, vanillaSplashes.stream().map(s -> new Splash(s, Collections.emptyList(), 0.0)).toList());

        SPLASHES.add(0, new Splash(this.user.getName().toUpperCase(Locale.ROOT) + " IS YOU"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // TODO: Find good random weight for all dates
        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.DATE) == 24) {
            SPLASHES.add(Double.MAX_VALUE / 4, new Splash("Merry X-mas!"));
        } else if (calendar.get(Calendar.MONTH) == Calendar.JANUARY && calendar.get(Calendar.DATE) == 1) {
            SPLASHES.add(Double.MAX_VALUE / 4, new Splash("Happy new year!"));
        } else if (calendar.get(Calendar.MONTH) == Calendar.OCTOBER && calendar.get(Calendar.DATE) == 31) {
            SPLASHES.add(Double.MAX_VALUE / 4, new Splash("OOoooOOOoooo! Spooky!"));
        }

        for (Map.Entry<ResourceLocation, Splash> entry : Splashes.SPLASHES.entrySet()) {
            ResourceLocation location = entry.getKey();
            Splash splash = entry.getValue();
            if (location.getNamespace().equals("debug") && FMLLoader.isProduction()) continue;

            if (Util.confirmRequirements(location, user, splash.requirements())) {
                SPLASHES.add(splash.weight(), splash);
            }
        }
    }

    @Inject(method = "getSplash", at = @At("RETURN"), cancellable = true)
    private void onGetSplash(CallbackInfoReturnable<String> cir) {
        String originalSplash = cir.getReturnValue();
        String newSplash = SPLASHES.next().value().formatted(user.getName());

        if (!FMLLoader.isProduction()) {
            Splashify.LOGGER.debug("Changed original splash from {} to {}", originalSplash, newSplash);
        }

        if (originalSplash == null) {
            cir.setReturnValue("Failed to get splash");
            return;
        }
        cir.setReturnValue(newSplash);
    }
}
