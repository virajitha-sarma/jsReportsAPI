package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Report;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {

}
