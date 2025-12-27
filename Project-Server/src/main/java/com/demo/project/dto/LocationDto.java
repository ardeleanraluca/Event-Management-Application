package com.demo.project.dto;

import com.demo.project.entity.LocationEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class LocationDto {

    @NotEmpty
    private Long id;

    @NotEmpty
    private Long noSeats;

    @NotEmpty
    private String address;

    @NotEmpty
    private String name;
    @NotEmpty
    private String county;
    @NotEmpty
    private String city;

    public LocationDto(LocationEntity locationEntity) {
        this.id = locationEntity.getId();
        this.noSeats = locationEntity.getNoSeats();
        this.address = locationEntity.getAddress();
        this.name = locationEntity.getName();
        this.county = locationEntity.getCounty();
        this.city = locationEntity.getCity();
    }

}
