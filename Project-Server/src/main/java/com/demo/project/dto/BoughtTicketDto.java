package com.demo.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class BoughtTicketDto {
    private Long id;

    private Long typedTicketId;

    private Long userId;

}
