package adelina.luxtravel.domain.transport;

public class VehicleFactory {

    public Vehicle getVehicle(String type, String brand, AirplaneClass... airplaneClass) {
        if (type.equalsIgnoreCase("Airplane")) {
            return new Airplane(brand, airplaneClass[0]);
        } else {
            return new Bus(brand);
        }
    }
}
