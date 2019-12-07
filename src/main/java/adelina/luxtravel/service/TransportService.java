package adelina.luxtravel.service;

import adelina.luxtravel.domain.transport.*;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.TransportRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
        if (id <= NumberUtils.LONG_ZERO) {
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
        return getListOfExistingTransports(transportRepository.findAllBusesByClass(transportClass));
    }

    public List<Transport> findAllAirplanesByClass(TransportClass transportClass) {
        validateTransportClass(transportClass);
        return getListOfExistingTransports(transportRepository.findAllAirplanesByClass(transportClass));
    }

    public List<Transport> findAllAirplanes() {
        return getListOfExistingTransports(transportRepository.findAllAirplanes());
    }

    public List<Transport> findAllBuses() {
        return getListOfExistingTransports(transportRepository.findAllBuses());
    }

    public void delete(Transport transport) {
        validateTransport(transport);
        transportRepository.delete(transport);
    }

    public void deleteAll() {
        transportRepository.deleteAll();
    }

    private void validateListOfTransport(List<Transport> transports) {
        if (ObjectUtils.isEmpty(transports)) {
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

    private List<Transport> getListOfExistingTransports(List<Transport> transports) {
        if (ObjectUtils.isEmpty(transports)) {
            throw new NonExistentItemException("List of transports is not found");
        }
        return transports;
    }
}