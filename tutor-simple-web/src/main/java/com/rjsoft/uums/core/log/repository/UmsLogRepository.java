package com.rjsoft.uums.core.log.repository;

import com.rjsoft.common.repository.CustomRepository;
import com.rjsoft.uums.facade.log.entity.UmsLog;
import org.springframework.stereotype.Repository;

@Repository
public interface UmsLogRepository extends CustomRepository<UmsLog, String> {
}
