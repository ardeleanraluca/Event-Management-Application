package com.demo.project.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TicketDetailsDto {

    private Long id;
    private Double price;
    private EventDto eventDto;

}
