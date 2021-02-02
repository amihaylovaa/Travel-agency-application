package adelina.luxtravel.service;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.InvalidArgumentException;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.repository.TravelingPointRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelingPointServiceTest {

    @Mock
    TravelingPointRepository travelingPointRepository;
    @InjectMocks
    TravelingPointService travelingPointService;
    static TravelingPoint expectedTravelingPoint;

    @BeforeAll
    static void init() {
        long id = 3;
        String name = "Sofia, Bulgaria";
        double latitude = 42.698334;
        double longitude = 23.319941;

        expectedTravelingPoint = new TravelingPoint(id, name, longitude, latitude);
    }

    @Test
    void save_TravelingPointIsNull_ExceptionThrown() {
        TravelingPoint travelingPoint = null;

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.save(travelingPoint));
    }

    @Test
    void save_TravelingPointNameIsEmpty_ExceptionThrown() {
        long id = expectedTravelingPoint.getId();
        double latitude = expectedTravelingPoint.getLatitude();
        double longitude = expectedTravelingPoint.getLongitude();
        String name = StringUtils.EMPTY;
        TravelingPoint travelingPoint = new TravelingPoint(id, name, longitude, latitude);

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.save(travelingPoint));
    }

    @Test
    void save_TravelingPointLatitudeIsBelowMinimum_ExceptionThrown() {
        long id = expectedTravelingPoint.getId();
        double latitude = -245.67;
        double longitude = expectedTravelingPoint.getLongitude();
        String name = expectedTravelingPoint.getName();
        TravelingPoint travelingPoint = new TravelingPoint(id, name, longitude, latitude);

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.save(travelingPoint));
    }

    @Test
    void save_TravelingPointLatitudeIsAboveMaximum_ExceptionThrown() {
        long id = expectedTravelingPoint.getId();
        double latitude = expectedTravelingPoint.getLatitude();
        double longitude = 234.56;
        String name = expectedTravelingPoint.getName();
        TravelingPoint travelingPoint = new TravelingPoint(id, name, longitude, latitude);

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.save(travelingPoint));
    }

    @Test
    void save_ValidData_SavedTravelingPoint() {
        when(travelingPointRepository.save(expectedTravelingPoint)).thenReturn(expectedTravelingPoint);

        TravelingPoint actualTravelingPoint = travelingPointService.save(expectedTravelingPoint);

        assertEquals(expectedTravelingPoint.getId(), actualTravelingPoint.getId());
        assertEquals(expectedTravelingPoint.getLatitude(), actualTravelingPoint.getLatitude(), 0);
        assertEquals(expectedTravelingPoint.getLongitude(), actualTravelingPoint.getLongitude(), 0);
    }

    @Test
    void findById_IdIsZero_ExceptionThrown() {
        long id = NumberUtils.LONG_ZERO;

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.findById(id));
    }

    @Test
    void findById_TravelingPointWithGiveIdDoesNotExist_ExceptionThrown() {
        long id = NumberUtils.LONG_ONE;

        when(travelingPointRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NonExistentItemException.class, () -> travelingPointService.findById(id));
    }

    @Test
    void findById_TravelingPointWithGiveIdExists_FoundTravelingPoint() {
        long id = expectedTravelingPoint.getId();

        when(travelingPointRepository.findById(id)).thenReturn(Optional.of(expectedTravelingPoint));

        TravelingPoint actualTravelingPoint = travelingPointService.findById(id);

        assertEquals(expectedTravelingPoint.getId(), actualTravelingPoint.getId());
        assertEquals(expectedTravelingPoint.getLongitude(), actualTravelingPoint.getLongitude(), 0);
        assertEquals(expectedTravelingPoint.getLatitude(), actualTravelingPoint.getLatitude(), 0);
    }

    @Test
    void findByName_TravelingPointNameIsEmpty_ExceptionThrown() {
        String name = StringUtils.EMPTY;

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.findByName(name));
    }

    @Test
    void findByName_TravelingPointsNameWithGivenNameDoNotExist_ExceptionThrown() {
        String name = expectedTravelingPoint.getName();
        List<TravelingPoint> emptyList = new ArrayList<>();

        when(travelingPointRepository.findByNameContaining(name)).thenReturn((emptyList));

        assertThrows(NonExistentItemException.class, () -> travelingPointService.findByName(name));
    }

    @Test
    void findByName_TravelingPointsNameWithGivenNameExist_TravelingPointsContainingGivenName() {
        String name = expectedTravelingPoint.getName();
        List<TravelingPoint> expectedTravelingPoints = new ArrayList<>();
        expectedTravelingPoints.add(expectedTravelingPoint);

        when(travelingPointRepository.findByNameContaining(name)).thenReturn((expectedTravelingPoints));

        List<TravelingPoint> actualTravelingPoints = travelingPointService.findByName(name);

        assertEquals(expectedTravelingPoints.size(), actualTravelingPoints.size());
        assertTrue(expectedTravelingPoints.containsAll(actualTravelingPoints));
    }

    @Test
    void findAll_TravelingPointsAreNotFound_ExceptionThrown() {

        assertThrows(NonExistentItemException.class, () -> travelingPointService.findAll());
    }

    @Test
    void deleteById_IdIsNegative_ExceptionThrown() {
        long negativeId = NumberUtils.LONG_MINUS_ONE;

        assertThrows(InvalidArgumentException.class, () -> travelingPointService.deleteById(negativeId));
    }

    @Test
    void deleteById_TravelingPointWithGivenIdDoesNotExist_ExceptionThrown() {
        long id = expectedTravelingPoint.getId();

        when(travelingPointRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NonExistentItemException.class, () -> travelingPointService.deleteById(id));
    }
}