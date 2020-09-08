/*
 * Author Christian Gausepohl
 * License: CC0 (no copyright if possible, otherwise fallback to public domain)
 * https://github.com/cgausepohl/gcli
 */
package com.cg.cli;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Parameter {

	private final String name, abbreviation;
	private String argument, default_, description;
	private final boolean isMandatory, expectsArguments;
	private boolean hideValue = false, parsed = false, treatArgumentAsList = false;
	private Long min, max;
	private char listSeparator = ',';
	private ArrayList<String> validValues = new ArrayList<>();

	Parameter(String parameterLong, String parameterShort, boolean isMandatory, boolean expectsArguments) {
		this.name = parameterLong;
		this.isMandatory = isMandatory;
		this.expectsArguments = expectsArguments;
		this.abbreviation = parameterShort;
	}

	public Parameter setMinValue(long min) {
		this.min = min;
		return this;
	}
	
	public Parameter setValidValues(String... validValues) {
		for (String value: validValues)
			this.validValues.add(value);
		return this;
	}
	
	public ArrayList<String> getValidValues() {
		return validValues;
	}

	public Parameter treatArgumentAsList() {
		this.treatArgumentAsList = true;
		return this;
	}

	public boolean isArgumentList() {
		return treatArgumentAsList;
	}

	public Parameter setMaxValue(long max) {
		this.max = max;
		return this;
	}

	public Long getMinValue() {
		return min;
	}

	public Long getMaxValue() {
		return max;
	}

	public boolean isFlag() {
		if (expectsArguments == false && isMandatory == false)
			return true;
		return false;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Parameter setDescription(String description) {
		this.description = description;
		return this;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setArgument(String argument) {
		this.argument = argument;
	}

	public String getArgument() {
		if (!expectsArguments)
			throw new IllegalStateException("A flag cannot have an argument(" + getName() + ")");
		if (argument == null || "".equals(argument))
			return default_;
		if (treatArgumentAsList)
			throw new IllegalStateException(
					"this a list-argument, please call getArguments() instead of getArgument()");
		return argument;
	}

	public String[] getArguments() {
		ArrayList<String> args = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(argument, "" + listSeparator);
		while (st.hasMoreTokens())
			args.add(st.nextToken());
		String[] res = new String[args.size()];
		args.toArray(res);
		return res;
	}

	public String getDefault() {
		return default_;
	}

	void setDefault(String default_) {
		this.default_ = default_;
	}

	public Parameter hideValue() {
		this.hideValue = true;
		return this;
	}

	public boolean hideValueIfPossible() {
		return hideValue;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Parameter other = (Parameter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public boolean appliesTo(String currToken) {
		if (currToken == null || "".equals(currToken))
			return false;
		if (currToken.equals("--" + getName()))
			return true;
		if (currToken.equals(abbreviation))
			return true;
		return false;
	}

	public boolean wasParsed() {
		return parsed;
	}

	public void setParsed() {
		parsed = true;
	}

	@Override
	public String toString() {
		return "Parameter [name=" + name + ", argument=" + (hideValue ? "(hidden)" : argument) + ", default_="
				+ default_ + ", description=" + description + ", abbreviation=" + abbreviation + ", isMandatory="
				+ isMandatory + ", expectsArguments=" + expectsArguments + ", hideValue=" + hideValue + ", parsed="
				+ parsed + "]";
	}

	void setListSeparator(char listSeparator) {
		this.listSeparator = listSeparator;
	}

}
