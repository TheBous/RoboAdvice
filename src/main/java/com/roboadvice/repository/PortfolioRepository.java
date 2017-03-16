package com.roboadvice.repository;

import com.roboadvice.dto.PortfolioDTO;
import com.roboadvice.model.Portfolio;
import com.roboadvice.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface PortfolioRepository extends PagingAndSortingRepository<Portfolio, Long> {

    List<Portfolio> findByDate(LocalDate date);

    List<Portfolio> findByUserAndDate(User u, LocalDate date);

    @Query("SELECT NEW com.roboadvice.model.Portfolio(p.assetsClass, sum(p.value), p.date) FROM Portfolio p WHERE p.user=?1 and p.date=(SELECT MAX(p.date) FROM p WHERE p.user=?1) GROUP BY p.assetsClass")
    List<Portfolio> getCurrent(User u);

    @Query(value = "SELECT * FROM portfolio WHERE user_id=?1 GROUP BY assets_class_id, date ORDER BY date, assets_class_id", nativeQuery = true)
    List<Portfolio> fullHistoryByUser(User u);

    @Query("SELECT p FROM Portfolio p WHERE p.date=?1 AND p.user NOT IN (SELECT s.user FROM Strategy s  WHERE s.active=1 AND s.date=?1 GROUP BY s.user)")
    List<Portfolio> findAllPortfoliosToBeUpdatedByDate(LocalDate date);

    @Query("SELECT p.user FROM Portfolio p WHERE p.date=?1 GROUP BY p.user")
    List<User> findAllPortfoliosUsersByDate(LocalDate date);

    @Query(value = "SELECT * FROM portfolio WHERE user_id=?1 AND date BETWEEN ?2 and ?3 GROUP BY assets_class_id, date ORDER BY date, assets_class_id", nativeQuery = true)
    List<Portfolio> historyByUserAndDates(User u, String from, String to);



}
