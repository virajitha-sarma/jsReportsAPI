package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public class Template {

	List<Script> scripts = new ArrayList<Script>();
	String content;
	String engine;
	String recipe;
	
	public Template() {}
	
	public List<Script> getScripts() {
		return scripts;
	}
	public void setScripts(List<Script> scripts) {
		this.scripts = scripts;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	public String getRecipe() {
		return recipe;
	}
	public void setRecipe(String recipe) {
		this.recipe = recipe;
	}

}
