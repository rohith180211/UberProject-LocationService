package org.example.uberprojectlocationservice.services;

import org.example.uberprojectlocationservice.dto.DriverLocationDto;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
@Service
public class LocationServiceImplementation implements LocationServiceInterface{

    private static final String DRIVER_GEO_OPS_KEY = "drivers";
    private static final Double SEARCH_RADIUS = 5.0;

    private final StringRedisTemplate stringRedisTemplate;



    public LocationServiceImplementation(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Boolean saveDriverLocation(String driverId, Double latitude, Double longitude) {
        GeoOperations<String, String> geoOps = stringRedisTemplate.opsForGeo();
        geoOps.add(
                DRIVER_GEO_OPS_KEY,
                new RedisGeoCommands.GeoLocation<>(
                        driverId,
                        new Point(
                                latitude,longitude)));
        return true;
    }

    @Override
    public List<DriverLocationDto> getNearbyDrivers(Double latitude, Double longitude) {
        GeoOperations<String, String> geoOps = stringRedisTemplate.opsForGeo();
        Distance radius = new Distance(SEARCH_RADIUS, Metrics.KILOMETERS);
        Circle within = new Circle(new Point(longitude, latitude), radius); // Corrected order

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoOps.radius(DRIVER_GEO_OPS_KEY, within);
        List<DriverLocationDto> drivers = new ArrayList<>();
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {
            Point point = geoOps.position(DRIVER_GEO_OPS_KEY, result.getContent().getName()).get(0);
            DriverLocationDto driverLocation = DriverLocationDto.builder()
                    .driverId(result.getContent().getName())
                    .latitude(point.getX())
                    .longitude(point.getY())
                    .build();
            drivers.add(driverLocation);
        }
        return drivers;
    }

}
