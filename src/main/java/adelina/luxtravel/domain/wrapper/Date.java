package adelina.luxtravel.domain.wrapper;

import adelina.luxtravel.exception.FailedInitializationException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Embeddable
public class Date {
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate fromDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
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