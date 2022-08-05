package com.tagnumelite.splashify.api;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

// Adapted from https://stackoverflow.com/a/6409791,
// TODO: Improve by implementing some interfaces.
public class RandomWeightedMap<O> {
    private final Random random;
    private final NavigableMap<Double, O> map = new TreeMap<>();
    private double total = 0;

    public RandomWeightedMap() {
        this(new Random());
    }

    public RandomWeightedMap(Random random) {
        this.random = random;
    }

    public void add(double weight, O result) {
        if (weight <= 0) weight = 1.0;
        total += weight;
        map.put(total, result);
    }

    public void addAll(double weight, Iterable<O> collection) {
        for (O obj : collection) {
            add(weight, obj);
        }
    }

    public O next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    public void clear() {
        total = 0;
        map.clear();
    }
}
