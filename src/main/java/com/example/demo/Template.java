package com.example.demo;

public class Template {
	

	private String content;
	private String id;
	private String script;

	public Template() {}

	public Template(String content, String data, String script)
	{
		this.content = content;
		this.id = data;
		this.script = script;
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
	public String getScript() {
		return script;
	}

}
