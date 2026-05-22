package com.parque.dinosaurios.simulation;

import java.util.List;

public interface RandomProvider {

    int nextIntBetween(int minInclusive, int maxInclusive);

    double nextDouble();

    boolean chance(double probability);

    <T> T pick(List<T> values);
}
