package adelina.luxtravel.service;

import adelina.luxtravel.domain.transport.*;
import adelina.luxtravel.enumeration.TransportClass;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.TransportRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Constants.INVALID_ID;
import static adelina.luxtravel.utility.Constants.NON_EXISTING_TRANSPORT_WITH_GIVEN_ID;

/**
 * Represents service for a transport
 */
@Service
public class TransportService {

    private final TransportRepository transportRepository;

    @Autowired
    public TransportService(TransportRepository transportService) {
        this.transportRepository = transportService;
    }

    /**
     * Saves new transport
     *
     * @param transport the transport that is going to be saved
     * @return the saved transport
     */
    public Transport save(Transport transport) {
        validateTransport(transport);

        return transportRepository.save(transport);
    }

    /**
     * Gets transport by id
     *
     * @param id transport's id
     * @return if present - the searched transport, otherwise throws exception for non-existent transport
     */
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

    /**
     * Gets specific transports by class
     *
     * @param transportClass transport's class
     * @param type           transport's type
     * @return list of transports, otherwise throws exception for non-existent transports of that class
     */
    public List<Transport> findSpecificTransportsByTransportClass(TransportClass transportClass, String type) {
        validateTransportClass(transportClass);

        if (StringUtils.isEmpty(type)) {
            throw new InvalidArgumentException("Invalid transport type");
        }

        String transportClassName = transportClass.name();
        List<Transport> transports = transportRepository.findSpecificTransportsByTransportClass(transportClassName, type);

        return validateTransportListExist(transports);
    }

    /**
     * Gets all airplanes
     *
     * @return list of all airplanes, throws exception if airplanes are not found
     */
    public List<Transport> findAllAirplanes() {
        List<Transport> transports = transportRepository.findAllAirplanes();

        return validateTransportListExist(transports);
    }

    /**
     * Gets all buses
     *
     * @return list of all buses, throws exception if buses are not found
     */
    public List<Transport> findAllBuses() {
        List<Transport> transports = transportRepository.findAllBuses();

        return validateTransportListExist(transports);
    }

    /**
     * Gets all transports
     *
     * @return list of transports, throws exception if transports are not found
     */
    public List<Transport> findAll() {
        List<Transport> transports = transportRepository.findAll();

        return validateTransportListExist(transports);
    }

    /**
     * Updates transport's class by id
     *
     * @param id        transport's id
     * @param transport the transport containing the new class
     * @return the updated transport
     */
    public Transport updateTransportClass(long id, Transport transport) {
        validateTransport(transport);

        TransportClass transportClass = transport.getTransportClass();

        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        validateTransportExistById(id);
        transportRepository.updateTransportClass(transportClass.name(), id);

        return findById(id);
    }

    /**
     * Deletes transport by id
     *
     * @param id transport's id
     * @return true for successfully deleted transport, false for unsuccessful attempt
     */
    public boolean deleteById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        validateTransportExistById(id);
        transportRepository.deleteById(id);

        try {
            findById(id);
        } catch (NonExistentItemException e) {
            return true;
        }
        return false;
    }

    public void validateTransportExistById(long id) {
        Optional<Transport> transport = transportRepository.findById(id);

        if (!transport.isPresent()) {
            throw new NonExistentItemException(NON_EXISTING_TRANSPORT_WITH_GIVEN_ID);
        }
    }

    private void validateTransportClass(TransportClass transportClass) {
        if (ObjectUtils.isEmpty(transportClass)) {
            throw new InvalidArgumentException("Invalid transport class");
        }
    }

    private List<Transport> validateTransportListExist(List<Transport> transports) {
        if (ObjectUtils.isEmpty(transports)) {
            throw new NonExistentItemException("Transports are not found");
        }
        return transports;
    }

    private void validateTransport(Transport transport) {
        if (ObjectUtils.isEmpty(transport)) {
            throw new InvalidArgumentException("Invalid transport");
        }

        TransportClass transportClass = transport.getTransportClass();

        validateTransportClass(transportClass);
    }
}