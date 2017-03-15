package com.roboadvice.repository;

import com.roboadvice.model.AssetsClass;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
@Transactional
public interface AssetsClassRepository extends PagingAndSortingRepository<AssetsClass, Long> {

    AssetsClass findById(long id);


}
