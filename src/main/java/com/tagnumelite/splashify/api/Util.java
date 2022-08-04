package com.tagnumelite.splashify.api;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.tagnumelite.splashify.Splashify;
import net.minecraft.client.User;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Util {
    public static final Map<String, BiFunction<Dynamic<?>, User, Boolean>> REQUIREMENTS_CONSUMER = new HashMap<>();

    public static boolean confirmRequirements(final ResourceLocation location, final User user, List<Pair<String, Dynamic<?>>> requirements) {
        for (Pair<String, Dynamic<?>> requirement : requirements) {
            String key = requirement.getFirst();
            Dynamic<?> value = requirement.getSecond();
            if (!REQUIREMENTS_CONSUMER.containsKey(key)) {
                Splashify.LOGGER.warn("Splashify ({}) has no consumers for: {}", location, key);
                continue;
            }

            BiFunction<Dynamic<?>, User, Boolean> consumer = REQUIREMENTS_CONSUMER.get(key);
            if (!consumer.apply(value, user)) {
                return false;
            }
        }
        return true;
    }
}
