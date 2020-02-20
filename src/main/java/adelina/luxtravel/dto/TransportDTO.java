package adelina.luxtravel.dto;


import adelina.luxtravel.domain.transport.Airplane;
import adelina.luxtravel.domain.transport.Bus;
import adelina.luxtravel.domain.transport.TransportClass;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({@JsonSubTypes.Type(value = AirplaneDTO.class, name = "Airplane"),
        @JsonSubTypes.Type(value = BusDTO.class, name = "Bus")})
@NoArgsConstructor
@Getter
public abstract class TransportDTO {
    private long id;
    private TransportClass transportClass;

    public TransportDTO(long id, TransportClass transportClass) {
        this.id = id;
        this.transportClass = transportClass;
    }

    public TransportDTO(TransportClass transportClass) {
        this.transportClass = transportClass;
    }
}
