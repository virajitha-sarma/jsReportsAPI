package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Getter
@Entity
@Table(name = "report")
public class Report {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	public String reportname;
	public String type;
	
	public Report() {
		
	}
			
	public Report(Long id, String reportname, String type) {
		super();
		this.id = id;
		this.reportname = reportname;
		this.type = type;
	}
}
