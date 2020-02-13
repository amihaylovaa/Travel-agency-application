package adelina.luxtravel.service;

import adelina.luxtravel.domain.TravelingData;
import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.domain.transport.*;
import adelina.luxtravel.domain.wrapper.Date;
import adelina.luxtravel.domain.wrapper.DepartureDestination;
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
        long id = 2;
        int availableTicketsCount = travelingData.getAvailableTicketsCount();
        Transport nonExistingTransport = new Airplane(id, TransportClass.FIRST);
        DepartureDestination departureDestination = travelingData.getDepartureDestination();
        Date dates = travelingData.getDate();
        TravelingData newTravelingData = new TravelingData(id, departureDestination, nonExistingTransport, dates, availableTicketsCount);

        lenient().when(transportRepository.findById(existingTransport.getId())).thenReturn(Optional.of(existingTransport));

        assertThrows(NonExistentItemException.class, () -> travelingDataService.save(newTravelingData));
    }


    @Test
    public void save_ValidData_SuccessfullyCreatedTravelingData() {
        TravelingData expectedTravelingData = createTravelingData();
        DepartureDestination departureDestination = expectedTravelingData.getDepartureDestination();
        TravelingPoint departurePoint = departureDestination.getDeparturePoint();
        TravelingPoint destinationPoint = departureDestination.getDestinationPoint();
        Transport transport = expectedTravelingData.getTransport();

        when(travelingPointRepository.findById(departurePoint.getId())).thenReturn(Optional.of(departurePoint));
        when(travelingPointRepository.findById(destinationPoint.getId())).thenReturn(Optional.of(destinationPoint));
        when(transportRepository.findById(transport.getId())).thenReturn(Optional.of(transport));
        when(travelingDataRepository.save(expectedTravelingData)).thenReturn(expectedTravelingData);
        TravelingData actualTravelingData = travelingDataService.save(expectedTravelingData);

        assertEquals(expectedTravelingData, actualTravelingData);
    }

    @Test
    public void findById_IdIsZero_ExceptionThrown() {
        long id = NumberUtils.LONG_ZERO;

        assertThrows(InvalidArgumentException.class, () -> travelingDataService.findById(id));
    }

    @Test
    public void findById_TravelingDataWithGivenIdDoesNotExist_ExceptionThrown() {
        TravelingData travelingData = createTravelingData();
        long existingId = travelingData.getId();
        long nonExistentId = 100;

        lenient().when(travelingDataRepository.findById(existingId)).thenReturn(Optional.of(travelingData));

        assertThrows(NonExistentItemException.class, () -> travelingDataService.findById(nonExistentId));
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
    public void findByDates_DatesAreInvalid_ExceptionThrown() {
        LocalDate from = LocalDate.of(2021, 5, 12);
        LocalDate to = LocalDate.of(2021, 4, 5);

        assertThrows(InvalidArgumentException.class, () -> travelingDataService.findByDates(from, to));
    }

    @Test
    public void findByDates_EmptyTravelingDataListFortTheseDates_ExceptionThrown() {
        List<TravelingData> travelingData = new ArrayList<>();
        LocalDate from = LocalDate.of(2021, 5, 12);
        LocalDate to = LocalDate.of(2021, 5, 15);

        when(travelingDataRepository.findByDates(from, to)).thenReturn(travelingData);

        assertThrows(NonExistentItemException.class, () -> travelingDataService.findByDates(from, to));
    }

    @Test
    public void findByDates_FoundTravelingDataListFortTheseDates_ReturnedTravelingDataList() {
        List<TravelingData> expectedTravelingData = new ArrayList<>();
        TravelingData travelingData = createTravelingData();
        Date dates = travelingData.getDate();
        LocalDate from = dates.getFromDate();
        LocalDate to = dates.getToDate();
        expectedTravelingData.add(travelingData);

        when(travelingDataRepository.findByDates(from, to)).thenReturn(expectedTravelingData);
        List<TravelingData> actualTravelingData = travelingDataService.findByDates(from, to);

        assertTrue(expectedTravelingData.containsAll(actualTravelingData));
    }

    @Test
    public void findAll_ListIsEmpty_ExceptionThrown() {
        List<TravelingData> travelingData = new ArrayList<>();

        when(travelingDataRepository.findAll()).thenReturn(travelingData);

        assertThrows(NonExistentItemException.class, () -> travelingDataService.findAll());
    }

    @Test
    public void findAll_ListIsNotEmpty_ReturnedTravelingDataList() {
        List<TravelingData> expectedTravelingData = createTravelingDataList();
        int expectedSize = expectedTravelingData.size();

        when(travelingDataRepository.findAll()).thenReturn(expectedTravelingData);
        List<TravelingData> actualTravelingData = travelingDataService.findAll();

        assertEquals(expectedSize, actualTravelingData.size());
        assertTrue(actualTravelingData.containsAll(expectedTravelingData));
    }

    @Test
    public void updateTransport_TravelingDataIdIsNegative_ExceptionThrown() {
        long invalidId = NumberUtils.LONG_MINUS_ONE;
        long transportId = NumberUtils.LONG_ONE;
        TransportClass transportClass = TransportClass.FIRST;
        Transport transport = new Airplane(transportId, transportClass);

        assertThrows(InvalidArgumentException.class,
                () -> travelingDataService.updateTransport(invalidId, transport));
    }

    @Test
    public void updateTransport_TravelingDataWithGivenIdDoesNotExist_ExceptionThrown() {
        TravelingData travelingData = createTravelingData();
        long existingTravelingDataId = travelingData.getId();
        long nonExistingTravelingDataId = 12;
        long transportId = 1;
        TransportClass transportClass = TransportClass.FIRST;
        Transport transport = new Airplane(transportId, transportClass);

        lenient().when(travelingDataRepository.findById(existingTravelingDataId)).thenReturn(Optional.of(travelingData));

        assertThrows(NonExistentItemException.class,
                () -> travelingDataService.updateTransport(nonExistingTravelingDataId, transport));
    }

    @Test
    public void updateTransport_TransportIsNull_ExceptionThrown() {
        TravelingData travelingData = createTravelingData();
        long id = travelingData.getId();
        Transport transport = null;

        when(travelingDataRepository.findById(id)).thenReturn(Optional.of(travelingData));

        assertThrows(InvalidArgumentException.class, () -> travelingDataService.updateTransport(id, transport));
    }

    @Test
    public void updateTransport_TransportWithGivenIdDoesNotExist_ExceptionThrown() {
        TravelingData travelingData = createTravelingData();
        long travelingDataId = travelingData.getId();
        long nonExistentTransportId = 5;
        long existingTransportId = 1;
        TransportClass transportClass = TransportClass.FIRST;
        Transport transport = new Airplane(existingTransportId, transportClass);
        Transport nonExistentTransport = new Bus(nonExistentTransportId, transportClass);

        lenient().when(transportRepository.findById(existingTransportId)).thenReturn(Optional.of(transport));
        when(travelingDataRepository.findById(travelingDataId)).thenReturn(Optional.of(travelingData));

        assertThrows(NonExistentItemException.class,
                () -> travelingDataService.updateTransport(travelingDataId, nonExistentTransport));
    }

    @Test
    public void updateTransport_NewTransportClassIsTheSameAsTheCurrent_ExceptionThrown() {
        TravelingData travelingData = createTravelingData();
        long travelingDataId = travelingData.getId();
        Transport existingTransport = travelingData.getTransport();
        TransportClass transportClass = existingTransport.getTransportClass();
        long existingTransportId = existingTransport.getId();
        Transport updatedTransport = new Airplane(existingTransportId, transportClass);

        lenient().when(transportRepository.findById(existingTransportId)).thenReturn(Optional.of(existingTransport));
        when(travelingDataRepository.findById(travelingDataId)).thenReturn(Optional.of(travelingData));

        assertThrows(AlreadyExistingItemException.class,
                () -> travelingDataService.updateTransport(travelingDataId, updatedTransport));
    }

    @Test
    void deleteById_IdIsZero_ExceptionThrown() {
        long id = NumberUtils.LONG_ZERO;

        assertThrows(InvalidArgumentException.class, () -> travelingDataService.deleteById(id));
    }

    @Test
    void deleteById_TravelingDataWithGivenIdDoesNotExist_ExceptionThrown() {
        TravelingData travelingData = createTravelingData();
        long id = travelingData.getId();
        long nonExistentId = 145;

        lenient().when(travelingDataRepository.findById(id)).thenReturn(Optional.of(travelingData));

        assertThrows(NonExistentItemException.class, () -> travelingDataService.deleteById(nonExistentId));
    }

    private TravelingData createTravelingData() {
        long id = NumberUtils.LONG_ONE;
        int availableTicketsCount = 36;
        double departureLatitude = 42.13;
        double departureLongitude = 24.74;
        double destinationLatitude = 8.73;
        double destinationLongitude = 76.71;
        String departureName = "Plovdiv, Bulgaria";
        String destinationName = "Varkala, Kerala, India";
        TravelingPoint departurePoint = new TravelingPoint(departureName, departureLongitude, departureLatitude);
        TravelingPoint destinationPoint = new TravelingPoint(destinationName, destinationLongitude, destinationLatitude);
        DepartureDestination departureDestination = new DepartureDestination(departurePoint, destinationPoint);
        TransportClass transportClass = TransportClass.FIRST;
        Transport transport = new Airplane(id, transportClass);
        LocalDate from = LocalDate.of(2020, 4, 21);
        LocalDate to = LocalDate.of(2020, 4, 30);
        Date date = new Date(from, to);

        return new TravelingData(id, departureDestination, transport, date, availableTicketsCount);
    }

    private List<TravelingData> createTravelingDataList() {
        long id = 2;
        int availableTicketsCount = 10;
        double departureLatitude = 42.69;
        double departureLongitude = 23.31;
        double destinationLatitude = 47.31;
        double destinationLongitude = 5.01;
        String departureName = "Sofia, Bulgaria";
        String destinationName = "Dijon, the Bourgogne-Franche-Comté, France";
        TravelingPoint departurePoint = new TravelingPoint(departureName, departureLongitude, departureLatitude);
        TravelingPoint destinationPoint = new TravelingPoint(destinationName, destinationLongitude, destinationLatitude);
        DepartureDestination departureDestination = new DepartureDestination(departurePoint, destinationPoint);
        TransportClass transportClass = TransportClass.BUSINESS;
        Transport transport = new Airplane(id, transportClass);
        LocalDate from = LocalDate.of(2020, 10, 10);
        LocalDate to = LocalDate.of(2020, 10, 12);
        Date date = new Date(from, to);
        TravelingData travelingDataA = new TravelingData(id, departureDestination, transport, date, availableTicketsCount);
        TravelingData travelingDataB = createTravelingData();
        List<TravelingData> travelingData = new ArrayList<>();

        travelingData.add(travelingDataA);
        travelingData.add(travelingDataB);

        return travelingData;
    }
}