package adelina.luxtravel.service;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.*;
import adelina.luxtravel.repository.TravelingPointRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import adelina.luxtravel.exception.InvalidArgumentException;

import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Constants.*;

/**
 * Represents service for a traveling point
 */
@Service
public class TravelingPointService {

    private final TravelingPointRepository travelingPointRepository;

    @Autowired
    public TravelingPointService(TravelingPointRepository travelingPointRepository) {
        this.travelingPointRepository = travelingPointRepository;
    }

    /**
     * Saves new traveling point
     *
     * @param travelingPoint the traveling point that is going to be saved
     * @return the saved traveling point
     */
    public TravelingPoint save(TravelingPoint travelingPoint) {
        validateTravelingPoint(travelingPoint);

        return travelingPointRepository.save(travelingPoint);
    }

    /**
     * Gets traveling point by id
     *
     * @param id traveling point's id
     * @return if present - the searched traveling point, otherwise throws exception for non-existent traveling point
     */
    public TravelingPoint findById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        Optional<TravelingPoint> travelingPoint = travelingPointRepository.findById(id);

        if (!travelingPoint.isPresent()) {
            throw new NonExistentItemException("Traveling point with this id does not exist");
        }

        return travelingPoint.get();
    }

    /**
     * Gets traveling points by name
     *
     * @param name traveling point's name
     * @return if present - the searched traveling points, otherwise throws exception for non-existent traveling points
     */
    public List<TravelingPoint> findByName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new InvalidArgumentException("Invalid name");
        }

        List<TravelingPoint> travelingPoints = travelingPointRepository.findByNameContaining(name);

        if (ObjectUtils.isEmpty(travelingPoints)) {
            throw new NonExistentItemException("Traveling points with this name do not exist");
        }

        return travelingPoints;
    }

    /**
     * Gets all traveling points
     *
     * @return list of traveling points, throws exception if no traveling points are found
     */
    public List<TravelingPoint> findAll() {
        List<TravelingPoint> travelingPoints = travelingPointRepository.findAll();

        if (ObjectUtils.isEmpty(travelingPoints)) {
            throw new NonExistentItemException("Traveling points are not found");
        }

        return travelingPoints;
    }

    /**
     * Deletes traveling point by id
     *
     * @param id traveling point's id
     * @return true for successfully deleted traveling point, false for unsuccessful attempt
     */
    public boolean deleteById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        Optional<TravelingPoint> travelingPoint = travelingPointRepository.findById(id);

        if (!travelingPoint.isPresent()) {
            throw new NonExistentItemException("Traveling point with given ID does not exist");
        }

        travelingPointRepository.deleteById(id);

        try {
            travelingPointRepository.findById(id);
        } catch (NonExistentItemException e) {
            return false;
        }
        return true;
    }

    private void validateTravelingPoint(TravelingPoint travelingPoint) {
        if (ObjectUtils.isEmpty(travelingPoint)) {
            throw new InvalidArgumentException("Invalid traveling point");
        }

        String name = travelingPoint.getName();
        double latitude = travelingPoint.getLatitude();
        double longitude = travelingPoint.getLongitude();

        if (StringUtils.isEmpty(name)) {
            throw new InvalidArgumentException("Invalid name");
        }

        if (latitude < MIN_LATITUDE || latitude > MAX_LATITUDE) {
            throw new InvalidArgumentException("Invalid longitude");
        }

        if (longitude < MIN_LONGITUDE || longitude > MAX_LONGITUDE) {
            throw new InvalidArgumentException("Invalid longitude");
        }
    }
}