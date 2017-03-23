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

    //query usata per inserire utente tramite script
    @Query(value = "SELECT * FROM api_data WHERE assets_id = ?1 AND date = (SELECT MAX(date) FROM api_data WHERE assets_id = ?1 AND date<= ?2)", nativeQuery = true)
    ApiData findLatestValueByAssetAndDate(long assets_id, String date);

    ApiData findTopByAssetsAndDateLessThanEqualOrderByDateDesc(Assets asset, LocalDate date);

    @Query(value = "SELECT id, max(date) as date, value, assets_id FROM (SELECT * FROM api_data WHERE date<=?1 order by date desc) as m group by LOWER(assets_id) order by assets_id", nativeQuery = true)
    List<ApiData> findLatestApiValuesByDate(String date);



}
