package com.roboadvice.repository;

import com.roboadvice.model.Assets;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
@Transactional
public interface AssetsRepository extends PagingAndSortingRepository<Assets, Long> {

    Assets findById(long id);

}
