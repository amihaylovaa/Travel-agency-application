package adelina.luxtravel.domain.transport;

public class VehicleFactory {

    public Vehicle getVehicle(String type, VehicleClass vehicleClass) {
        if (type.equalsIgnoreCase("Airplane")) {
            return new Airplane(vehicleClass);
        } else {
            return new Bus(vehicleClass);
        }
    }
}
