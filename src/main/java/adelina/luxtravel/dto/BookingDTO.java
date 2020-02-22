package adelina.luxtravel.dto;


import adelina.luxtravel.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookingDTO {
       private long id;
       private int reservedTicketsCount;
       private TravelingDataDTO travelingDataDTO;
       @JsonBackReference

       private User user;

       public BookingDTO(int reservedTicketsCount, TravelingDataDTO travelingDataDTO, User user) {
              this.reservedTicketsCount = reservedTicketsCount;
              this.travelingDataDTO = travelingDataDTO;
              this.user = user;
       }
}
