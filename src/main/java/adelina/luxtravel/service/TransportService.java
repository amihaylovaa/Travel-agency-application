package adelina.luxtravel.service;

import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
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

    public Transport findById(Transport transport) {
        if (transport == null) {
            throw new InvalidArgumentException("Invalid transport");
        }
        validateTransportClass(transport.getTransportClass());

        Transport foundTransport = transportRepository.findById(transport.getId());

        if (foundTransport == null) {
            throw new NonExistentItemException("Transport with that id does not exist");
        }
        return foundTransport;
    }

    public List<Transport> findAllBusesByClass(TransportClass transportClass) {
        validateTransportClass(transportClass);

        return getListOfTransports(transportRepository.findAllBusesByClass(transportClass));
    }

    public List<Transport> findAllAirplanesByClass(TransportClass transportClass) {
        validateTransportClass(transportClass);

        return getListOfTransports(transportRepository.findAllAirplanesByClass(transportClass));
    }

    public List<Transport> findAllAirplanes() {
        return getListOfTransports(transportRepository.findAllAirplanes());
    }

    public List<Transport> findAllBuses() {
        return getListOfTransports(transportRepository.findAllBuses());
    }

    private void validateTransportClass(TransportClass transportClass) {
        if (transportClass == null) {
            throw new InvalidArgumentException("Invalid transport class");
        }
    }

    private void validateTransportsExist(List<Transport> transports) {
        if (transports == null || transports.isEmpty()) {
            throw new NonExistentItemException("This kind of transport is not found");
        }
    }

    private List<Transport> getListOfTransports(List<Transport> transports){
        validateTransportsExist(transports);

        return transports;
    }
}