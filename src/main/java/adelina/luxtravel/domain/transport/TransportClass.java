package adelina.luxtravel.domain.transport;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TransportClass {

    FIRST(0.1), BUSINESS(0.2), ECONOMY(0.4);

    private double priceCoefficient;

    TransportClass(double priceCoefficient) {
        this.priceCoefficient = priceCoefficient;
    }
}
