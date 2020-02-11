package adelina.luxtravel.service;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.AlreadyExistingItemException;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.repository.TravelingPointRepository;
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
public class TravelingPointServiceTest {
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
        travelingPoints.add(null);

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.saveAll(travelingPoints));
    }

    @Test
    public void saveAll_CreateTravelingPointListOfTwo_SuccessfullyReturnedList() {
        List<TravelingPoint> travelingPoints = new ArrayList<>(createTravelingPointList());
        int size = travelingPoints.size();

        when(travelingPointRepository.saveAll(travelingPoints)).thenReturn(travelingPoints);
        List<TravelingPoint> createdTravelingPoints = travelingPointService.saveAll(travelingPoints);

        assertEquals(size, createdTravelingPoints.size());
    }

    @Test
    public void findById_IdIsZero_ExceptionThrown() {
        long id = NumberUtils.LONG_ZERO;

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.findById(id));
    }

    @Test
    public void findById_IdIsNegative_ExceptionThrown() {
        long id = NumberUtils.LONG_MINUS_ONE;

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
    public void updateName_NewNameIsNull_ExceptionThrown() {
        TravelingPoint travelingPoint = createTravelingPoint();
        String oldName = travelingPoint.getName();
        String invalidNewName = null;

        assertThrows(InvalidArgumentException.class,
                () -> travelingPointService.updateName(invalidNewName, oldName));
    }

    @Test
    public void updateName_NewNameIsTheSameAsTheCurrent_ExceptionThrown() {
        TravelingPoint travelingPoint = createTravelingPoint();
        Optional<TravelingPoint> travelingPointOptional = Optional.of(travelingPoint);
        String oldName = travelingPoint.getName();
        String newName = oldName;

        when(travelingPointRepository.findByName(oldName)).thenReturn(travelingPointOptional);

        assertThrows(AlreadyExistingItemException.class,
                () -> travelingPointService.updateName(newName, oldName));
    }

    @Test
    public void deleteById_IdIsLessThanZero_ExceptionThrown() {
        long invalidId = -5;

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.deleteById(invalidId));
    }

    @Test
    public void deleteById_TravelingPointWithGivenIdExists_SuccessfullyDeletedTravelingPoint() {
        TravelingPoint travelingPoint = createTravelingPoint();
        String name = travelingPoint.getName();
        long id = travelingPoint.getId();

        when(travelingPointRepository.findByName(name)).thenThrow(NonExistentItemException.class);
        travelingPointService.deleteById(id);

        assertThrows(NonExistentItemException.class, () -> travelingPointService.findByName(name));
    }

    private TravelingPoint createTravelingPoint() {
        String name = "Canada";
        double longitude = -79.35;
        double latitude = 43.65;
        long id = NumberUtils.LONG_ONE;

        return new TravelingPoint(id, name, longitude, latitude);
    }

    private List<TravelingPoint> createTravelingPointList() {
        String name = "Germany";
        double latitude = 51.13;
        double longitude = 10.01;
        long id = (NumberUtils.LONG_ONE + NumberUtils.LONG_ONE);
        TravelingPoint firstTravelingPoint = createTravelingPoint();
        TravelingPoint secondTravelingPoint = new TravelingPoint(id, name, longitude, latitude);
        List<TravelingPoint> travelingPoints = new ArrayList<>();

        travelingPoints.add(firstTravelingPoint);
        travelingPoints.add(secondTravelingPoint);

        return travelingPoints;
    }
}