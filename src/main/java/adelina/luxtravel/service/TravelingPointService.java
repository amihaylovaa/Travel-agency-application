package adelina.luxtravel.service;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.repository.TravelingPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import adelina.luxtravel.exception.InvalidArgumentException;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TravelingPointService {
    private TravelingPointRepository travelingPointRepository;

    @Autowired
    public TravelingPointService(TravelingPointRepository travelingPointRepository) {
        this.travelingPointRepository = travelingPointRepository;
    }

    public TravelingPoint save(TravelingPoint travelingPoint) {
        validateTravelingPoint(travelingPoint);
        return travelingPointRepository.save(travelingPoint);
    }

    public List<TravelingPoint> saveAll(List<TravelingPoint> travelingPoints) {
        validateListOfTravelingPoints(travelingPoints);
        return travelingPointRepository.saveAll(travelingPoints);
    }

    public TravelingPoint findByName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new InvalidArgumentException("Invalid name");
        }
        return getExistingTravelingPoint(travelingPointRepository.findByName(name));
    }

    public List<TravelingPoint> findAll() {
        return travelingPointRepository.findAll();
    }

    public void updateName(String newName, String currentName) {
        if (StringUtils.isEmpty(newName) || StringUtils.isEmpty(currentName)) {
            throw new InvalidArgumentException("Invalid arguments");
        }
        findByName(currentName);
        travelingPointRepository.updateName(newName, currentName);
    }

    public void deleteByName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new InvalidArgumentException("Invalid name");
        }
        findByName(name);
        travelingPointRepository.deleteByName(name);
    }

    public void deleteAll() {
        travelingPointRepository.deleteAll();
    }

    private void validateListOfTravelingPoints(List<TravelingPoint> travelingPoints) {
        if (travelingPoints == null || travelingPoints.isEmpty()) {
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
        validateTravelingPointFields(travelingPoint);
    }

    private void validateTravelingPointFields(TravelingPoint travelingPoint) {
        String name = travelingPoint.getName();
        double latitude = travelingPoint.getLatitude();
        double longitude = travelingPoint.getLongitude();

        if (latitude < 0.00 || longitude < 0.00 || StringUtils.isEmpty(name)) {
            throw new InvalidArgumentException("Invalid fields");
        }
    }

    private TravelingPoint getExistingTravelingPoint(TravelingPoint travelingPoint) {
        if (travelingPoint == null) {
            throw new NonExistentItemException("This traveling point does not exist");
        }
        return travelingPoint;
    }
}