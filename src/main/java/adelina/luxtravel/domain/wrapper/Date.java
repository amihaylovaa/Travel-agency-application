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
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Embeddable
public class Date {
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull(message = "Starting date can not be null")
    private LocalDate fromDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull(message = "Ending date can not be null")
    private LocalDate toDate;

    public Date(LocalDate fromDate, LocalDate toDate) {
        setBookingDates(fromDate, toDate);
    }

    private void setBookingDates(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate) || fromDate.isEqual(toDate) || fromDate.isBefore(LocalDate.now())) {
            throw new FailedInitializationException("Invalid dates");
        }
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}