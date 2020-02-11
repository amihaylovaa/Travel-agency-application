package adelina.luxtravel.service;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.AlreadyExistingItemException;
import adelina.luxtravel.exception.NonExistentItemException;
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

@Service
public class TravelingPointService {
    private TravelingPointRepository travelingPointRepository;

    @Autowired
    public TravelingPointService(TravelingPointRepository travelingPointRepository) {
        this.travelingPointRepository = travelingPointRepository;
    }

    public TravelingPoint save(TravelingPoint travelingPoint) {
        validateTravelingPoint(travelingPoint);
        validateTravelingPointDoesNotExist(travelingPoint);

        return travelingPointRepository.save(travelingPoint);
    }

    public List<TravelingPoint> saveAll(List<TravelingPoint> travelingPoints) {
        validateTravelingPointsList(travelingPoints);
        return travelingPointRepository.saveAll(travelingPoints);
    }

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

    public List<TravelingPoint> findAll() {
        List<TravelingPoint> travelingPoints = travelingPointRepository.findAll();

        if (ObjectUtils.isEmpty(travelingPoints)) {
            throw new NonExistentItemException("There are no traveling points found");
        }
        return travelingPoints;
    }

    public void updateName(String newName, String oldName) {
        if (StringUtils.isEmpty(newName)) {
            throw new InvalidArgumentException("Invalid new name");
        }
        if (StringUtils.isEmpty(oldName)) {
            throw new InvalidArgumentException("Invalid old name");
        }

        TravelingPoint existingTravelingPoint = findByName(oldName);

        if (newName.equals(existingTravelingPoint.getName())) {
            throw new AlreadyExistingItemException("Traveling point with this name already exists");
        }

        travelingPointRepository.updateName(newName, oldName);
    }

    public void deleteById(long id) {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException(INVALID_ID);
        }
        travelingPointRepository.deleteById(id);
    }

    private void validateTravelingPointsList(List<TravelingPoint> travelingPoints) {
        if (ObjectUtils.isEmpty(travelingPoints)) {
            throw new InvalidArgumentException("Invalid list of traveling points");
        }
        for (TravelingPoint travelingPoint : travelingPoints) {
            validateTravelingPoint(travelingPoint);
        }
    }

    private void validateTravelingPoint(TravelingPoint travelingPoint) {
        if (travelingPoint == null) {
            throw new InvalidArgumentException("Invalid traveling point");
        }
    }

    private void validateTravelingPointDoesNotExist(TravelingPoint travelingPoint) {
        String name = travelingPoint.getName();
        double latitude = travelingPoint.getLatitude();
        double longitude = travelingPoint.getLongitude();

        TravelingPoint existingTravelingPoint = findByName(name);

        if (name.equals(existingTravelingPoint.getName())
                && latitude == existingTravelingPoint.getLatitude()
                && longitude == existingTravelingPoint.getLongitude()) {
            throw new AlreadyExistingItemException("This traveling point already exists");
        }
    }
}