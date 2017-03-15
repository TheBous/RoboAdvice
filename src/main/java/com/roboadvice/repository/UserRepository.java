package com.roboadvice.repository;

import com.roboadvice.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
@Transactional
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByEmail(String email);

    User findById(long id);

    User findByEmailAndPassword(String email, String password);

}
