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

import static adelina.luxtravel.utility.Utility.*;
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

    @Test
    public void saveBus_ValidData_CreatedBus() {
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

    @Test
    public void saveAirplane_ValidData_CreatedAirplane() {
        TransportClass transportClass = TransportClass.BUSINESS;
        Transport expectedTransport = new Airplane(transportClass);

        when(transportRepository.saveAirplane(transportClass)).thenReturn((Airplane) expectedTransport);

        Transport actualTransport = transportService.saveAirplane(expectedTransport);

        assertEquals(expectedTransport, actualTransport);
    }

    @Test
    public void saveAll_TransportListIsEmpty_ExceptionThrown() {
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
    public void saveAll_ValidData_CreatedList() {
        List<Transport> expectedTransports = new ArrayList<>(createTransportList());
        int expectedSize = expectedTransports.size();

        when(transportRepository.saveAll(expectedTransports)).thenReturn(expectedTransports);

        List<Transport> actualTransports = transportService.saveAll(expectedTransports);

        assertEquals(expectedSize, actualTransports.size());
        assertTrue(expectedTransports.containsAll(actualTransports));
    }

    @Test
    public void findById_IdIsEqualToZero_ExceptionThrown() {

        assertThrows(InvalidArgumentException.class, () -> transportService.findById(ZERO_ID));
    }

    @Test
    public void findById_TransportWithGivenIdDoesNotExist_ExceptionThrown() {
        long existingId = NumberUtils.LONG_ONE;
        TransportClass transportClass = TransportClass.BUSINESS;
        Transport transport = new Airplane(existingId, transportClass);

        lenient().when(transportRepository.findById(existingId)).thenReturn(Optional.of(transport));

        assertThrows(NonExistentItemException.class, () -> transportService.findById(NON_EXISTENT_ID));
    }

    @Test
    public void findById_TransportWithGivenIdExists_FoundTransport() {
        long id = NumberUtils.LONG_ONE;
        TransportClass transportClass = TransportClass.BUSINESS;
        Transport expectedTransport = new Airplane(id, transportClass);

        when(transportRepository.findById(id)).thenReturn(Optional.of(expectedTransport));

        Transport actualTransport = transportService.findById(id);

        assertEquals(expectedTransport, actualTransport);
    }

    @Test
    public void findAllBusesByClass_TransportClassIsNull_ExceptionThrown() {
        TransportClass transportClass = null;

        assertThrows(InvalidArgumentException.class, () -> transportService.findAllBusesByClass(transportClass));
    }

    @Test
    public void findAllBusesByClass_BusesWithGivenTransportClassDoesNotExists_ExceptionThrown() {
        TransportClass transportClass = TransportClass.FIRST;

        when(transportRepository.findAllBusesByClass(transportClass)).thenThrow(NonExistentItemException.class);

        assertThrows(NonExistentItemException.class, () -> transportService.findAllBusesByClass(transportClass));
    }

    @Test
    public void findAllBusesByClass_BusesWithGivenTransportClassExists_FoundList() {
        TransportClass transportClass = TransportClass.ECONOMY;
        Transport transportA = new Bus(transportClass);
        Transport transportB = new Bus(transportClass);
        List<Transport> expectedTransports = new ArrayList<>();
        expectedTransports.add(transportA);
        expectedTransports.add(transportB);

        when(transportRepository.findAllBusesByClass(transportClass)).thenReturn(expectedTransports);

        List<Transport> actualTransports = transportService.findAllBusesByClass(transportClass);

        assertEquals(expectedTransports.size(), actualTransports.size());
        assertTrue(expectedTransports.containsAll(actualTransports));
    }

    @Test
    public void findAllAirplanesByClass_TransportClassIsNull_ExceptionThrown() {
        TransportClass transportClass = null;

        assertThrows(InvalidArgumentException.class, () -> transportService.findAllAirplanesByClass(transportClass));
    }

    @Test
    public void findAllAirplanesByClass_AirplanesWithGivenTransportClassDoesNotExists_ExceptionThrown() {
        TransportClass transportClass = TransportClass.ECONOMY;

        when(transportRepository.findAllAirplanesByClass(transportClass)).thenThrow(NonExistentItemException.class);

        assertThrows(NonExistentItemException.class, () -> transportService.findAllAirplanesByClass(transportClass));
    }

    @Test
    public void findAllAirplanesByClass_AirplanesWithGivenTransportClassExists_FoundList() {
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
        TransportClass transportClass = TransportClass.ECONOMY;

        assertThrows(InvalidArgumentException.class, () -> transportService.updateClass(transportClass, NEGATIVE_ID));
    }

    @Test
    public void updateClass_TransportWithGivenIdDoesNotExist_ExceptionThrown() {
        long id = NumberUtils.LONG_ONE;
        TransportClass transportClass = TransportClass.ECONOMY;
        Transport transport = new Airplane(id, transportClass);

        lenient().when(transportRepository.findById(id)).thenReturn(Optional.of(transport));

        assertThrows(NonExistentItemException.class,
                () -> transportService.updateClass(transportClass, NON_EXISTENT_ID));
    }

    @Test
    public void deleteById_IdIsZero_ExceptionThrown() {

        assertThrows(InvalidArgumentException.class, () -> transportService.deleteById(ZERO_ID));
    }

    @Test
    public void deleteById_TransportWithGivenIdDoesNotExist_ExceptionThrown() {
        long id = NumberUtils.LONG_ONE;
        TransportClass transportClass = TransportClass.FIRST;
        Transport transport = new Bus(id, transportClass);

        lenient().when(transportRepository.findById(id)).thenReturn(Optional.of(transport));

        assertThrows(NonExistentItemException.class, () -> transportService.deleteById(NON_EXISTENT_ID));
    }
}