package adelina.luxtravel.domain.transport;

public class TransportFactory {

    public Transport getVehicle(String type, TransportClass vehicleClass) {
        if (type.equalsIgnoreCase("Airplane")) {
            return new Airplane(vehicleClass);
        } else {
            return new Bus(vehicleClass);
        }
    }
}
