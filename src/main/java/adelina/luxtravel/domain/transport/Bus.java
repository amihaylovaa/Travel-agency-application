package adelina.luxtravel.domain.transport;

import adelina.luxtravel.domain.City;

import javax.persistence.*;

@Entity
@Table(name = "bus")
public class Bus extends Vehicle {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private long id;

    public Bus(VehicleClass vehicleClass) {
        super(vehicleClass);
    }

    @Override
    public void calculateDuration(City to) {
        //  double duration = to.getDistance() / BUS_MAX_SPEED;
        ;
        //   return parseToLocalTime(duration);
    }
}