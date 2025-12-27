package com.demo.project.repository;

import com.demo.project.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    LocationEntity findByNameAndCountyAndCity(String name,String county,String city);

    List<LocationEntity> findAllByCountyAndCityOrderByName(String county,String city);
}
