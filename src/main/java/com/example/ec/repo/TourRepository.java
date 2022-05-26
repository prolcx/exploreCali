package com.example.ec.repo;

import com.example.ec.domain.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

//Rest Resource is to override the Spring data api endpoint with the collectionResourceRel name

public interface TourRepository extends PagingAndSortingRepository<Tour, String>, CrudRepository<Tour, String> {
//    List<Tour> findByTourPackageCode(@Param("code")String code);
    Page<Tour> findByTourPackageCode(@Param("code")String code, Pageable pageable);

    @Query(value = "{'tourPackageCode' : ?0}",
            fields = "{'id' : 1, 'title':1, 'tourPackageCode':1, 'tourPackageName':1}")
    Page<Tour> findSummaryByTourPackageCode(@Param("code") String code, Pageable pageable);

    @Override
    @RestResource(exported = false)
    Iterable<Tour> findAll(Sort sort);

    @Override
    @RestResource(exported = false)
    Iterable<Tour> findAll();

    @Override
    @RestResource(exported = false)
    Iterable<Tour> findAllById(Iterable<String> string);

    @Override
    @RestResource(exported = false)
    void deleteById(String string);

    @Override
    @RestResource(exported = false)
    void delete(Tour entity);

    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Tour> entities);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
