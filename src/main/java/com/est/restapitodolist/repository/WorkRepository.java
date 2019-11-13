package com.est.restapitodolist.repository;

import com.est.restapitodolist.model.Work;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends PagingAndSortingRepository<Work, Long> {
}
