package adelina.luxtravel.service;

import adelina.luxtravel.domain.transport.Airplane;
import adelina.luxtravel.domain.transport.Bus;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.enumeration.TransportClass;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.repository.TransportRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransportServiceTest {

    @Mock
    TransportRepository transportRepository;
    @InjectMocks
    TransportService transportService;
    static Transport expectedTransport;

    @BeforeAll
    static void init() {
        long id = NumberUtils.LONG_ONE;
        TransportClass transportClass = TransportClass.ECONOMY;

        expectedTransport = new Airplane(id, transportClass);
    }

    @Test
    void save_TransportClassIsNull_ExceptionThrown() {
        TransportClass transportClass = null;
        Transport transport = new Bus(transportClass);

        assertThrows(InvalidArgumentException.class, () -> transportService.save(transport));
    }

    @Test
    void save_ValidData_SavedTransport() {

        when(transportRepository.save(expectedTransport)).thenReturn(expectedTransport);

        Transport actualTransport = transportService.save(expectedTransport);

        assertEquals(expectedTransport.getId(), actualTransport.getId());
        assertEquals(expectedTransport.getTransportClass(), actualTransport.getTransportClass());
    }

    @Test
    void findById_IdIsZero_ExceptionThrown() {
        long zeroId = NumberUtils.LONG_ZERO;

        assertThrows(InvalidArgumentException.class, () -> transportService.findById(zeroId));
    }

    @Test
    void findById_TransportWithGivenIdDoesNotExist_ExceptionThrown() {
        long nonExistentId = NumberUtils.LONG_ONE;

        when(transportRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(NonExistentItemException.class, () -> transportService.findById(nonExistentId));
    }

    @Test
    void findById_TransportWithGivenIdExists_FoundTransport() {
        long id = expectedTransport.getId();

        when(transportRepository.findById(id)).thenReturn(Optional.of(expectedTransport));

        Transport actualTransport = transportService.findById(id);

        assertEquals(expectedTransport.getId(), actualTransport.getId());
        assertEquals(expectedTransport.getTransportClass(), actualTransport.getTransportClass());
    }

    @Test
    void findSpecificTransportsByTransportClass_TransportClassIsNull_ExceptionThrown() {
        String type = "Bus";
        TransportClass transportClass = null;

        assertThrows(InvalidArgumentException.class,
                () -> transportService.findSpecificTransportsByTransportClass(transportClass, type));
    }

    @Test
    void findSpecificTransportsByTransportClass_EmptyTransportType_ExceptionThrown() {
        String type = StringUtils.EMPTY;
        TransportClass transportClass = expectedTransport.getTransportClass();

        assertThrows(InvalidArgumentException.class,
                () -> transportService.findSpecificTransportsByTransportClass(transportClass, type));
    }

    @Test
    void findSpecificTransportsByTransportClass_TransportWithGivenTransportClassAndTypeDoNotExist_ExceptionThrown() {
        String type = "Airplane";
        TransportClass transportClass = expectedTransport.getTransportClass();
        List<Transport> transports = new ArrayList<>();

        when(transportRepository.findSpecificTransportsByTransportClass(transportClass.name(), type))
                .thenReturn(transports);

        assertThrows(NonExistentItemException.class,
                () -> transportService.findSpecificTransportsByTransportClass(transportClass, type));
    }

    @Test
    void findSpecificTransportsByTransportClass_TransportWithGivenTransportClassAndTypeExists_ExceptionThrown() {
        String type = "Airplane";
        TransportClass transportClass = expectedTransport.getTransportClass();
        Transport firstTransport = expectedTransport;
        Transport secondTransport = new Airplane(2, transportClass);
        List<Transport> expectedTransports = new ArrayList<>();
        expectedTransports.add(firstTransport);
        expectedTransports.add(secondTransport);

        when(transportRepository.findSpecificTransportsByTransportClass(transportClass.name(), type))
                .thenReturn(expectedTransports);

        List<Transport> actualTransports = transportService.findSpecificTransportsByTransportClass(transportClass, type);

        assertEquals(expectedTransports.size(), actualTransports.size());
        assertTrue(actualTransports.contains(firstTransport));
        assertTrue(actualTransports.contains(secondTransport));
    }

    @Test
    void findAllAirplanes_AirplanesDoNotExist_ExceptionThrown() {
        List<Transport> emptyAirplaneList = new ArrayList<>();

        when(transportRepository.findAllAirplanes()).thenReturn(emptyAirplaneList);

        assertThrows(NonExistentItemException.class, () -> transportService.findAllAirplanes());
    }

    @Test
    void findAllBuses_BusesDoNotExist_ExceptionThrown() {
        List<Transport> emptyBusList = new ArrayList<>();

        when(transportRepository.findAllBuses()).thenReturn(emptyBusList);

        assertThrows(NonExistentItemException.class, () -> transportService.findAllBuses());
    }

    @Test
    void findAll_TransportsDoNotExist_ExceptionThrown() {
        List<Transport> emptyTransportList = new ArrayList<>();

        when(transportRepository.findAll()).thenReturn(emptyTransportList);

        assertThrows(NonExistentItemException.class, () -> transportService.findAll());
    }

    @Test
    void updateTransportClass_NullTransport_ExceptionThrown() {
        long id = NumberUtils.LONG_ONE;
        Transport transport = null;

        assertThrows(InvalidArgumentException.class,
                () -> transportService.updateTransportClass(id, transport));
    }

    @Test
    void updateTransportClass_NewTransportClassIsNull_ExceptionThrown() {
        long id = NumberUtils.LONG_ONE;
        TransportClass transportClass = null;
        Transport transportWithNullTransportClass = new Bus(id, transportClass);

        assertThrows(InvalidArgumentException.class,
                () -> transportService.updateTransportClass(id, transportWithNullTransportClass));
    }


    @Test
    void updateTransportClass_TransportIdIsNegative_ExceptionThrown() {
        long negativeId = NumberUtils.LONG_MINUS_ONE;

        assertThrows(InvalidArgumentException.class,
                () -> transportService.updateTransportClass(negativeId, expectedTransport));
    }

    @Test
    void updateTransportClass_TransportWithGivenIdDoesNotExist_ExceptionThrown() {
        long nonExistentId = NumberUtils.LONG_ONE;

        when(transportRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(NonExistentItemException.class,
                () -> transportService.updateTransportClass(nonExistentId, expectedTransport));
    }

    @Test
    void updateTransportClass_ValidData_SuccessfullyUpdatedTransport() {
        long id = expectedTransport.getId();

        when(transportRepository.findById(id)).thenReturn(Optional.of(expectedTransport));

        Transport actualTransport = transportService.updateTransportClass(id, expectedTransport);

        assertEquals(expectedTransport.getTransportClass(), actualTransport.getTransportClass());
    }

    @Test
    void deleteById_IdIsNegative_ExceptionThrown() {
        long negativeId = NumberUtils.LONG_MINUS_ONE;

        assertThrows(InvalidArgumentException.class, () -> transportService.deleteById(negativeId));
    }

    @Test
    void deleteById_TransportWithGivenIdDoesNotExist_ExceptionThrown() {
        long nonExistentId = NumberUtils.LONG_ONE;

        when(transportRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(NonExistentItemException.class, () -> transportService.deleteById(nonExistentId));
    }

    @AfterAll
    static void clear() {
        expectedTransport = null;
    }
}