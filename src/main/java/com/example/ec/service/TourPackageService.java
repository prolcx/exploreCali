package com.example.ec.service;

import com.example.ec.domain.TourPackage;
import com.example.ec.repo.TourPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourPackageService {

    @Autowired
    private TourPackageRepository tourPackageRepository;

    public Iterable<TourPackage> getAllTourPackage() {
        return tourPackageRepository.findAll();
    }

    public TourPackage findOrCreateTourPackage(String code, String name) {
        return tourPackageRepository.findById(code)
                .orElse(tourPackageRepository.save(new TourPackage(code, name)));
    }

    public long countTotalTourPackages() {
        return tourPackageRepository.count();
    }

}
