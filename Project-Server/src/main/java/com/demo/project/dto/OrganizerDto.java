package com.demo.project.dto;

import com.demo.project.entity.OrganizerEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class OrganizerDto extends UserAccountDto {
    private List<EventDto> events;
    private Long id;

    public OrganizerDto(OrganizerEntity organizerEntity) {
        super(organizerEntity.getUserAccountEntity());
        this.id = organizerEntity.getId();
        this.events = organizerEntity.getEvents().stream().map(EventDto::new).toList();
    }
}
