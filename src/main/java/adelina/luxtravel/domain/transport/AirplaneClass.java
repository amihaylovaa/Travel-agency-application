package adelina.luxtravel.domain.transport;

import lombok.Getter;

@Getter
public enum AirplaneClass {
    FIRST(0.1), BUSINESS (0.2), ECONOMY(0.4);

    private double priceCoefficient;

    AirplaneClass(double priceCoefficient) {
        this.priceCoefficient = priceCoefficient;
    }
}
