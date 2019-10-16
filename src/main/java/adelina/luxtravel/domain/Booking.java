package adelina.luxtravel.domain;

import adelina.luxtravel.domain.transport.Vehicle;

import java.time.LocalDate;

public class Booking {
    private LocalDate from;
    private LocalDate to;
    private Continent continent;
    private Vehicle vehicle;
    private double price;

    public Booking(LocalDate from, LocalDate to, Continent continent, Vehicle vehicle) {
        this.from = from;
        this.to = to;
        this.continent = continent;
        this.vehicle = vehicle;
    }
}
