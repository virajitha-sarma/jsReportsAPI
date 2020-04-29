package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ReportTemplate;

@Repository
public interface ReportTemplateRepository extends CrudRepository<ReportTemplate, Long> {

}
