package adelina.luxtravel.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents transport class on which ticket's price depend
 */
@Getter
@NoArgsConstructor
public enum TransportClass {

    FIRST(0.1), BUSINESS(0.2), ECONOMY(0.4);

    @JsonProperty
    private double priceCoefficient;

    TransportClass(double priceCoefficient) {
        this.priceCoefficient = priceCoefficient;
    }
}
