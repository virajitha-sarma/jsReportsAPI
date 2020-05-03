package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Getter
@Entity
@Table(name = "reporttemplate")
public class ReportTemplate {	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	@Column(columnDefinition = "TEXT")
	public String content;
	public String recipe;
	public String engine;
	public String name;

	@Column(columnDefinition = "TEXT")
	public String script;
	
	public ReportTemplate() {
		
	}
			
	public ReportTemplate(Long id, String content, String recipe, String engine, String name, String script) {
		super();
		this.id = id;
		this.content = content;
		this.recipe = recipe;
		this.engine = engine;
		this.name = name;
		this.script = script;
	}
}
