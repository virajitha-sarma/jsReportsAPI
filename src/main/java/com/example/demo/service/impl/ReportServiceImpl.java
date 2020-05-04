package com.example.demo.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Data;
import com.example.demo.model.JSReportPayload;
import com.example.demo.model.LogDetail;
import com.example.demo.model.LogSummaryResult;
import com.example.demo.model.Template;
import com.example.demo.repository.LogDetailRepository;
import com.example.demo.repository.LogSummaryRepository;
import com.example.demo.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	
	@Autowired
	LogSummaryRepository logSummaryRepository;

	@Autowired
	LogDetailRepository logDetailRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	HttpHeaders headers;
	
	@Value("${server.port}")
	String serverPort;
	
	private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

	
	@Override
	public List<LogSummaryResult> getLogSummary(String groupBy, int limit) {

		logger.info("Fetching log summary data grouped by host");

		if(limit> 0) {
			Pageable PageWithNElements = PageRequest.of(0, limit);
			return logSummaryRepository.findAndGroupBy(PageWithNElements);
		} else {
			return logSummaryRepository.findAndGroupBy();
		}

	}

	@Override
	public List<LogDetail> getLogData(String groupBy, int limit) {

		Pageable PageWithNElements = PageRequest.of(0, limit);

		return null;
	}
	
	@Override
	public InputStreamResource generateJsReport(String pdfType, List<?> data, int templateId) {

		Template dbTemplate = getTemplate(templateId);
		JSReportPayload payload = new JSReportPayload();
		Template template = new Template();
		template.setEngine("handlebars");
		template.setRecipe(pdfType);
		template.setContent(dbTemplate.getContent());

		payload.setTemplate(template);
		Data payloadData = new Data();
		payloadData.setMessages(data);
		payload.setData(payloadData);

		HttpEntity<Object> entity = new HttpEntity<>(payload, getHeaders());
		logger.info("Started generating jsReport");
		byte[] response = restTemplate.postForObject("http://localhost:5488/api/report", entity, byte[].class);
		logger.info("Finished generating jsReport");
		InputStream targetStream = new ByteArrayInputStream(response);
		InputStreamResource resource = new InputStreamResource(targetStream);
		return resource;
	}
	
	private HttpHeaders getHeaders() {
		if (null == headers) {
			headers = new HttpHeaders();
			headers.set("User-Agent", "request");
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.ALL));
		}
		return headers;
	}

	private Template getTemplate(int templateId) {
		Template content = restTemplate.getForObject("http://localhost:" + serverPort + "/report/template/" + templateId, Template.class);
		return content;
	}
}
