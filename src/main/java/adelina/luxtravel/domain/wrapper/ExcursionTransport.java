package adelina.luxtravel.domain.wrapper;

import adelina.luxtravel.domain.Excursion;
import adelina.luxtravel.domain.transport.Transport;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Represents an excursion and one of the transports for it
 */
@Entity
@Table(name = "excursions_transport")
@IdClass(ExcursionTransportId.class)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ExcursionTransport {
    @Id
    @ManyToOne
    @JoinColumn(name = "excursion_id")
    @JsonBackReference(value = "excursion")
    private Excursion excursion;
    @Id
    @ManyToOne
    @JoinColumn(name = "transport_id")
    @Setter
    private Transport transport;
    @Column(name = "price")
    private double price;
}