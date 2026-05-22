package com.parque.dinosaurios.application.service;

import com.parque.dinosaurios.config.ParkProperties;
import com.parque.dinosaurios.domain.enums.RevenueType;
import com.parque.dinosaurios.domain.enums.TouristType;
import com.parque.dinosaurios.domain.model.Revenue;
import com.parque.dinosaurios.domain.model.Tourist;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final ParkProperties properties;
    private final RevenueService revenueService;

    public TicketService(ParkProperties properties, RevenueService revenueService) {
        this.properties = properties;
        this.revenueService = revenueService;
    }

    public Revenue sellTicket(Tourist tourist, int simulationStep) {
        BigDecimal price = calculatePrice(tourist.getType());
        tourist.payTicket();
        tourist.changeSatisfaction(2);
        return revenueService.record(
                RevenueType.TICKET,
                price,
                "Venta de boleto para turista " + tourist.getId(),
                simulationStep,
                1);
    }

    public BigDecimal calculatePrice(TouristType type) {
        BigDecimal basePrice = properties.getTickets().getBasePrice();
        BigDecimal price = switch (type) {
            case CHILD -> basePrice.multiply(BigDecimal.ONE.subtract(properties.getTickets().getChildDiscount()));
            case VIP -> basePrice.multiply(new BigDecimal("1.40"));
            case ADULT -> basePrice;
        };
        return price.setScale(2, RoundingMode.HALF_UP);
    }
}
