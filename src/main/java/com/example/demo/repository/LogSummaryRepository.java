package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.LogSummary;

@Repository
public interface LogSummaryRepository extends CrudRepository<LogSummary, Long>, PagingAndSortingRepository<LogSummary, Long> {

}
