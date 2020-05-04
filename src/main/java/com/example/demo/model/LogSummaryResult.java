package com.example.demo.model;

public class LogSummaryResult {

	String host;
	long logcount;
	
	public LogSummaryResult() {
		
	}
	
	public LogSummaryResult(String host, long logcount) {
		this.host = host;
		this.logcount = logcount;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public long getLogcount() {
		return logcount;
	}

	public void setLogcount(long logcount) {
		this.logcount = logcount;
	}
}
