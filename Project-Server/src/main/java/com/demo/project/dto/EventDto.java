package com.demo.project.dto;

import com.demo.project.entity.EventEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class EventDto {

    private Long id;

    @NotEmpty
    private String name;

    private String description;
    @NotEmpty
    private String category;
    @NotEmpty
    private String date;
    private String hour;
    private Long availableTickets;

    private Long soldTickets;

    private Long organizerId;
    private LocationDto location;
    private ImageDto image;

    private List<TicketDto> tickets;

    public EventDto(EventEntity eventEntity) {
        this.id = eventEntity.getId();
        this.name = eventEntity.getName();
        this.description = eventEntity.getDescription();
        this.category = eventEntity.getCategory();
        this.date = eventEntity.getDate();
        this.hour = eventEntity.getHour();
        this.soldTickets = eventEntity.getSoldTickets();
        this.availableTickets = eventEntity.getAvailableTickets();
        this.organizerId = eventEntity.getOrganizer().getId();
        this.location = new LocationDto(eventEntity.getLocation());
        this.tickets = eventEntity.getTickets().stream().map(TicketDto::new).collect(Collectors.toList());
        this.image = new ImageDto(eventEntity.getImage());

    }

}
