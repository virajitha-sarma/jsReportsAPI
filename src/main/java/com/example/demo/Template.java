package com.example.demo;

public class Template {
	

	private String content;
	private String id;

	public Template(String content, String data)
	{
		this.content = content;
		this.id = data;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getData() {
		return id;
	}
	public void setData(String data) {
		this.id = data;
	}
}
