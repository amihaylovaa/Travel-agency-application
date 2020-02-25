package adelina.luxtravel.service;

import adelina.luxtravel.domain.TravelingData;
import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.domain.transport.*;
import adelina.luxtravel.domain.wrapper.*;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.TransportRepository;
import adelina.luxtravel.repository.TravelingDataRepository;
import adelina.luxtravel.repository.TravelingPointRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Utility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TravelingDataServiceTest {
    @Mock
    private TravelingDataRepository travelingDataRepository;
    @Mock
    private TransportRepository transportRepository;
    @Mock
    private TravelingPointRepository travelingPointRepository;
    @InjectMocks
    private TravelingDataService travelingDataService;

    @Test
    public void save_TravelingDataIsNull_ExceptionThrown() {
        TravelingData travelingData = null;

        assertThrows(InvalidArgumentException.class, () -> travelingDataService.save(travelingData));
    }

    @Test
    public void save_TransportDoesNotExist_ExceptionThrown() {
        TravelingData travelingData = createTravelingData();
        Transport existingTransport = travelingData.getTransport();
        TravelingData travelingDataWithNonExistingTransport = createTravelingDataWithNonExistingTransport();
        long existingTransportId = existingTransport.getId();

        lenient().when(transportRepository.findById(existingTransportId)).thenReturn(Optional.of(existingTransport));

        assertThrows(NonExistentItemException.class, () -> travelingDataService.save(travelingDataWithNonExistingTransport));
    }

    @Test
    public void save_ValidData_CreatedTravelingData() {
        TravelingData expectedTravelingData = createTravelingData();
        DepartureDestination departureDestination = expectedTravelingData.getDepartureDestination();
        Transport transport = expectedTravelingData.getTransport();
        TravelingPoint departurePoint = departureDestination.getDeparturePoint();
        TravelingPoint destinationPoint = departureDestination.getDestinationPoint();

        when(travelingPointRepository.findById(departurePoint.getId())).thenReturn(Optional.of(departurePoint));
        lenient().when(travelingPointRepository.findById(destinationPoint.getId())).thenReturn(Optional.of(destinationPoint));
        lenient().when(transportRepository.findById(transport.getId())).thenReturn(Optional.of(transport));
        lenient().when(travelingDataRepository.save(expectedTravelingData)).thenReturn(expectedTravelingData);

        TravelingData actualTravelingData = travelingDataService.save(expectedTravelingData);

        assertEquals(expectedTravelingData, actualTravelingData);
    }

    @Test
    public void findById_IdIsZero_ExceptionThrown() {

        assertThrows(InvalidArgumentException.class, () -> travelingDataService.findById(ZERO_ID));
    }

    @Test
    public void findById_TravelingDataWithGivenIdDoesNotExist_ExceptionThrown() {
        TravelingData travelingData = createTravelingData();
        long existingId = travelingData.getId();

        lenient().when(travelingDataRepository.findById(existingId)).thenReturn(Optional.of(travelingData));

        assertThrows(NonExistentItemException.class, () -> travelingDataService.findById(NON_EXISTENT_ID));
    }

    @Test
    public void findById_TravelingDataWithGivenIdExists_FoundTravelingData() {
        TravelingData expectedTravelingData = createTravelingData();
        long id = expectedTravelingData.getId();

        lenient().when(travelingDataRepository.findById(id)).thenReturn(Optional.of(expectedTravelingData));

        TravelingData actualTravelingData = travelingDataService.findById(id);

        assertEquals(expectedTravelingData, actualTravelingData);
    }

    @Test
    public void findByDates_EmptyTravelingDataListFortTheseDates_ExceptionThrown() {
        List<TravelingData> travelingData = new ArrayList<>();
        LocalDate from = LocalDate.of(2021, 5, 12);
        LocalDate to = LocalDate.of(2021, 5, 15);
        Date dates = new Date(from, to);

        when(travelingDataRepository.findByDates(from, to)).thenReturn(travelingData);

        assertThrows(NonExistentItemException.class, () -> travelingDataService.findByDates(dates));
    }

    @Test
    public void findByDates_TravelingDataListFortTheseDatesIsNotEmpty_FoundTravelingDataList() {
        List<TravelingData> expectedTravelingData = new ArrayList<>();
        TravelingData travelingData = createTravelingData();
        Date dates = travelingData.getDate();
        LocalDate from = dates.getFromDate();
        LocalDate to = dates.getToDate();
        expectedTravelingData.add(travelingData);

        when(travelingDataRepository.findByDates(from, to)).thenReturn(expectedTravelingData);

        List<TravelingData> actualTravelingData = travelingDataService.findByDates(dates);

        assertEquals(expectedTravelingData.size(), actualTravelingData.size());
        assertTrue(expectedTravelingData.containsAll(actualTravelingData));
    }

    @Test
    public void findAll_ListIsEmpty_ExceptionThrown() {
        List<TravelingData> travelingData = new ArrayList<>();

        when(travelingDataRepository.findAll()).thenReturn(travelingData);

        assertThrows(NonExistentItemException.class, () -> travelingDataService.findAll());
    }

    @Test
    public void findAll_ListIsNotEmpty_FoundTravelingDataList() {
        List<TravelingData> expectedTravelingData = createTravelingDataList();
        int expectedSize = expectedTravelingData.size();

        when(travelingDataRepository.findAll()).thenReturn(expectedTravelingData);

        List<TravelingData> actualTravelingData = travelingDataService.findAll();

        assertEquals(expectedSize, actualTravelingData.size());
        assertTrue(actualTravelingData.containsAll(expectedTravelingData));
    }

    @Test
    public void deleteById_IdIsZero_ExceptionThrown() {

        assertThrows(InvalidArgumentException.class, () -> travelingDataService.deleteById(ZERO_ID));
    }

    @Test
    public void deleteById_TravelingDataWithGivenIdDoesNotExist_ExceptionThrown() {
        TravelingData travelingData = createTravelingData();
        long id = travelingData.getId();

        lenient().when(travelingDataRepository.findById(id)).thenReturn(Optional.of(travelingData));

        assertThrows(NonExistentItemException.class, () -> travelingDataService.deleteById(NON_EXISTENT_ID));
    }
}