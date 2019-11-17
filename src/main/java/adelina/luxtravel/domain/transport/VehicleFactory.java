package adelina.luxtravel.domain.transport;

public class VehicleFactory {

    public Vehicle getVehicle(String type, String brand) {
        if (type.equalsIgnoreCase("Airplane")) {
            return new Airplane(brand);
        } else {
            return new Bus(brand);
        }
    }
}
