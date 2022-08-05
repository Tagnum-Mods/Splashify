package com.tagnumelite.splashify.api;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Splash(String value, List<Pair<String, Dynamic<?>>> requirements, Double weight) {
    public static final Codec<Splash> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("value").forGetter(Splash::value),
                    Codec.compoundList(Codec.STRING, Codec.PASSTHROUGH).optionalFieldOf("requirements", new ArrayList<>()).forGetter(Splash::requirements),
                    Codec.DOUBLE.optionalFieldOf("weight", 1.0).forGetter(Splash::weight)
            ).apply(instance, Splash::new));

    public Splash(String value) {
        this(value, 1.0);
    }

    public Splash(String value, List<Pair<String, Dynamic<?>>> requirements) {
        this(value, requirements, 1.0);
    }

    public Splash(String value, Double weight) {
        this(value, Collections.emptyList(), weight);
    }
}
