package adelina.luxtravel.service;

import adelina.luxtravel.domain.transport.*;
import adelina.luxtravel.exception.*;
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

    public Transport save(Transport transport) {
        validateTransport(transport);
        return transportRepository.save(transport);
    }

    public List<Transport> saveAll(List<Transport> transports) {
        validateListOfTransport(transports);
        return transportRepository.saveAll(transports);
    }

    public Transport findById(long id) {
        if (id <= 0) {
            throw new InvalidArgumentException("Invalid id");
        }

        Transport foundTransport = transportRepository.findById(id);

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

    public void delete(Transport transport) {
        validateTransport(transport);
        transportRepository.delete(transport);
    }

    public void deleteAll() {
        transportRepository.deleteAll();
    }

    private void validateListOfTransport(List<Transport> transports) {
        if (transports == null || transports.isEmpty()) {
            throw new InvalidArgumentException("Invalid list of transport");
        }
        for (Transport transport : transports) {
            validateTransport(transport);
        }
    }

    private void validateTransport(Transport transport) {
        if (transport == null) {
            throw new InvalidArgumentException("Invalid transport");
        }
        validateTransportClass(transport.getTransportClass());
    }

    private void validateTransportClass(TransportClass transportClass) {
        if (transportClass == null) {
            throw new InvalidArgumentException("Invalid transport class");
        }
    }

    private List<Transport> getListOfTransports(List<Transport> transports) {
        validateTransportsExist(transports);
        return transports;
    }

    private void validateTransportsExist(List<Transport> transports) {
        if (transports == null || transports.isEmpty()) {
            throw new NonExistentItemException("Transport is not found");
        }
    }
}