package com.example.demo.service;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import com.example.demo.model.LogDetail;
import com.example.demo.model.LogSummaryResult;
import com.example.demo.model.ReportTemplate;

@Service
public interface ReportService {
	
	List<LogSummaryResult> getLogSummary(String groupBy, int limit);
	List<LogDetail> getLogData(String groupBy, int limit);
	InputStreamResource generateJsReport(String pdfType, List<?> data, int templateId);
	ReportTemplate getTemplate(long templateId);
}
