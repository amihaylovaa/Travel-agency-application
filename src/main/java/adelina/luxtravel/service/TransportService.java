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
        validateTransportList(transports);
        return transportRepository.saveAll(transports);
    }

    public Transport findById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }

        Transport transport = transportRepository.findById(id);

        if (transport == null) {
            throw new NonExistentItemException("Transport with that id does not exist");
        }
        return transport;
    }

    // TODO : maybe extract the result from repository method
    public List<Transport> findAllBusesByClass(TransportClass transportClass) {
        validateTransportClass(transportClass);
        return validateTransportListExist(transportRepository.findAllBusesByClass(transportClass));
    }

    public List<Transport> findAllAirplanesByClass(TransportClass transportClass) {
        validateTransportClass(transportClass);
        return validateTransportListExist(transportRepository.findAllAirplanesByClass(transportClass));
    }

    public List<Transport> findAllAirplanes() {
        return validateTransportListExist(transportRepository.findAllAirplanes());
    }

    public List<Transport> findAllBuses() {
        return validateTransportListExist(transportRepository.findAllBuses());
    }

    // TODO: think about another solution because the result is not used (same for bookings' services)
    public void updateClass(TransportClass transportClass, long id) {
        findById(id);
        transportRepository.updateClass(transportClass, id);
    }

    // TODO : THINK (maybe transport class and then get id and then delete)
    public void deleteById(long id) {
        transportRepository.deleteById(id);
    }

    private void validateTransportList(List<Transport> transports) {
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

    private List<Transport> validateTransportListExist(List<Transport> transports) {
        if (ObjectUtils.isEmpty(transports)) {
            throw new NonExistentItemException("List of transports is not found");
        }
        return transports;
    }
}