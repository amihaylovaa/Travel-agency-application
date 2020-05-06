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

/**
 * Represents service for transport
 */
@Service
@Transactional
public class TransportService {
    private TransportRepository transportRepository;

    /**
     * Constructor
     *
     * @param transportRepository transport repository
     */
    @Autowired
    public TransportService(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    /**
     * Saves new bus
     *
     * @param transport the transport (bus) that is going to be saved
     * @return the saved bus
     */
    public Transport saveBus(Transport transport) {
        validateTransport(transport);

        return transportRepository.save(transport);
    }

    /**
     * Saves new airplanes
     *
     * @param transport the transport(airplane) that is going to be saved
     * @return the saved transport
     */
    public Transport saveAirplane(Transport transport) {
        validateTransport(transport);

        return transportRepository.save(transport);
    }

    /**
     * Saves list of transports
     *
     * @param transports the list of transports that are going to be saved
     * @return the saved list of transports
     */
    public List<Transport> saveAll(List<Transport> transports) {
        validateTransportList(transports);

        return transportRepository.saveAll(transports);
    }

    /**
     * Searches for a transport by id
     *
     * @param id transport's id
     * @return the found transport, otherwise throws exception for non-existent transport
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
     * Searches for all buses by class
     *
     * @param transportClass the transport class
     * @return list of buses of that class, otherwise throws exception
     * for non-existent list of buses of that class
     */
    public List<Transport> findAllBusesByClass(String transportClass) {
        validateTransportClass(transportClass);

        List<Transport> transports = transportRepository.findAllBusesByClass(transportClass);

        return validateTransportListExist(transports);
    }

    /**
     * Searches for all airplanes by class
     *
     * @param transportClass the transport class
     * @return list of airplanes of that class, otherwise throws exception
     * for non-existent list of airplanes of that class
     */
    public List<Transport> findAllAirplanesByClass(String transportClass) {
        validateTransportClass(transportClass);

        List<Transport> transports = transportRepository.findAllAirplanesByClass(transportClass);

        return validateTransportListExist(transports);
    }

    /**
     * Gets all airplanes
     *
     * @return list of all airplanes, throws exception if no airplanes are present
     */
    public List<Transport> findAllAirplanes() {
        List<Transport> transports = transportRepository.findAllAirplanes();

        return validateTransportListExist(transports);
    }

    /**
     * Gets all buses
     *
     * @return list of all buses, throws exception if no buses are present
     */
    public List<Transport> findAllBuses() {
        List<Transport> transports = transportRepository.findAllBuses();

        return validateTransportListExist(transports);
    }

    /**
     * Updates transport's class by id
     *
     * @param transportClass the new transport class
     * @param id             transport's id
     */
    public void updateClass(String transportClass, long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        validateTransportClass(transportClass);
        validateTransportExistById(id);
        transportRepository.updateClass(transportClass, id);
    }

    /**
     * Deletes transport by id
     *
     * @param id transport's id
     */
    public void deleteById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        validateTransportExistById(id);
        transportRepository.deleteById(id);
    }

    private void validateTransportClass(String transportClass) {
        if (transportClass == null || transportClass.isEmpty()) {
            throw new InvalidArgumentException("Invalid transport class");
        }

        if (!(transportClass.equals(TransportClass.FIRST.name()))
                && !(transportClass.equals(TransportClass.BUSINESS.name()))
                && !(transportClass.equals(TransportClass.ECONOMY.name()))) {
            throw new InvalidArgumentException("Invalid transport class name");
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

    private void validateTransport(Transport transport) {
        if (transport == null) {
            throw new InvalidArgumentException("Invalid transport");
        }

        TransportClass transportClass = transport.getTransportClass();

        if (transportClass == null) {
            throw new InvalidArgumentException("Invalid transport class");
        }
        validateTransportClass(transportClass.name());
    }

    private void validateTransportList(List<Transport> transports) {
        if (transports == null || transports.isEmpty()) {
            throw new InvalidArgumentException("Invalid transport list");
        }

        for (Transport transport : transports) {
            validateTransport(transport);
        }
    }
}