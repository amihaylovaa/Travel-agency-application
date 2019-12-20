package adelina.luxtravel.service;

import adelina.luxtravel.domain.transport.*;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.TransportRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Constants.INVALID_ID;

@Service
public class TransportService {
    private TransportRepository transportRepository;

    @Autowired
    public TransportService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    public Transport saveBus(Transport transport) throws InvalidArgumentException {
        validateTransport(transport);

        TransportClass transportClass = transport.getTransportClass();

        validateTransportClass(transportClass);

        return transportRepository.saveBus(transportClass);
    }

    public Transport saveAirplane(Transport transport) throws InvalidArgumentException {
        validateTransport(transport);

        TransportClass transportClass = transport.getTransportClass();

        validateTransportClass(transportClass);

        return transportRepository.saveAirplane(transportClass);
    }

    public List<Transport> saveAll(List<Transport> transports) throws InvalidArgumentException {
        validateTransportList(transports);
        return transportRepository.saveAll(transports);
    }

    public Transport findById(long id) throws InvalidArgumentException, NonExistentItemException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }

        Optional<Transport> transport = transportRepository.findById(id);

        if (!transport.isPresent()) {
            throw new NonExistentItemException("Transport with that id does not exist");
        }
        return transport.get();
    }

    public List<Transport> findAllBusesByClass(TransportClass transportClass) throws InvalidArgumentException, NonExistentItemException {
        validateTransportClass(transportClass);

        List<Transport> transports = transportRepository.findAllBusesByClass(transportClass);

        return validateTransportListExist(transports);
    }

    public List<Transport> findAllAirplanesByClass(TransportClass transportClass) throws InvalidArgumentException, NonExistentItemException {
        validateTransportClass(transportClass);

        List<Transport> transports = transportRepository.findAllAirplanesByClass(transportClass);

        return validateTransportListExist(transports);
    }

    public List<Transport> findAllAirplanes() throws NonExistentItemException {
        List<Transport> transports = transportRepository.findAllAirplanes();

        return validateTransportListExist(transports);
    }

    public List<Transport> findAllBuses() throws NonExistentItemException {
        List<Transport> transports = transportRepository.findAllBuses();

        return validateTransportListExist(transports);
    }

    public void updateClass(TransportClass transportClass, long id) throws InvalidArgumentException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        validateTransportClass(transportClass);

        try {
            Transport transport = findById(id);
        } catch (NonExistentItemException nonExistentItemException) {
            // TODO - logger
        }
        transportRepository.updateClass(transportClass, id);
    }

    public void deleteById(long id) throws InvalidArgumentException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }
        transportRepository.deleteById(id);
    }

    private void validateTransportList(List<Transport> transports) throws InvalidArgumentException {
        if (ObjectUtils.isEmpty(transports)) {
            throw new InvalidArgumentException("Invalid list of transport");
        }
        for (Transport transport : transports) {
            validateTransport(transport);
            validateTransportClass(transport.getTransportClass());
        }
    }

    private void validateTransport(Transport transport) throws InvalidArgumentException {
        if (transport == null) {
            throw new InvalidArgumentException("Invalid transport");
        }
    }

    private void validateTransportClass(TransportClass transportClass) throws InvalidArgumentException {
        if (transportClass == null) {
            throw new InvalidArgumentException("Invalid transport class");
        }
    }

    private List<Transport> validateTransportListExist(List<Transport> transports) throws NonExistentItemException {
        if (ObjectUtils.isEmpty(transports)) {
            throw new NonExistentItemException("List of transports is not found");
        }
        return transports;
    }
}