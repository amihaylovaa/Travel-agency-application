package adelina.luxtravel.service;

import adelina.luxtravel.domain.transport.*;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.TransportRepository;
import org.apache.commons.lang3.math.NumberUtils;
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

    // TODO FIXING (also service)
    @Test
    public void saveBus_ValidData_SuccessfullyCreatedBus() {
        TransportClass transportClass = TransportClass.ECONOMY;
        Transport expectedTransport = new Bus(transportClass);

        when(transportRepository.saveBus(transportClass)).thenReturn((Bus) expectedTransport);
        Transport actualTransport = transportService.saveBus(expectedTransport);

        assertEquals(expectedTransport, actualTransport);
    }

    @Test
    public void saveAirplane_AirplaneIsNull_ExceptionThrown() {
        Transport transport = null;

        assertThrows(InvalidArgumentException.class, () -> transportService.saveAirplane(transport));
    }

    @Test
    public void saveAirplane_TransportClassIsNull_ExceptionThrown() {
        TransportClass transportClass = null;
        Transport transport = new Airplane(transportClass);

        assertThrows(InvalidArgumentException.class, () -> transportService.saveAirplane(transport));
    }

    // TODO FIXING (also service)
    @Test
    public void saveAirplane_ValidData_SuccessfullyCreatedAirplane() {
        TransportClass transportClass = TransportClass.BUSINESS;
        Transport expectedTransport = new Airplane(transportClass);

        when(transportRepository.saveAirplane(transportClass)).thenReturn((Airplane) expectedTransport);
        Transport actualTransport = transportService.saveAirplane(expectedTransport);

        assertEquals(expectedTransport, actualTransport);
    }

    @Test
    public void saveAll_ListIsEmpty_ExceptionThrown() {
        List<Transport> transports = new ArrayList<>();

        assertThrows(InvalidArgumentException.class, () -> transportService.saveAll(transports));
    }

    @Test
    public void saveAll_TransportListHasNullElement_ExceptionThrown() {
        List<Transport> transports = new ArrayList<>(createTransportList());
        transports.add(null);

        assertThrows(InvalidArgumentException.class, () -> transportService.saveAll(transports));
    }

    @Test
    public void saveAll_ValidData_SuccessfullyCreatedTransportList() {
        List<Transport> expectedTransports = new ArrayList<>(createTransportList());
        int expectedSize = expectedTransports.size();

        when(transportRepository.saveAll(expectedTransports)).thenReturn(expectedTransports);
        List<Transport> actualTransports = transportService.saveAll(expectedTransports);

        assertEquals(expectedSize, actualTransports.size());
        assertTrue(expectedTransports.containsAll(actualTransports));
    }

    @Test
    public void findById_IdIsEqualToZero_ExceptionThrown() {
        long invalidId = NumberUtils.LONG_ZERO;

        assertThrows(InvalidArgumentException.class, () -> transportService.findById(invalidId));
    }

    @Test
    public void findById_TransportWithGivenIdDoesNotExist_ExceptionThrown() {
        TransportClass transportClass = TransportClass.BUSINESS;
        Transport transport = new Airplane(NumberUtils.LONG_ONE, transportClass);
        long existingId = transport.getId();
        long nonExistentId = 2;

        lenient().when(transportRepository.findById(existingId)).thenReturn(Optional.of(transport));

        assertThrows(NonExistentItemException.class, () -> transportService.findById(nonExistentId));
    }

    @Test
    public void findById_TransportWithGivenIdExists_ReturnedSearchedTransport() {
        TransportClass transportClass = TransportClass.BUSINESS;
        Transport expectedTransport = new Airplane(NumberUtils.LONG_ONE, transportClass);
        long expectedId = expectedTransport.getId();

        when(transportRepository.findById(expectedId)).thenReturn(Optional.of(expectedTransport));
        Transport actualTransport = transportService.findById(expectedId);

        assertEquals(expectedTransport, actualTransport);
    }

    @Test
    public void findAllBusesByClass_TransportClassIsNull_ExceptionThrown() {
        TransportClass transportClass = null;

        assertThrows(InvalidArgumentException.class,
                () -> transportService.findAllBusesByClass(transportClass));
    }

    @Test
    public void findAllBusesByClass_BusesWithGivenTransportClassDoesNotExists_ExceptionThrown() {
        TransportClass transportClass = TransportClass.FIRST;

        when(transportRepository.findAllBusesByClass(transportClass)).thenThrow(NonExistentItemException.class);

        assertThrows(NonExistentItemException.class,
                () -> transportService.findAllBusesByClass(transportClass));
    }

    @Test
    public void findAllBusesByClass_BusesWithGivenTransportClassExists_ReturnedBusesList() {
        TransportClass transportClass = TransportClass.ECONOMY;
        Transport transportEconomyA = new Bus(transportClass);
        Transport transportEconomyB = new Bus(transportClass);
        List<Transport> expectedTransports = new ArrayList<>();
        expectedTransports.add(transportEconomyA);
        expectedTransports.add(transportEconomyB);

        when(transportRepository.findAllBusesByClass(transportClass)).thenReturn(expectedTransports);
        List<Transport> actualTransports = transportService.findAllBusesByClass(transportClass);

        assertEquals(expectedTransports.size(), actualTransports.size());
        assertTrue(expectedTransports.containsAll(actualTransports));
    }

    @Test
    public void findAllAirplanesByClass_TransportClassIsNull_ExceptionThrown() {
        TransportClass transportClass = null;

        assertThrows(InvalidArgumentException.class,
                () -> transportService.findAllAirplanesByClass(transportClass));
    }

    @Test
    public void findAllAirplanesByClass_AirplanesWithGivenTransportClassDoesNotExists_ExceptionThrown() {
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
        List<Transport> expectedTransports = new ArrayList<>();
        expectedTransports.add(transportBusiness);
        expectedTransports.add(transportBusinessClass);

        when(transportRepository.findAllAirplanesByClass(transportClass)).thenReturn(expectedTransports);
        List<Transport> actualTransports = transportService.findAllAirplanesByClass(transportClass);

        assertEquals(expectedTransports.size(), actualTransports.size());
        assertTrue(expectedTransports.containsAll(actualTransports));
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
        long id = NumberUtils.LONG_ONE;

        assertThrows(InvalidArgumentException.class, () -> transportService.updateClass(transportClass, id));
    }

    @Test
    public void updateClass_TransportIdIsNegative_ExceptionThrown() {
        TransportClass transportClass = null;
        long invalidId = NumberUtils.LONG_MINUS_ONE;

        assertThrows(InvalidArgumentException.class, () -> transportService.updateClass(transportClass, invalidId));
    }

    @Test
    public void updateClass_TransportWithGivenIdDoesNotExist_ExceptionThrown() {
        long id = 1;
        long invalidId = 1024;
        TransportClass transportClass = TransportClass.ECONOMY;
        Transport transport = new Airplane(id, transportClass);

        lenient().when(transportRepository.findById(id)).thenReturn(Optional.of(transport));

        assertThrows(NonExistentItemException.class,
                () -> transportService.updateClass(transportClass, invalidId));
    }

    @Test
    public void deleteById_IdIsZero_ExceptionThrown() {
        long id = NumberUtils.LONG_ZERO;

        assertThrows(InvalidArgumentException.class, () -> transportService.deleteById(id));
    }

    @Test
    public void deleteById_TransportWithGivenIdDoesNotExist_ExceptionThrown() {
        long id = 1;
        long invalidId = 4;
        TransportClass transportClass = TransportClass.FIRST;
        Transport transport = new Bus(id, transportClass);

        lenient().when(transportRepository.findById(id)).thenReturn(Optional.of(transport));

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