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
@Table(name = "logdetail")
public class LogDetail {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="dataclass")
	public String dataClass;
	
	@Column(name="dataohost")
	public String dataoHost;
	
	@Column(name="dataclasstype")
	public String dataClassType;
	
	@Column(columnDefinition = "TEXT", name="dataevent")
	public String dataEvent;
	
	@Column(name="datalogsourcetype")
	public String dataLogSourceType;
	
	@Column(name="dataientity")
	public String dataiEntity;
	
	@Column(name="datadomainimpacted")
	public String dataDomainImpacted;
	
	@Column(name="dataocountry")
	public String dataoCountry;
	
	@Column(name="dataoip")
	public String dataoIP;
	
	public LogDetail() {
		
	}
	
	public LogDetail(Long id,String dataClass, String dataoHost, String dataClassType, String dataEvent, String dataLogSourceType, String dataiEntity,
			String dataDomainImpacted, String dataoCountry, String dataoIP) {
		super();
		this.id = id;
		this.dataClass = dataClass;
		this.dataoHost = dataoHost;
		this.dataClassType = dataClassType;
		this.dataEvent = dataEvent;
		this.dataLogSourceType = dataLogSourceType;
		this.dataiEntity = dataiEntity;
		this.dataDomainImpacted = dataDomainImpacted;
		this.dataoCountry = dataoCountry;
		this.dataoIP = dataoIP;
	}
}
