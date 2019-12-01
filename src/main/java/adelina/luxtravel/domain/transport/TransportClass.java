package adelina.luxtravel.domain.transport;

import lombok.Getter;

@Getter
public enum TransportClass {
    FIRST(0.1), BUSINESS(0.2), ECONOMY(0.4);

    private double priceCoefficient;

    TransportClass(double priceCoefficient) {
        this.priceCoefficient = priceCoefficient;
    }
}
