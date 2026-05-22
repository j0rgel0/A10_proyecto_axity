package com.parque.dinosaurios.simulation;

import java.util.List;
import java.util.Random;

public class DefaultRandomProvider implements RandomProvider {

    private final Random random;

    public DefaultRandomProvider() {
        this(new Random());
    }

    public DefaultRandomProvider(Random random) {
        this.random = random;
    }

    @Override
    public int nextIntBetween(int minInclusive, int maxInclusive) {
        if (maxInclusive < minInclusive) {
            return minInclusive;
        }
        return random.nextInt(maxInclusive - minInclusive + 1) + minInclusive;
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }

    @Override
    public boolean chance(double probability) {
        return nextDouble() < Math.max(0.0, Math.min(1.0, probability));
    }

    @Override
    public <T> T pick(List<T> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("No hay elementos para seleccionar");
        }
        return values.get(nextIntBetween(0, values.size() - 1));
    }
}
