package com.example.ec;

import com.example.ec.model.TourFromFile;
import com.example.ec.service.TourPackageService;
import com.example.ec.service.TourService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

@SpringBootApplication
public class ExplorecaliApplication implements CommandLineRunner {

	@Value("${importJsonFile}")
	private String importFile;

	@Autowired
	private TourService tourService;

	@Autowired
	private TourPackageService tourPackageService;

	public static void main(String[] args) {
		SpringApplication.run(ExplorecaliApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		createTourPackage();
		long numberOfTourPackages = tourPackageService.countTotalTourPackages();
		createTour(importFile);
		long numberOfTours = tourService.countTotalTour();

	}

	private void createTourPackage() {
		tourPackageService.findOrCreateTourPackage("BC", "Backpack Cal");
		tourPackageService.findOrCreateTourPackage("CC", "California Calm");
		tourPackageService.findOrCreateTourPackage("CH", "California Hot springs");
		tourPackageService.findOrCreateTourPackage("CY", "Cycle California");
		tourPackageService.findOrCreateTourPackage("DS", "From Desert to Sea");
		tourPackageService.findOrCreateTourPackage("KC", "Kids California");
		tourPackageService.findOrCreateTourPackage("NW", "Nature Watch");
		tourPackageService.findOrCreateTourPackage("SC", "Snowboard Cali");
		tourPackageService.findOrCreateTourPackage("TC", "Taste of California");

	}

	private void createTour(String importFile) throws IOException {
		List<TourFromFile> toursFromFile = new ObjectMapper().setVisibility(FIELD, JsonAutoDetect.Visibility.ANY)
				.readValue(new FileInputStream(importFile), new TypeReference<List<TourFromFile>>() {});

		toursFromFile.forEach(
				arrayChild -> tourService.createTour(
						arrayChild.getTitle(), arrayChild.getDescription(), arrayChild.getBlurb(), arrayChild.getPrice(),
						arrayChild.getLength(), arrayChild.getBullets(), arrayChild.getKeywords(), arrayChild.getPackageType(),
						arrayChild.getDifficulty(), arrayChild.getRegion()
				)
		);
	}

}
