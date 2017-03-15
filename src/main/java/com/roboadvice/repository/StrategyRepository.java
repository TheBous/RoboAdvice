package com.roboadvice.repository;

import com.roboadvice.model.Strategy;
import com.roboadvice.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface StrategyRepository extends PagingAndSortingRepository<Strategy, Long> {

    List<Strategy> findByUserAndDate(User u, LocalDate date);

    List<Strategy> findByUserAndActive(User u, boolean active);

    @Modifying
    @Query ("UPDATE Strategy s SET s.active=0 WHERE s.active=1 AND s.user=?1")
    void setInactive(User u);

    @Modifying
    @Query("DELETE FROM Strategy s where s.user = ?1 and s.active=1 and date=?2")
    int deleteActiveStrategyByUserAndDate(User u, LocalDate date);

    @Query("select s from Strategy s where s.user=?1 and s.date=(select max(s.date) from s where s.user=?1 and s.active=0) order by s.id desc")
    List<Strategy> findLatestInactiveStrategy(User u, Pageable pageable);

    @Query("SELECT s FROM Strategy s WHERE s.user=?1 AND s.date<(SELECT s.date FROM Strategy s WHERE s.user=?1 AND s.active=1 GROUP BY s.date) ORDER BY s.date DESC , s.id DESC")
    List<Strategy> findLastUsedStrategy(User u, Pageable pageable);

    @Query("SELECT s FROM Strategy s WHERE s.user=?1")
    List<Strategy> fullHistoryByUser(User u);






    @Query("SELECT s FROM Strategy s WHERE s.active = 1 AND s.date=?1")
    List<Strategy> findNewStrategies(LocalDate yesterday);

    @Query("SELECT s FROM Strategy s WHERE s.user=?1 AND s.date<>?2 AND s.active=0")
    List<Strategy> findUserOldStrategies(User u, LocalDate date);







}
