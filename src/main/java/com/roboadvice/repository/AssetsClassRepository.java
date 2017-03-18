package com.roboadvice.repository;

import com.roboadvice.model.AssetsClass;
import com.roboadvice.model.Portfolio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;


@Repository
@Transactional
public interface AssetsClassRepository extends PagingAndSortingRepository<AssetsClass, Long> {

    AssetsClass findById(long id);

    @Query("SELECT NEW com.roboadvice.model.Portfolio(a.assetsClass, sum(api.value), api.date) FROM ApiData api, AssetsClass ac, Assets a WHERE api.assets=a AND a.assetsClass=ac AND api.date BETWEEN ?1 AND ?2 group by ac, api.date order by api.date asc, ac ASC")
    List<Portfolio> getHistoryByDate(LocalDate from, LocalDate to);


}
