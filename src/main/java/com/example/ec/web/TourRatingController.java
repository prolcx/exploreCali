package com.example.ec.web;

import com.example.ec.domain.Tour;
import com.example.ec.domain.TourRating;

import com.example.ec.repo.TourRatingRespository;
import com.example.ec.repo.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path= "/tours/{tourId}/ratings")
public class TourRatingController {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private TourRatingRespository tourRatingRespository;

    //4 types of way to pass value in request: pathvariable, params, requestbody & requestheader

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourRating(@PathVariable(value = "tourId") String tourId,
                                 @RequestBody @Validated TourRating request) {

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(()-> new NoSuchElementException("Tour does not exist " + tourId));

        tourRatingRespository.save(new TourRating(tourId, request.getCustomerId(), request.getScore(),
                                                    request.getComment()));

    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Page<TourRating> getAllRatings(@PathVariable(value = "tourId") String tourId,
                                          Pageable pageable) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(()-> new NoSuchElementException("Tour does not exist " + tourId));

        return tourRatingRespository.findByTourId(tourId, pageable);
    }

    @GetMapping(path = "/average")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public Map<String, Double> getAverageRating(@PathVariable(value = "tourId") String tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(()-> new NoSuchElementException("Tour does not exist " + tourId + ", so can't average"));

        Map<String, Double> resultMap = Map.of("average rating", tourRatingRespository.findByTourId(tourId)
        .stream().mapToInt(TourRating::getScore).average()
        .orElseThrow(()->
                new NoSuchElementException("Tour has no Ratings!!!")));

        return resultMap;
    }

    @PutMapping
    public TourRating updateWithPut(@PathVariable(value = "tourId") String tourId,
                                        @RequestBody @Validated TourRating ratingRequest) {
        TourRating rating =tourRatingRespository.findByTourIdAndCustomerId(tourId, ratingRequest.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException("Tour rating pair for request( "
                + tourId + " for customer" + ratingRequest.getCustomerId()));

    rating.setScore(ratingRequest.getScore());
    rating.setComment(ratingRequest.getComment());
    return tourRatingRespository.save(rating);
    }

    @PatchMapping
    public TourRating updateWithPatch(@PathVariable(value = "tourId") String tourId,
                                       @RequestBody @Validated TourRating ratingRequest) {
        TourRating rating =tourRatingRespository.findByTourIdAndCustomerId(tourId, ratingRequest.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException("Tour rating pair for request( "
                        + tourId + " for customer" + ratingRequest.getCustomerId()));

        if(ratingRequest.getScore()!=null)
        rating.setScore(ratingRequest.getScore());

        if(ratingRequest.getComment()!=null)
        rating.setComment(ratingRequest.getComment());

        return tourRatingRespository.save(rating);
    }

    @DeleteMapping(path = "/{customerId}")
    public void deleteByCustomerId(@PathVariable(value = "tourId") String tourId,
                                   @PathVariable (value = "customerId") int customerId){

        TourRating rating =tourRatingRespository.findByTourIdAndCustomerId(tourId, customerId)
                .orElseThrow(() -> new NoSuchElementException("Tour rating pair for request( "
                        + tourId + " for customer" + customerId));
        tourRatingRespository.delete(rating);
    }

    //using page and sort in url---> http://localhost:8088/tours/5/ratings/page?size=4&page=0&sort=score,desc
    //remember to include the additional method in repository with pageable as argument
    @GetMapping(path = "/page")
    public Page<TourRating> getAllRatingsForTourWithPage(@PathVariable(value = "tourId") String tourId,
                                                            Pageable pageable){
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(()-> new NoSuchElementException("Tour does not exist " + tourId));
        Page<TourRating> ratings = tourRatingRespository.findByTourId(tourId, pageable);
        return new PageImpl<>(
                ratings.get().map(TourRating::new).collect(Collectors.toList()),
                pageable,
                ratings.getTotalElements()
        );
    }




    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex){
        return ex.getMessage();
    }

}
