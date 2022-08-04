package com.tagnumelite.splashify.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tagnumelite.splashify.Splashify;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
    @Shadow
    @Nullable
    private String splash;

    protected MixinTitleScreen(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void onRender(PoseStack p_96739_, int p_96740_, int p_96741_, float p_96742_, CallbackInfo ci) {
        if (Splashify.REFRESH_SPLASH) {
            Splashify.REFRESH_SPLASH = false;
            splash = minecraft.getSplashManager().getSplash();
        }
    }
}
