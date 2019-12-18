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

import static adelina.luxtravel.utility.Constants.NINETY_DEGREES;
import static adelina.luxtravel.utility.Constants.NINETY_DEGREES_NEGATIVE;

@Service
public class TravelingPointService {
    private TravelingPointRepository travelingPointRepository;

    @Autowired
    public TravelingPointService(TravelingPointRepository travelingPointRepository) {
        this.travelingPointRepository = travelingPointRepository;
    }

    public TravelingPoint save(TravelingPoint travelingPoint) throws InvalidArgumentException, NonExistentItemException {
        validateTravelingPoint(travelingPoint);
        return travelingPointRepository.save(travelingPoint);
    }

    public List<TravelingPoint> saveAll(List<TravelingPoint> travelingPoints) throws InvalidArgumentException, NonExistentItemException {
        validateTravelingPointsList(travelingPoints);
        return travelingPointRepository.saveAll(travelingPoints);
    }

    public TravelingPoint findById(long id) throws InvalidArgumentException, NonExistentItemException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }

        Optional<TravelingPoint> travelingPoint = travelingPointRepository.findById(id);

        if (!travelingPoint.isPresent()) {
            throw new NonExistentItemException("Traveling point with this id does not exist");
        }
        return travelingPoint.get();
    }

    public TravelingPoint findByName(String name) throws InvalidArgumentException {
        if (StringUtils.isEmpty(name)) {
            throw new InvalidArgumentException("Invalid name");
        }
        return travelingPointRepository.findByName(name);
    }

    public List<TravelingPoint> findAll() throws NonExistentItemException {
        List<TravelingPoint> travelingPoints = travelingPointRepository.findAll();

        if (ObjectUtils.isEmpty(travelingPoints)) {
            throw new NonExistentItemException("There are no traveling points found");
        }
        return travelingPoints;
    }

    public void updateName(String newName, String oldName) throws InvalidArgumentException, NonExistentItemException {
        if (StringUtils.isEmpty(newName) || StringUtils.isEmpty(oldName) || newName.equals(oldName)) {
            throw new InvalidArgumentException("Invalid arguments");
        }
        validateTravelingPointDoesNotExist(findByName(newName));
        travelingPointRepository.updateName(newName, oldName);
    }

    public void deleteById(long id) throws InvalidArgumentException {
        if (id <= NumberUtils.LONG_ZERO) {
            throw new InvalidArgumentException("Invalid id");
        }
        travelingPointRepository.deleteById(id);
    }

    private void validateTravelingPointsList(List<TravelingPoint> travelingPoints) throws InvalidArgumentException, NonExistentItemException {
        if (ObjectUtils.isEmpty(travelingPoints)) {
            throw new InvalidArgumentException("Invalid list of traveling points");
        }
        for (TravelingPoint travelingPoint : travelingPoints) {
            validateTravelingPoint(travelingPoint);
        }
    }

    private void validateTravelingPoint(TravelingPoint travelingPoint) throws InvalidArgumentException, NonExistentItemException {
        if (travelingPoint == null) {
            throw new InvalidArgumentException("Invalid traveling point");
        }
        validateFields(travelingPoint);
        validateTravelingPointDoesNotExist(travelingPoint);
    }

    private void validateFields(TravelingPoint travelingPoint) throws InvalidArgumentException {
        String name = travelingPoint.getName();
        double latitude = travelingPoint.getLatitude();
        double longitude = travelingPoint.getLongitude();

        if ((longitude > NINETY_DEGREES || longitude < NINETY_DEGREES_NEGATIVE) &&
                (latitude > NINETY_DEGREES || latitude < NINETY_DEGREES_NEGATIVE)) {
            throw new InvalidArgumentException("Invalid coordinates");
        }
        if (StringUtils.isEmpty(name)) {
            throw new InvalidArgumentException("Invalid name");
        }
    }

    private void validateTravelingPointDoesNotExist(TravelingPoint travelingPoint) throws InvalidArgumentException, NonExistentItemException {
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