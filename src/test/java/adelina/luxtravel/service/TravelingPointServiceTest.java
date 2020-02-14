package adelina.luxtravel.service;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.*;
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

import static adelina.luxtravel.utility.Utility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        String name = travelingPoint.getName();

        when(travelingPointRepository.findByName(name)).thenReturn(Optional.of(travelingPoint));

        assertThrows(AlreadyExistingItemException.class, () -> travelingPointService.save(travelingPoint));
    }

    @Test
    public void save_ValidData_CreatedTravelingPoint() {
        TravelingPoint expectedTravelingPoint = createTravelingPoint();
        String name = "Paris, France";
        double longitude = 2.34;
        double latitude = 48.86;
        TravelingPoint someExistingTravelingPoint = new TravelingPoint(name, longitude, latitude);

        lenient().when(travelingPointRepository.findByName(name)).thenReturn(Optional.of(someExistingTravelingPoint));
        when(travelingPointRepository.save(expectedTravelingPoint)).thenReturn(expectedTravelingPoint);

        TravelingPoint actualTravelingPoint = travelingPointService.save(expectedTravelingPoint);

        assertEquals(expectedTravelingPoint, actualTravelingPoint);
    }

    @Test
    public void saveAll_ListIsNull_ExceptionThrown() {
        List<TravelingPoint> travelingPoints = null;

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.saveAll(travelingPoints));
    }

    @Test
    public void saveAll_TravelingPointListHasNullElement_ExceptionThrown() {
        List<TravelingPoint> travelingPoints = new ArrayList<>(createTravelingPointList());
        travelingPoints.add(null);

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.saveAll(travelingPoints));
    }

    @Test
    public void saveAll_CreateListOfTwoTravelingPoints_CreatedList() {
        List<TravelingPoint> expectedTravelingPoints = new ArrayList<>(createTravelingPointList());
        int expectedSize = expectedTravelingPoints.size();

        when(travelingPointRepository.saveAll(expectedTravelingPoints)).thenReturn(expectedTravelingPoints);

        List<TravelingPoint> actualTravelingPoints = travelingPointService.saveAll(expectedTravelingPoints);

        assertEquals(expectedSize, actualTravelingPoints.size());
        assertTrue(expectedTravelingPoints.containsAll(actualTravelingPoints));
    }

    @Test
    public void findById_IdIsZero_ExceptionThrown() {

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.findById(ZERO_ID));
    }

    @Test
    public void findById_TravelingPointWithGivenIdDoesNotExist_ExceptionThrown() {
        TravelingPoint travelingPoint = createTravelingPoint();
        long existingId = travelingPoint.getId();

        lenient().when(travelingPointRepository.findById(existingId)).thenReturn(Optional.of(travelingPoint));

        assertThrows(NonExistentItemException.class, () -> travelingPointService.findById(NON_EXISTENT_ID));
    }

    @Test
    public void findById_TravelingPointWithGivenIdExists_FoundTravelingPoint() {
        TravelingPoint expectedTravelingPoint = createTravelingPoint();
        long id = expectedTravelingPoint.getId();

        when(travelingPointRepository.findById(id)).thenReturn(Optional.of(expectedTravelingPoint));

        TravelingPoint actualTravelingPoint = travelingPointService.findById(id);

        assertEquals(expectedTravelingPoint, actualTravelingPoint);
    }

    @Test
    public void findByName_NameIsNull_ExceptionThrown() {
        String name = null;

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.findByName(name));
    }

    @Test
    public void findByName_TravelingPointWithGivenNameDoesNotExist_ExceptionThrown() {
        TravelingPoint travelingPoint = createTravelingPoint();
        String existingName = travelingPoint.getName();
        String nonExistentName = "Kenya";

        lenient().when(travelingPointRepository.findByName(existingName)).thenReturn(Optional.of(travelingPoint));

        assertThrows(NonExistentItemException.class, () -> travelingPointService.findByName(nonExistentName));
    }

    @Test
    public void findByName_TravelingPointWithGivenNameExists_FoundTravelingPoint() {
        TravelingPoint expectedTravelingPoint = createTravelingPoint();
        String name = expectedTravelingPoint.getName();

        when(travelingPointRepository.findByName(name)).thenReturn(Optional.of(expectedTravelingPoint));

        TravelingPoint actualTravelingPoint = travelingPointService.findByName(name);

        assertEquals(expectedTravelingPoint, actualTravelingPoint);
    }

    @Test
    public void findAll_TravelingPointListIsEmpty_ExceptionThrown() {
        List<TravelingPoint> travelingPoints = new ArrayList<>();

        when(travelingPointRepository.findAll()).thenReturn(travelingPoints);

        assertThrows(NonExistentItemException.class, () -> travelingPointService.findAll());
    }

    @Test
    public void findAll_CreateListOfTwoTravelingPoints_ListFound() {
        List<TravelingPoint> expectedTravelingPoints = new ArrayList<>(createTravelingPointList());

        when(travelingPointRepository.findAll()).thenReturn(expectedTravelingPoints);
        List<TravelingPoint> actualTravelingPoints = travelingPointService.findAll();

        assertEquals(expectedTravelingPoints.size(), actualTravelingPoints.size());
        assertTrue(expectedTravelingPoints.containsAll(actualTravelingPoints));
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
        String oldName = travelingPoint.getName();
        String newName = oldName;

        lenient().when(travelingPointRepository.findByName(oldName)).thenReturn(Optional.of(travelingPoint));

        assertThrows(AlreadyExistingItemException.class, () -> travelingPointService.updateName(newName, oldName));
    }

    @Test
    public void deleteById_IdIsLessThanZero_ExceptionThrown() {

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.deleteById(NEGATIVE_ID));
    }

    @Test
    public void deleteById_TravelingPointWithGivenIdDoesNotExist_ExceptionThrown() {
        TravelingPoint travelingPoint = createTravelingPoint();
        long id = travelingPoint.getId();

        lenient().when(travelingPointRepository.findById(id)).thenReturn(Optional.of(travelingPoint));

        assertThrows(NonExistentItemException.class, () -> travelingPointService.deleteById(NON_EXISTENT_ID));
    }
}