package adelina.luxtravel.service;

import adelina.luxtravel.domain.TravelingData;
import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.domain.transport.Airplane;
import adelina.luxtravel.domain.transport.Transport;
import adelina.luxtravel.domain.transport.TransportClass;
import adelina.luxtravel.domain.wrapper.Date;
import adelina.luxtravel.domain.wrapper.DepartureDestination;
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

import static org.junit.jupiter.api.Assertions.*;

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
    void save() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByDates() {
    }

    @Test
    void findAll() {
    }

    @Test
    void updateTransport() {
    }

    @Test
    void deleteById() {
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

        return new TravelingData(id, departureDestination, transport ,date, availableTicketsCount);
    }
}