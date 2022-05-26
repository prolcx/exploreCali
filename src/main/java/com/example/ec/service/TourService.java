package com.example.ec.service;

import com.example.ec.domain.Difficulties;
import com.example.ec.domain.Region;
import com.example.ec.domain.Tour;
import com.example.ec.domain.TourPackage;
import com.example.ec.repo.TourPackageRepository;
import com.example.ec.repo.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourService {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private TourPackageRepository tourPackageRepository;

    public Tour createTour(String title, String description, String blurb, Integer price,
                           String duration, String bullets, String keywords, String tourPackageName,
                           Difficulties difficulties, Region region) {
        TourPackage tourPackage = tourPackageRepository.findByName(tourPackageName)
                .orElseThrow(()-> new RuntimeException("Tour Package does not exist: " + tourPackageName));

        return tourRepository.save(new Tour(title, description, blurb, price, duration, bullets, keywords,
                tourPackage, difficulties, region));
    }

    public long countTotalTour () {
        return tourRepository.count();
    }
}
