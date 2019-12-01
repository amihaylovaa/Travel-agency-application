package adelina.luxtravel.service;

import adelina.luxtravel.domain.transport.Bus;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.repository.TransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportService {
    private TransportRepository transportRepository;

    @Autowired
    public TransportService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    public List<Transport> getAllBusesByClass(TransportClass transportClass) {
        validateTransportClass(transportClass);
        return transportRepository.getAllBusesByClass(transportClass);
    }

    public List<Transport> getAllAirplanesByClass(TransportClass transportClass) {
        validateTransportClass(transportClass);
        return transportRepository.getAllAirplanesByClass(transportClass);
    }

    public List<Transport> getAllAirplanes() {
        return transportRepository.getAllAirplanes();
    }

    public List<Transport> getAllBuses() {
        return transportRepository.getAllBuses();
    }

    private void validateTransportClass(TransportClass transportClass) {
        if (transportClass == null) {
            throw new InvalidArgumentException("Invalid transport class");
        }
    }
}
