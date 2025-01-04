package org.example.uberprojectlocationservice.services;

import org.example.uberprojectlocationservice.dto.DriverLocationDto;

import java.util.List;

public interface LocationServiceInterface {

    Boolean saveDriverLocation(String driverId, Double latitude, Double Longitude);

    List<DriverLocationDto> getNearbyDrivers(Double latitude, Double Longitude);

}
