package adelina.luxtravel.service;

import adelina.luxtravel.repository.ExcursionTransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents service for an excursion-transport
 */
@Service
public class ExcursionTransportService {

    private final ExcursionTransportRepository excursionTransportRepository;

    @Autowired
    public ExcursionTransportService(ExcursionTransportRepository excursionTransportRepository) {
        this.excursionTransportRepository = excursionTransportRepository;
    }

    /**
     * Gets excursion's id by transport's id and price
     *
     * @param transportId transport's id for a given excursion
     * @param price       excursion's price
     * @return the searched id
     */
    public long getExcursionId(long transportId, double price) {
        return excursionTransportRepository.getExcursionId(transportId, price);
    }
}
