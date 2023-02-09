package ru.miihe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.miihe.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByProductName(String productName);

    boolean existsByPeriod(String period);
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE report r SET r.sales = r.sales + ?1 WHERE r.product_name = ?2 AND r.period = ?3")
    void update(Long salesNumber, String productName, String period);
}
