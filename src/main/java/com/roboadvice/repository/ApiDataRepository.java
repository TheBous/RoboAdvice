package com.roboadvice.repository;

import com.roboadvice.model.ApiData;
import com.roboadvice.model.Assets;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface ApiDataRepository extends PagingAndSortingRepository<ApiData, Long> {

    ApiData findByAssetsIdAndDate(long assetId, LocalDate date);

    @Query(value = "SELECT * FROM api_data WHERE date = (SELECT MAX(date) FROM api_data WHERE assets_id = ?1) AND assets_id = ?1", nativeQuery = true)
    ApiData findLatestValueByAsset(long assets_id);

    ApiData findTopByAssetsAndDateLessThanEqualOrderByDateDesc(Assets asset, LocalDate date);

    @Query(value = "SELECT id, MAX(date) AS date, value, assets_id FROM api_data WHERE date<=?1 GROUP BY assets_id DESC", nativeQuery = true)
    List<ApiData> findLatestApiValuesByDate(String date);



}
