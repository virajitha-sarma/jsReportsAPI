package com.example.demo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.LogDetail;
import com.example.demo.model.LogSummary;
import com.example.demo.model.Report;
import com.example.demo.model.ReportTemplate;
import com.example.demo.repository.LogDetailRepository;
import com.example.demo.repository.LogSummaryRepository;
import com.example.demo.repository.ReportRepository;
import com.example.demo.repository.ReportTemplateRepository;
import com.google.gson.Gson;

@RestController
public class ReportController {

	@Autowired
	RestTemplate template;

	@Autowired
	LogDetailRepository logDetailRepository;

	@Autowired
	LogSummaryRepository logSummaryRepository;

	@Autowired
	ReportRepository reportRepository;

	@Autowired
	ReportTemplateRepository reportTemplateRepository;

	@Autowired
	Gson gson;

	HttpHeaders headers;
	
	@Value("${server.port}")
	String serverPort;
	
	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	/**
	 * Renders reports in PDF format
	 * 
	 * @param templateType: templateType can be SIEM report's log summary or log
	 *                      detail template types.
	 * @param pdfType:      Type of JSReport engine to be used for rendering reports
	 * @param limit:        number of records to fetch from database
	 * @return returns pdf file
	 */
	@GetMapping("/reportPDF/{templateType}")
	public ResponseEntity<InputStreamResource> reportPDF(@PathVariable("templateType") String templateType,
			@RequestParam String pdfType, @RequestParam String limit) {

		String data = getDataForReportTemplate(templateType, limit);
		InputStreamResource resource = jsReport(data, pdfType);
		HttpHeaders responseHeaders = getResponseHeaders();
		responseHeaders.add("Content-Disposition", "filename=report.pdf");
		responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		ResponseEntity<InputStreamResource> responsepdf = ResponseEntity.ok().headers(responseHeaders).body(resource);
		return responsepdf;

	}

	/**
	 * Renders reports in HTML format
	 * 
	 * @param templateType: templateType can be SEIM report's log summary or log
	 *                      detail template types.
	 * @param limit:        number of records to fetch from database
	 * @return returns HTML file
	 */
	@GetMapping("/reportHTML/{templateType}")
	public ResponseEntity<InputStreamResource> reportHTML(@PathVariable("templateType") String templateType,
			@RequestParam String limit) {

		String data = getDataForReportTemplate(templateType, limit);
		InputStreamResource resource = jsReport(data, "html");

		HttpHeaders responseHeaders = getResponseHeaders();
		responseHeaders.add("Content-Disposition", "filename=report.html");
		responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		ResponseEntity<InputStreamResource> responseHTML = new ResponseEntity<InputStreamResource>(resource,
				responseHeaders, HttpStatus.OK);

		return responseHTML;

	}

	@GetMapping("/reportList")
	public ResponseEntity<String> reportsList() {
		String jsonReportList = template.getForObject("http://localhost:" + serverPort + "/report", String.class);
		return ResponseEntity.ok().headers(getResponseHeaders()).body(jsonReportList);

	}

	/**
	 * JSreport API takes template in HTML format as part of its post request. For
	 * testing purposes an existing template from SIEM lR is stored as HTML in
	 * database. Gets HTML template from database. "Rendering LR report templates
	 * with JSReports" confluence page refers to the LR template
	 * "TestReporting-SIEMLR-LogSummaryTemplate.pdf" used for this POC.
	 * 
	 * @return Returns HTML template
	 */
	private String getContentforTemplate() {
		String content = template.getForObject("http://localhost:" + serverPort + "/report/template/1", String.class);
		Template template = gson.fromJson(content, Template.class);
		return template.getContent();
	}

	/**
	 * Renders report for a particular template and data
	 * 
	 * @param data         Data that fills the report template
	 * @param reportRecipe The type of rendering recipe to be used by the jsreports
	 * @return rendered report
	 */
	private InputStreamResource jsReport(String data, String reportRecipe) {
		String content = getContentforTemplate();

		String jsonString = "{\"template\": {" + "\"content\" :\"" + content + "\"," + "\"recipe\": \"" + reportRecipe
				+ "\"," + "\"engine\": \"handlebars\"" + "}," + "\"data\" : " + data + "}";
		// Request to jsReport engine is made to render reports
		HttpEntity<Object> entity = new HttpEntity<>(jsonString, getHeaders());
		byte[] response = template.postForObject("http://localhost:5488/api/report", entity, byte[].class);
		InputStream targetStream = new ByteArrayInputStream(response);
		InputStreamResource resource = new InputStreamResource(targetStream);
		return resource;
	}

	/**
	 * Data is fetched depending on the template type and limit. To fetch data,
	 * request is made to another service that reads data from database. "Rendering
	 * LR report templates with JSReports" confluence page refers to the LR template
	 * and the data "TestReporting-SIEMLR-LogSummaryTemplate.pdf" used for this POC.
	 *
	 * @param templateType: Template type of either "log summary" and "log detail"
	 * @param limit:        Number of records to fetch
	 * @return returns data in JSON format
	 */
	private String getDataForReportTemplate(String templateType, String limit) {
		String jsonData = "";
		if ("LS".equals(templateType)) {
			jsonData = template.getForObject("http://localhost:" + serverPort + "/report/log-summary/data?limit=" + limit,
					String.class);
		} else {
			jsonData = template.getForObject("http://localhost:" + serverPort + "/report/log-detail/data?limit=" + limit,
					String.class);

		}
		jsonData = "{ \"name\": \"Reports\", \"messages\": " + jsonData + "}";
		return jsonData;
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

	private HttpHeaders getResponseHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Methods", "GET, POST");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-cache, no-store");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		return headers;
	}

	/**
	 * Get LogDetail logs data from database
	 * 
	 * @param limit: number of records to fetch from database
	 * @return returns log detail data according to the limit provided
	 */
	@GetMapping("/report/log-detail/data")
	public List<LogDetail> getLogData(@RequestParam int limit) {
		logger.info("Executing getLogData() with limit set to {}", limit);
		if (limit > 0) {
			Pageable PageWithNElements = PageRequest.of(0, limit);
			return logDetailRepository.findAll(PageWithNElements).getContent();

		} else {
			Iterable<LogDetail> myObjects = logDetailRepository.findAll();
			List<LogDetail> target = new ArrayList<>();
			myObjects.forEach(target::add);
			return target;
		}
	}

	/**
	 * Get LogSummary logs data from database
	 * 
	 * @param limit: number of records to fetch from database
	 * @return returns log summary data according to the limit provided
	 */
	@GetMapping("/report/log-summary/data")
	public List<LogSummary> getLogSummary(@RequestParam(required = false) int limit) {
		logger.info("Executing getLogSummary() with limit set to {}", limit);
		if (limit > 0) {
			Pageable PageWithNElements = PageRequest.of(0, limit);
			return logSummaryRepository.findAll(PageWithNElements).getContent();

		} else {
			Iterable<LogSummary> myObjects = logSummaryRepository.findAll();
			List<LogSummary> target = new ArrayList<>();
			myObjects.forEach(target::add);
			return target;
		}
	}

	/**
	 * Get List of reports
	 * 
	 * @return returns list of all reports in database
	 */
	@GetMapping("/report")
	public Iterable<Report> getReportList() {
		logger.info("Executing getReportList()");
		return reportRepository.findAll();
	}

	/**
	 * Get Report Template by Id
	 * 
	 * @param id: id of template for which report need to be fetched
	 * @return returns the data of report template for which id is provided
	 */
	@GetMapping("/report/template/{id}")
	public ReportTemplate getReportTemplateById(@PathVariable("id") Long id) {
		logger.info("Executing getReportTemplateById() for template id = {}" , id );
		Optional<ReportTemplate> entity = reportTemplateRepository.findById(id);
		return entity.get();
	}
}
