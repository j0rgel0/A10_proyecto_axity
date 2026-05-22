package com.parque.dinosaurios.support;

import com.parque.dinosaurios.simulation.RandomProvider;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class StubRandomProvider implements RandomProvider {

    private final Queue<Integer> integers = new ArrayDeque<>();
    private final Queue<Double> doubles = new ArrayDeque<>();

    public StubRandomProvider addInt(int value) {
        integers.add(value);
        return this;
    }

    public StubRandomProvider addDouble(double value) {
        doubles.add(value);
        return this;
    }

    @Override
    public int nextIntBetween(int minInclusive, int maxInclusive) {
        int value = integers.isEmpty() ? minInclusive : integers.remove();
        return Math.max(minInclusive, Math.min(maxInclusive, value));
    }

    @Override
    public double nextDouble() {
        return doubles.isEmpty() ? 0.0 : doubles.remove();
    }

    @Override
    public boolean chance(double probability) {
        return nextDouble() < probability;
    }

    @Override
    public <T> T pick(List<T> values) {
        return values.get(nextIntBetween(0, values.size() - 1));
    }
}
