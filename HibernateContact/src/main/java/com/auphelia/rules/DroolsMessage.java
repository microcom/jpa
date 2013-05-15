package com.auphelia.rules;

import org.drools.runtime.rule.RuleContext;

public class DroolsMessage {
	
	private String message = "";
	private Severity type;
	private String rule = "";
	private String field = "";
	
	public String getMessage() {
		return message;
	}
	
	public Severity getType() {
		return type;
	}
	
	public String getRule() {
		return rule;
	}
	
	public String getField() {
		return field;
	}
	
	public enum Severity {
		INFO, WARNING, ERROR
	}
	
	private DroolsMessage(Severity type) {
		this.type = type;
	}
	
	public DroolsMessage message(String message) {
	      this.message = message;
	      return this;
	}
	
	public DroolsMessage rule(RuleContext context) {
		this.rule = context.getRule().getNamespace() + ":" + context.getRule().getName();
		return this;
	}
	
	public DroolsMessage field(String field) {
		this.field = field;
		return this;
	}
	
	public static DroolsMessage error() {
		return new DroolsMessage(Severity.ERROR);
	}
	
	public static DroolsMessage warning() {
		return new DroolsMessage(Severity.WARNING);
	}
	
	public static DroolsMessage info() {
		return new DroolsMessage(Severity.INFO);
	}
	
	public Boolean isError() {
		if (this.type == Severity.ERROR) {
			return true;
		}
		
		return false;
	}
	
	public Boolean isWarning() {
		if (this.type == Severity.WARNING) {
			return true;
		}
		
		return false;
	}
	
	public Boolean isInfo() {
		if (this.type == Severity.INFO) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return message;
	}

}
