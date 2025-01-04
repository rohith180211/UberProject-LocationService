package org.example.uberprojectlocationservice.dto;

import lombok.*;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class saveDriverLocationRequestDto {
    String driverId;
    Double latitude;
    Double longitude;
}
