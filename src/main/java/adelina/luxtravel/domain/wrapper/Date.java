package adelina.luxtravel.domain.wrapper;

import adelina.luxtravel.exception.FailedInitializationException;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Getter
@Embeddable
public class Date {
    private LocalDate fromDate;
    private LocalDate toDate;

    public Date(LocalDate fromDate, LocalDate toDate) {
        setBookingDates(fromDate, toDate);
    }

    private void setBookingDates(LocalDate fromDate, LocalDate toDate) {
        try {
            if (fromDate.isAfter(toDate) || fromDate.isEqual(toDate) || fromDate.isBefore(LocalDate.now())) {
                throw new FailedInitializationException("Invalid dates");
            }
        } catch (NullPointerException npe) {
            throw new FailedInitializationException("Null dates are forbidden");
        }
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}