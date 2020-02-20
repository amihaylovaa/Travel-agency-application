package adelina.luxtravel.domain.transport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

@Getter
@JsonSerialize
@JsonDeserialize
public enum TransportClass {
    FIRST(0.1), BUSINESS(0.2), ECONOMY(0.4);

    private double priceCoefficient;

    TransportClass(double priceCoefficient) {
        this.priceCoefficient = priceCoefficient;
    }
}
