package com.parque.dinosaurios.simulation.factory;

import com.parque.dinosaurios.domain.enums.TouristType;
import com.parque.dinosaurios.domain.model.Tourist;
import com.parque.dinosaurios.simulation.RandomProvider;
import org.springframework.stereotype.Component;

@Component
public class TouristFactory {

    private final RandomProvider randomProvider;

    public TouristFactory(RandomProvider randomProvider) {
        this.randomProvider = randomProvider;
    }

    public Tourist create(int sequence) {
        TouristType type = pickType();
        int baseSatisfaction = switch (type) {
            case CHILD -> 78;
            case VIP -> 88;
            case ADULT -> 72;
        };
        return new Tourist("TUR-" + sequence, type, baseSatisfaction + randomProvider.nextIntBetween(-8, 8));
    }

    private TouristType pickType() {
        double value = randomProvider.nextDouble();
        if (value < 0.25) {
            return TouristType.CHILD;
        }
        if (value > 0.88) {
            return TouristType.VIP;
        }
        return TouristType.ADULT;
    }
}
