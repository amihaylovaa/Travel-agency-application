package adelina.luxtravel.service;

import adelina.luxtravel.domain.transport.*;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.TransportRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Constants.INVALID_ID;
import static adelina.luxtravel.utility.Constants.NON_EXISTING_TRANSPORT_WITH_GIVEN_ID;

@Service
@Transactional
public class TransportService {
    private TransportRepository transportRepository;

    @Autowired
    public TransportService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    public Transport saveBus(Transport transport) {
        validateTransport(transport);

        TransportClass transportClass = transport.getTransportClass();

        validateTransportClass(transportClass);

        return transportRepository.saveBus(transportClass);
    }

    public Transport saveAirplane(Transport transport) {
        validateTransport(transport);

        TransportClass transportClass = transport.getTransportClass();

        validateTransportClass(transportClass);

        return transportRepository.saveAirplane(transportClass);
    }

    public List<Transport> saveAll(List<Transport> transports) {
        validateTransportList(transports);

        return transportRepository.saveAll(transports);
    }

    public Transport findById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        Optional<Transport> transport = transportRepository.findById(id);

        if (!transport.isPresent()) {
            throw new NonExistentItemException(NON_EXISTING_TRANSPORT_WITH_GIVEN_ID);
        }

        return transport.get();
    }

    public List<Transport> findAllBusesByClass(TransportClass transportClass) {
        validateTransportClass(transportClass);

        List<Transport> transports = transportRepository.findAllBusesByClass(transportClass);

        return validateTransportListExist(transports);
    }

    public List<Transport> findAllAirplanesByClass(TransportClass transportClass) {
        validateTransportClass(transportClass);

        List<Transport> transports = transportRepository.findAllAirplanesByClass(transportClass);

        return validateTransportListExist(transports);
    }

    public List<Transport> findAllAirplanes() {
        List<Transport> transports = transportRepository.findAllAirplanes();

        return validateTransportListExist(transports);
    }

    public List<Transport> findAllBuses() {
        List<Transport> transports = transportRepository.findAllBuses();

        return validateTransportListExist(transports);
    }

    public void updateClass(TransportClass transportClass, long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        validateTransportClass(transportClass);
        validateTransportExistById(id);
        transportRepository.updateClass(transportClass, id);
    }

    public void deleteById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        validateTransportExistById(id);
        transportRepository.deleteById(id);
    }

    private void validateTransportList(List<Transport> transports) {
        if (ObjectUtils.isEmpty(transports)) {
            throw new InvalidArgumentException("Invalid list of transport");
        }

        for (Transport transport : transports) {
            validateTransport(transport);
            validateTransportClass(transport.getTransportClass());
        }
    }

    private void validateTransport(Transport transport) {
        if (transport == null) {
            throw new InvalidArgumentException("Invalid transport");
        }
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

    private void validateTransportExistById(long id) {
        Optional<Transport> transport = transportRepository.findById(id);

        if (!transport.isPresent()) {
            throw new NonExistentItemException(NON_EXISTING_TRANSPORT_WITH_GIVEN_ID);
        }
    }
}