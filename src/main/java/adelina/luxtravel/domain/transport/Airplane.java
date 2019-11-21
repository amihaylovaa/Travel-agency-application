package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.City;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "airplane")
@Getter
public class Airplane extends Transport {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private long id;

    public Airplane(TransportClass vehicleClass) {
        super(vehicleClass);
    }

    @Override
    public void calculateDuration(City to) {
        //double duration = to.getDistance() / AIRPLANE_MAX_SPEED;
        ;
        //return parseToLocalTime(duration);
    }
}