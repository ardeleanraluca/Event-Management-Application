package com.demo.project.dto;

import com.demo.project.entity.TicketEntity;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TicketDto {

    private Long id;
    private Double price;

    private Double discount;

    public TicketDto(TicketEntity ticketEntity) {
        this.id = ticketEntity.getId();
        this.price = ticketEntity.getPrice();
        this.discount = ticketEntity.getDiscount();
    }


}
