package adelina.luxtravel.service;

import adelina.luxtravel.domain.TravelingPoint;
import adelina.luxtravel.exception.NonExistentItemException;
import adelina.luxtravel.repository.TravelingPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import adelina.luxtravel.exception.InvalidArgumentException;
import org.springframework.util.StringUtils;

@Service
public class TravelingPointService {
    private TravelingPointRepository travelingPointRepository;

    @Autowired
    public TravelingPointService(TravelingPointRepository travelingPointRepository) {
        this.travelingPointRepository = travelingPointRepository;
    }

    public TravelingPoint findByName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new InvalidArgumentException("Invalid name");
        }

        TravelingPoint travelingPoint = travelingPointRepository.findByName(name);

        if (travelingPoint == null) {
            throw new NonExistentItemException("Traveling point with that name does not exist");
        }
        return travelingPoint;
    }

    public void update(TravelingPoint travelingPoint, String currentName) {
        if (travelingPoint == null) {
            throw new InvalidArgumentException("Invalid traveling point");
        }

        findByName(currentName);

        double longitude = travelingPoint.getLongitude();
        double latitude = travelingPoint.getLatitude();
        String newName = travelingPoint.getName();

        travelingPointRepository.update(latitude, longitude, newName, currentName);
    }

    public void deleteByName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new InvalidArgumentException("Invalid name");
        }
        findByName(name);
        travelingPointRepository.deleteByName(name);
    }
}