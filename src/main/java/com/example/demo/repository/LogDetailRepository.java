package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.LogDetail;

@Repository
public interface LogDetailRepository extends CrudRepository<LogDetail, Long>, PagingAndSortingRepository<LogDetail, Long> {

}
