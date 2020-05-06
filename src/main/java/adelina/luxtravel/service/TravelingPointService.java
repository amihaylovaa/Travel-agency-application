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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static adelina.luxtravel.utility.Constants.*;

/**
 * Represents service for traveling point
 */
@Service
@Transactional
public class TravelingPointService {
    private TravelingPointRepository travelingPointRepository;

    /**
     * Constructor
     *
     * @param travelingPointRepository traveling point repository
     */
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
        validateTravelingPointDoesNotExist(travelingPoint.getName());

        return travelingPointRepository.save(travelingPoint);
    }

    /**
     * Save list of new traveling points
     *
     * @param travelingPoints list of traveling points that are going to be saved
     * @return the saved list of traveling points
     */
    public List<TravelingPoint> saveAll(List<TravelingPoint> travelingPoints) {
        validateTravelingPointsList(travelingPoints);

        return travelingPointRepository.saveAll(travelingPoints);
    }

    /**
     * Searches for traveling point by id
     *
     * @param id traveling point's id
     * @return the found traveling point, otherwise throws exception for non-existent traveling point
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
     * Searches for traveling point by name
     *
     * @param name traveling point's name
     * @return the found traveling point, otherwise throws exception for non-existent traveling point
     */
    public TravelingPoint findByName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new InvalidArgumentException("Invalid name");
        }

        Optional<TravelingPoint> travelingPoint = travelingPointRepository.findByName(name);

        if (!travelingPoint.isPresent()) {
            throw new NonExistentItemException("Traveling point with this name does not exists");
        }

        return travelingPoint.get();
    }

    /**
     * Gets all traveling points
     *
     * @return list of traveling points, throws exception if no traveling points are presented
     */
    public List<TravelingPoint> findAll() {
        List<TravelingPoint> travelingPoints = travelingPointRepository.findAll();

        if (ObjectUtils.isEmpty(travelingPoints)) {
            throw new NonExistentItemException("There are no traveling points found");
        }

        return travelingPoints;
    }

    /**
     * Updates traveling point's name by current(old) name
     *
     * @param newName traveling point's new name
     * @param oldName traveling point's current(old) name
     */
    public void updateName(String newName, String oldName) {
        validateUpdateNameParameters(newName, oldName);

        travelingPointRepository.updateName(newName, oldName);
    }

    /**
     * Delete traveling point by id
     *
     * @param id traveling point's id
     */
    public void deleteById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }

        Optional<TravelingPoint> travelingPoint = travelingPointRepository.findById(id);

        if (!travelingPoint.isPresent()) {
            throw new NonExistentItemException("Traveling point with this id does not exist");
        }

        travelingPointRepository.deleteById(id);
    }

    private void validateTravelingPointsList(List<TravelingPoint> travelingPoints) {
        if (ObjectUtils.isEmpty(travelingPoints)) {
            throw new InvalidArgumentException("Invalid list of traveling points");
        }

        for (TravelingPoint travelingPoint : travelingPoints) {
            validateTravelingPoint(travelingPoint);
            validateTravelingPointDoesNotExist(travelingPoint.getName());
        }
    }

    private void validateTravelingPoint(TravelingPoint travelingPoint) {
        if (travelingPoint == null) {
            throw new InvalidArgumentException("Invalid traveling point");
        }
    }

    private void validateTravelingPointDoesNotExist(String travelingPointName) {
        Optional<TravelingPoint> travelingPoint = travelingPointRepository.findByName(travelingPointName);

        if (travelingPoint.isPresent()) {
            throw new AlreadyExistingItemException("Traveling point already exists");
        }
    }

    private void validateUpdateNameParameters(String newName, String oldName) {
        if (StringUtils.isEmpty(newName)) {
            throw new InvalidArgumentException("Invalid new name");
        }
        if (StringUtils.isEmpty(oldName)) {
            throw new InvalidArgumentException("Invalid old name");
        }
        if (newName.equals(oldName)) {
            throw new AlreadyExistingItemException("Traveling point with given new name already exists");
        }

        validateTravelingPointDoesNotExist(newName);
    }
}