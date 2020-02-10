package adelina.luxtravel.service;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.AlreadyExistingItemException;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.repository.TravelingPointRepository;
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
class TravelingPointServiceTest {

    @Mock
    TravelingPointRepository travelingPointRepository;
    @InjectMocks
    TravelingPointService travelingPointService;


    @Test
    public void save_TravelingPointIsNull_ExceptionThrown() {
        TravelingPoint travelingPoint = null;

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.save(travelingPoint));
    }

    @Test
    public void save_TravelingPointAlreadyExists_ExceptionThrown() {
        TravelingPoint travelingPoint = createTravelingPoint();
        Optional<TravelingPoint> travelingPointOptional = Optional.of(travelingPoint);
        String name = travelingPoint.getName();

        when(travelingPointRepository.findByName(name)).thenReturn(travelingPointOptional);

        assertThrows(AlreadyExistingItemException.class, () -> travelingPointService.save(travelingPoint));
    }

    @Test
    public void save_ValidData_SuccessfullyCreatedTravelingPoint() {
        TravelingPoint travelingPoint = createTravelingPoint();
        Optional<TravelingPoint> travelingPointOptional = Optional.of(travelingPoint);
        String name = travelingPoint.getName();
        double latitude = travelingPoint.getLatitude();
        double longitude = travelingPoint.getLongitude();

        when(travelingPointRepository.findByName(name)).thenReturn(travelingPointOptional);
        TravelingPoint createdTravelingPoint = travelingPointService.findByName(name);

        assertEquals(name, createdTravelingPoint.getName());
        assertEquals(longitude, createdTravelingPoint.getLongitude());
        assertEquals(latitude, createdTravelingPoint.getLatitude());
    }

    @Test
    public void saveAll_ListIsEmpty_ExceptionThrown() {
        List<TravelingPoint> travelingPoints = new ArrayList<>();

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.saveAll(travelingPoints));
    }

    @Test
    public void saveAll_ListIsNull_ExceptionThrown() {
        List<TravelingPoint> travelingPoints = null;

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.saveAll(travelingPoints));
    }

    @Test
    public void saveAll_OneOfTheElementsIsNull_ExceptionThrown() {
        List<TravelingPoint> travelingPoints = new ArrayList<>(createTravelingPointList());
        TravelingPoint invalidTravelingPoint = null;
        travelingPoints.add(invalidTravelingPoint);

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.saveAll(travelingPoints));
    }

    @Test
    public void saveAll_CreateTravelingPointListOfTwo_SuccessfullyReturnedList() {
        List<TravelingPoint> travelingPoints = new ArrayList<>(createTravelingPointList());
        int size = travelingPoints.size();

        when(travelingPointRepository.saveAll(travelingPoints)).thenReturn(travelingPoints);

        assertEquals(size, travelingPointService.saveAll(travelingPoints).size());
    }

    @Test
    public void findById_IdIsZero_ExceptionThrown() {
        long id = 0;

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.findById(id));
    }

    @Test
    public void findById_IdIsNegative_ExceptionThrown() {
        long id = -123;

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.findById(id));
    }

    @Test
    public void findById_TravelingPointWithGivenIdDoesNotExist_ExceptionThrown() {
        TravelingPoint travelingPoint = createTravelingPoint();
        Optional<TravelingPoint> travelingPointOptional = Optional.of(travelingPoint);
        long id = travelingPoint.getId();
        long invalidId = 12;

        lenient().when(travelingPointRepository.findById(id)).thenReturn(travelingPointOptional);

        assertThrows(NonExistentItemException.class, () -> travelingPointService.findById(invalidId));
    }

    @Test
    public void findById_TravelingPointWithGivenIdExists_ReturnedTravelingPoint() {
        TravelingPoint travelingPoint = createTravelingPoint();
        Optional<TravelingPoint> travelingPointOptional = Optional.of(travelingPoint);
        String name = travelingPoint.getName();
        double latitude = travelingPoint.getLatitude();
        double longitude = travelingPoint.getLongitude();
        long id = travelingPoint.getId();

        when(travelingPointRepository.findById(id)).thenReturn(travelingPointOptional);
        TravelingPoint foundTravelingPoint = travelingPointService.findById(id);

        assertEquals(id, foundTravelingPoint.getId());
        assertEquals(name, foundTravelingPoint.getName());
        assertEquals(latitude, travelingPoint.getLatitude());
        assertEquals(longitude, travelingPoint.getLongitude());
    }

    @Test
    public void findById_NameIsNull_ExceptionThrown() {
        String name = null;

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.findByName(name));
    }

    @Test
    public void findById_TravelingPointWithGivenNameDoesNotExist_ExceptionThrown() {
        TravelingPoint travelingPoint = createTravelingPoint();
        Optional<TravelingPoint> travelingPointOptional = Optional.of(travelingPoint);
        String name = travelingPoint.getName();
        String nonExistentTravelingPointName = "Kenya";

        lenient().when(travelingPointRepository.findByName(name)).thenReturn(travelingPointOptional);

        assertThrows(NonExistentItemException.class,
                () -> travelingPointService.findByName(nonExistentTravelingPointName));
    }

    @Test
    public void findById_TravelingPointWithGivenNameExists_ReturnedTravelingPoint() {
        TravelingPoint travelingPoint = createTravelingPoint();
        Optional<TravelingPoint> travelingPointOptional = Optional.of(travelingPoint);
        String name = travelingPoint.getName();
        double latitude = travelingPoint.getLatitude();
        double longitude = travelingPoint.getLongitude();
        long id = travelingPoint.getId();

        when(travelingPointRepository.findByName(name)).thenReturn(travelingPointOptional);
        TravelingPoint foundTravelingPoint = travelingPointService.findByName(name);

        assertEquals(id, foundTravelingPoint.getId());
        assertEquals(name, foundTravelingPoint.getName());
        assertEquals(latitude, travelingPoint.getLatitude());
        assertEquals(longitude, travelingPoint.getLongitude());
    }

    @Test
    public void findAll_TravelingPointListIsEmpty_ExceptionThrown() {
        List<TravelingPoint> travelingPoints = new ArrayList<>();

        when(travelingPointRepository.findAll()).thenReturn(travelingPoints);

        assertThrows(NonExistentItemException.class, () -> travelingPointService.findAll());
    }

    @Test
    public void findAll_TravelingPointListHasTwoElements_ListReturned() {
        List<TravelingPoint> travelingPoints = new ArrayList<>(createTravelingPointList());

        when(travelingPointRepository.findAll()).thenReturn(travelingPoints);
        List<TravelingPoint> foundTravelingPoints = travelingPointService.findAll();

        assertEquals(travelingPoints, foundTravelingPoints);
        assertEquals(travelingPoints.size(), foundTravelingPoints.size());
    }


    @Test
    void updateName() {
    }

    @Test
    void deleteById() {
    }


    private TravelingPoint createTravelingPoint() {
        String name = "Canada";
        double longitude = -79.35;
        double latitude = 43.65;
        long id = 1;

        return new TravelingPoint(id, name, longitude, latitude);
    }

    private List<TravelingPoint> createTravelingPointList() {
        String name = "Germany";
        double latitude = 51.13;
        double longitude = 10.01;
        long id = 2;
        TravelingPoint firstTravelingPoint = createTravelingPoint();
        TravelingPoint secondTravelingPoint = new TravelingPoint(id, name, longitude, latitude);
        List<TravelingPoint> travelingPoints = new ArrayList<>();

        travelingPoints.add(firstTravelingPoint);
        travelingPoints.add(secondTravelingPoint);

        return travelingPoints;
    }
}