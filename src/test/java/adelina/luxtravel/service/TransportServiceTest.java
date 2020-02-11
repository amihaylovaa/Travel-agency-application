package adelina.luxtravel.service;

import adelina.luxtravel.domain.transport.Airplane;
import adelina.luxtravel.domain.transport.Bus;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.repository.TransportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

// TODO - OPTIMIZATIONS
// TODO - check for bad names after renaming
@ExtendWith(MockitoExtension.class)
public class TransportServiceTest {

    @Mock
    private TransportRepository transportRepository;
    @InjectMocks
    private TransportService transportService;

    @Test
    public void saveBus_BusIsNull_ExceptionThrown() {
        Transport transport = null;

        assertThrows(InvalidArgumentException.class, () -> transportService.saveBus(transport));
    }

    @Test
    public void saveBus_TransportClassIsNull_ExceptionThrown() {
        TransportClass transportClass = null;
        Transport transport = new Bus(transportClass);

        assertThrows(InvalidArgumentException.class, () -> transportService.saveBus(transport));
    }

    @Test
    public void saveBus_ValidData_SuccessfullyCreatedBus() {
        TransportClass transportClass = TransportClass.ECONOMY;
        Transport transport = new Bus(transportClass);

        when(transportRepository.saveBus(transportClass)).thenReturn((Bus) transport);

        assertEquals(transport, transportService.saveBus(transport));
    }

    @Test
    public void saveAirplane_AirplaneIsNull_ExceptionThrown() {
        Transport transport = null;

        assertThrows(InvalidArgumentException.class, () -> transportService.saveAirplane(transport));
    }

    @Test
    public void saveAirplane_TransportClassIsNull_ExceptionThrown() {
        TransportClass transportClass = null;
        Transport transport = new Bus(transportClass);

        assertThrows(InvalidArgumentException.class, () -> transportService.saveAirplane(transport));
    }

    @Test
    public void saveAirplane_ValidData_SuccessfullyCreatedAirplane() {
        TransportClass transportClass = TransportClass.BUSINESS;
        Transport transport = new Airplane(transportClass);

        when(transportRepository.saveAirplane(transportClass)).thenReturn((Airplane) transport);

        assertEquals(transport, transportService.saveAirplane(transport));
    }

    @Test
    public void saveAll_ListIsEmpty_ExceptionThrown() {
        List<Transport> transports = new ArrayList<>();

        assertThrows(InvalidArgumentException.class, () -> transportService.saveAll(transports));
    }

    @Test
    public void saveAll_ListHasNullElement_ExceptionThrown() {
        List<Transport> transports = new ArrayList<>(createTransportList());
        Transport transport = null;
        transports.add(transport);

        assertThrows(InvalidArgumentException.class, () -> transportService.saveAll(transports));
    }

    @Test
    public void saveAll_ValidData_SuccessfullyCreatedTransportList() {
        List<Transport> transports = new ArrayList<>(createTransportList());
        int size = transports.size();

        when(transportRepository.saveAll(transports)).thenReturn(transports);
        List<Transport> createdTransportList = transportService.saveAll(transports);

        assertEquals(size, createdTransportList.size());
        assertEquals(transports, createdTransportList);
    }

    @Test
    public void findById_IdIsEqualToZero_ExceptionThrown() {
        long invalidId = 0;

        assertThrows(InvalidArgumentException.class, () -> transportService.findById(invalidId));
    }

    @Test
    public void findById_TransportWithGivenIdDoesNotExist_ExceptionThrown() {
        TransportClass transportClass = TransportClass.BUSINESS;
        Transport transport = new Airplane(1, transportClass);
        Optional<Transport> transportOptional = Optional.of(transport);
        long transportId = transport.getId();
        long invalidId = 12;

        lenient().when(transportRepository.findById(transportId)).thenReturn(transportOptional);

        assertThrows(NonExistentItemException.class, () -> transportService.findById(invalidId));
    }

    @Test
    public void findById_TransportWithGivenIdExists_ReturnedSearchedTransport() {
        TransportClass transportClass = TransportClass.BUSINESS;
        Transport transport = new Airplane(1, transportClass);
        Optional<Transport> transportOptional = Optional.of(transport);
        long transportId = transport.getId();

        when(transportRepository.findById(transportId)).thenReturn(transportOptional);
        Transport searchedTransport = transportService.findById(transportId);

        assertEquals(transport, searchedTransport);
        assertEquals(transportId, searchedTransport.getId());
    }

    @Test
    public void findAllBusesByClass_TransportClassIsNull_ExceptionThrown() {
        TransportClass transportClass = null;

        assertThrows(InvalidArgumentException.class,
                () -> transportService.findAllBusesByClass(transportClass));
    }

    @Test
    public void findAllBusesByClass_BusesWithGivenTransportClassDoesNotExists_ExceptionThrown() {
        List<Transport> transports = new ArrayList<>(createTransportList());
        TransportClass transportClass = TransportClass.FIRST;

        when(transportRepository.findAllBusesByClass(transportClass)).thenThrow(NonExistentItemException.class);

        assertThrows(NonExistentItemException.class,
                () -> transportService.findAllBusesByClass(transportClass));
    }

    @Test
    public void findAllBusesByClass_BusesWithGivenTransportClassExists_ReturnedBusesList() {
        TransportClass transportClass = TransportClass.ECONOMY;
        Transport transportEconomy = new Bus(transportClass);
        Transport transportEconomyClass = new Bus(transportClass);
        List<Transport> transports = new ArrayList<>();
        transports.add(transportEconomy);
        transports.add(transportEconomyClass);

        when(transportRepository.findAllBusesByClass(transportClass)).thenReturn(transports);
        List<Transport> foundBuses = transportService.findAllBusesByClass(transportClass);

        assertEquals(transports.size(), foundBuses.size());
        assertTrue(foundBuses.containsAll(transports));
    }

    @Test
    public void findAllAirplanesByClass_TransportClassIsNull_ExceptionThrown() {
        TransportClass transportClass = null;

        assertThrows(InvalidArgumentException.class,
                () -> transportService.findAllAirplanesByClass(transportClass));
    }

    @Test
    public void findAllAirplanesByClass_AirplanesWithGivenTransportClassDoesNotExists_ExceptionThrown() {
        List<Transport> transports = new ArrayList<>(createTransportList());
        TransportClass transportClass = TransportClass.ECONOMY;

        when(transportRepository.findAllAirplanesByClass(transportClass)).thenThrow(NonExistentItemException.class);

        assertThrows(NonExistentItemException.class,
                () -> transportService.findAllAirplanesByClass(transportClass));
    }

    @Test
    public void findAllAirplanesByClass_AirplanesWithGivenTransportClassExists_ReturnedAirplaneList() {
        TransportClass transportClass = TransportClass.BUSINESS;
        Transport transportBusiness = new Airplane(transportClass);
        Transport transportBusinessClass = new Airplane(transportClass);
        List<Transport> transports = new ArrayList<>();
        transports.add(transportBusiness);
        transports.add(transportBusinessClass);

        when(transportRepository.findAllAirplanesByClass(transportClass)).thenReturn(transports);
        List<Transport> foundAirplanes = transportService.findAllAirplanesByClass(transportClass);

        assertEquals(transports.size(), foundAirplanes.size());
        assertTrue(foundAirplanes.containsAll(transports));
    }

    @Test
    public void findAllAirplanes_ThereAreNoAirplanes_ExceptionThrown() {
        List<Transport> transports = new ArrayList<>();

        when(transportRepository.findAllAirplanes()).thenReturn(transports);

        assertThrows(NonExistentItemException.class, () -> transportService.findAllAirplanes());
    }

    @Test
    public void findAllBuses_ThereAreNoBuses_ExceptionThrown() {
        List<Transport> transports = new ArrayList<>();

        when(transportRepository.findAllBuses()).thenReturn(transports);

        assertThrows(NonExistentItemException.class, () -> transportService.findAllBuses());
    }

    @Test
    public void updateClass_TransportClassIsNull_ExceptionThrown() {
        TransportClass transportClass = null;
        long id = 1;

        assertThrows(InvalidArgumentException.class, () -> transportService.updateClass(transportClass, id));
    }

    @Test
    public void updateClass_TransportIdIsNegative_ExceptionThrown() {
        TransportClass transportClass = null;
        long id = -1;

        assertThrows(InvalidArgumentException.class, () -> transportService.updateClass(transportClass, id));
    }

    @Test
    public void updateClass_TransportWithGivenIdDoesNotExist_ExceptionThrown() {
        long id = 1;
        long invalidId = 32;
        TransportClass transportClass = TransportClass.ECONOMY;
        Transport transport = new Airplane(id, transportClass);
        Optional<Transport> transportOptional = Optional.of(transport);

        lenient().when(transportRepository.findById(id)).thenReturn(transportOptional);

        assertThrows(NonExistentItemException.class,
                () -> transportService.updateClass(transportClass, invalidId));
    }

    @Test
    public void deleteById_IdIsZero_ExceptionThrown() {
        long id = 0;

        assertThrows(InvalidArgumentException.class, () -> transportService.deleteById(id));
    }

    @Test
    public void deleteById_TransportWithGivenIdDoesNotExist_ExceptionThrown() {
        long id = 1;
        long invalidId = 4;
        TransportClass transportClass = TransportClass.FIRST;
        Transport transport = new Bus(id, transportClass);
        Optional<Transport> transportOptional = Optional.of(transport);

        lenient().when(transportRepository.findById(id)).thenReturn(transportOptional);

        assertThrows(NonExistentItemException.class, () -> transportService.deleteById(invalidId));
    }

    private List<Transport> createTransportList() {
        TransportClass transportClassFirst = TransportClass.FIRST;
        TransportClass transportClassBusiness = TransportClass.BUSINESS;
        TransportClass transportClassEconomy = TransportClass.ECONOMY;
        Transport transportAirplaneFirstClass = new Airplane(1, transportClassFirst);
        Transport transportAirplaneBusiness = new Airplane(2, transportClassBusiness);
        Transport transportBusBusiness = new Bus(3, transportClassBusiness);
        Transport transportBusEconomy = new Bus(4, transportClassEconomy);
        Transport transportSecondBusEconomy = new Bus(5, transportClassEconomy);
        List<Transport> transports = new ArrayList<>();

        transports.add(transportAirplaneBusiness);
        transports.add(transportAirplaneFirstClass);
        transports.add(transportBusBusiness);
        transports.add(transportBusEconomy);
        transports.add(transportSecondBusEconomy);

        return transports;
    }
}