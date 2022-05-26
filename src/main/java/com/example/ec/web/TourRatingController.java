package com.example.ec.web;

import com.example.ec.domain.Tour;
import com.example.ec.domain.TourRating;
import com.example.ec.domain.TourRatingPk;
import com.example.ec.model.RatingRequest;
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
    public void createTourRating(@PathVariable(value = "tourId") Integer tourId,
                                 @RequestBody @Validated RatingRequest request) {

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(()-> new NoSuchElementException("Tour does not exist " + tourId));

        tourRatingRespository.save(new TourRating(new TourRatingPk(tour, request.getCustomerId()),
                request.getScore(), request.getComment()));

    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<RatingRequest> getAllRatings(@PathVariable(value = "tourId") Integer tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(()-> new NoSuchElementException("Tour does not exist " + tourId));

        List<RatingRequest> ratingList = tourRatingRespository.findByPkTourId(tourId)
                .stream().map(RatingRequest::new).collect(Collectors.toList());

        return ratingList;
    }

    @GetMapping(path = "/average")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public Map<String, OptionalDouble> getAverageRating(@PathVariable(value = "tourId") Integer tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(()-> new NoSuchElementException("Tour does not exist " + tourId + ", so can't average"));

        Map<String, OptionalDouble> resultMap = Map.of("average rating", tourRatingRespository.findByPkTourId(tourId)
        .stream().mapToInt(TourRating::getScore).average());

        return resultMap;
    }

    @PutMapping
    public RatingRequest updateWithPut(@PathVariable(value = "tourId") Integer tourId,
                                        @RequestBody @Validated RatingRequest ratingRequest) {
        TourRating rating =tourRatingRespository.findByPkTourIdAndPkCustomerId(tourId, ratingRequest.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException("Tour rating pair for request( "
                + tourId + " for customer" + ratingRequest.getCustomerId()));

    rating.setScore(ratingRequest.getScore());
    rating.setComment(ratingRequest.getComment());
    return new RatingRequest(tourRatingRespository.save(rating));
    }

    @PatchMapping
    public RatingRequest updateWithPatch(@PathVariable(value = "tourId") Integer tourId,
                                       @RequestBody @Validated RatingRequest ratingRequest) {
        TourRating rating =tourRatingRespository.findByPkTourIdAndPkCustomerId(tourId, ratingRequest.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException("Tour rating pair for request( "
                        + tourId + " for customer" + ratingRequest.getCustomerId()));

        if(ratingRequest.getScore()!=null)
        rating.setScore(ratingRequest.getScore());

        if(ratingRequest.getComment()!=null)
        rating.setComment(ratingRequest.getComment());

        return new RatingRequest(tourRatingRespository.save(rating));
    }

    @DeleteMapping(path = "/{customerId}")
    public void deleteByCustomerId(@PathVariable(value = "tourId") int tourId,
                                   @PathVariable (value = "customerId") int customerId){

        TourRating rating =tourRatingRespository.findByPkTourIdAndPkCustomerId(tourId, customerId)
                .orElseThrow(() -> new NoSuchElementException("Tour rating pair for request( "
                        + tourId + " for customer" + customerId));
        tourRatingRespository.delete(rating);
    }

    //using page and sort in url---> http://localhost:8088/tours/5/ratings/page?size=4&page=0&sort=score,desc
    //remember to include the additional method in repository with pageable as argument
    @GetMapping(path = "/page")
    public Page<RatingRequest> getAllRatingsForTourWithPage(@PathVariable(value = "tourId") Integer tourId,
                                                            Pageable pageable){
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(()-> new NoSuchElementException("Tour does not exist " + tourId));
        Page<TourRating> ratings = tourRatingRespository.findByPkTourId(tourId, pageable);
        return new PageImpl<>(
                ratings.get().map(RatingRequest::new).collect(Collectors.toList()),
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
