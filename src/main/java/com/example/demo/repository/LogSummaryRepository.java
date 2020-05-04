package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.LogSummary;
import com.example.demo.model.LogSummaryResult;

@Repository
public interface LogSummaryRepository extends CrudRepository<LogSummary, Long>, PagingAndSortingRepository<LogSummary, Long> {

	@Query(value = "select new com.example.demo.model.LogSummaryResult(l.dataoHost , count(l.id)) from LogSummary l group by dataoHost")
	public List<LogSummaryResult> findAndGroupBy();

	@Query("select new com.example.demo.model.LogSummaryResult(l.dataoHost , count(l.id)) from LogSummary l group by dataoHost")
	public List<LogSummaryResult> findAndGroupBy(Pageable page);
}
