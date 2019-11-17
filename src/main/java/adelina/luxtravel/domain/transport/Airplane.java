package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.City;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;

import static adelina.luxtravel.utility.Constants.AIRPLANE_MAX_SPEED;

@Entity
@Table(name = "airplane")
@Getter
public class Airplane extends Vehicle {
    public Airplane(String brandName) {
        super(brandName);
    }

    @Override
    public void calculateDuration(City to) {
        //double duration = to.getDistance() / AIRPLANE_MAX_SPEED;
        ;
        //return parseToLocalTime(duration);
    }
}