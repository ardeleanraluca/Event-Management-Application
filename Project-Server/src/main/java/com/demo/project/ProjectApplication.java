package com.demo.project;

import com.demo.project.service.LocationSeviceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectApplication {
    @Autowired
    LocationSeviceInterface locationService;
    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);

    }


}
