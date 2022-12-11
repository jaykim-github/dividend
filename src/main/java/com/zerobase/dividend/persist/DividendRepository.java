package com.zerobase.dividend.persist;

import com.zerobase.dividend.persist.entity.DividendEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {

    List<DividendEntity> findAllByCompanyId(Long companyId);

    boolean existsByCompanyIdAndDate(Long companyId,
        LocalDateTime date); // 복합 유니크 키로 인덱스를 걸어서 빨라짐..
}
