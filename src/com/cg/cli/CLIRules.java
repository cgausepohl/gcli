/*
 * Author Christian Gausepohl
 * License: CC0 (no copyright if possible, otherwise fallback to public domain)
 */
package com.cg.cli;

import java.util.Hashtable;
import java.util.Set;

public class CLIRules {

	private String purpose;
	Hashtable<String, Parameter> params = new Hashtable<>();
	char listSeparator = ',';

	public CLIRules(String purpose) {
		this.purpose = purpose;
	}

	public void setListSeparator(char arraySeparator) {
		this.listSeparator = arraySeparator;
	}

	private Parameter addParam(String nameLong, String nameShort, boolean isMandatory, boolean expectsArgument) {
		checkParamName(nameLong);
		Parameter p = new Parameter(nameLong, nameShort, isMandatory, expectsArgument);
		if (p.isArgumentList())
			p.setListSeparator(listSeparator);
		params.put(nameLong, p);
		return p;
	}

	public Parameter addRequired(String nameLong) {
		return addRequired(nameLong, null);
	}

	public Parameter addRequired(String nameLong, String nameShort) {
		return addParam(nameLong, nameShort, true, true);
	}

	public Parameter addOptional(String nameLong, String default_) {
		return addOptional(nameLong, null, default_);
	}

	public Parameter addOptional(String nameLong, String nameShort, String default_) {
		Parameter p = addParam(nameLong, nameShort, false, true);
		p.setDefault(default_);
		return p;
	}

	public Parameter addFlag(String nameLong) {
		return addFlag(nameLong, null);
	}

	public Parameter addFlag(String nameLong, String nameShort) {
		return addParam(nameLong, nameShort, false, false);
	}

	public Set<String> getParameterNames() {
		return params.keySet();
	}

	private void checkParamName(String pName) {
		// rules:
		// (5) must be != null and length>=1
		// (10) must be new
		// (15) char "-" not allowed
		// (20) no whitespaces (space, return, tab etc allowed)
		// (30) max length=40

		// (5)
		if (pName == null || "".equals(pName)) {
			throw new IllegalArgumentException("parameter/option name must have a value");
		}
		// (10)
		for (String paramAlreadyDefined : params.keySet())
			if (paramAlreadyDefined.equals(pName))
				throw new IllegalArgumentException("parameter/option already declared");

		// (15)
		if (pName.indexOf('-') != -1) {
			throw new IllegalArgumentException("the character '-' is not allowed in a parameter");
		}
		// (20)
		if (pName.replaceFirst("\\s", "-").indexOf('-') != -1) {
			throw new IllegalArgumentException(
					"whitespace characters (space, tabe, newline) are not allowed in a parameter");
		}
		// (30)
		if (pName.length() >= 40) {
			throw new IllegalArgumentException(
					"length of paramter is to big, max allowed=40, current=" + pName.length() + ", value=" + pName);
		}
	}

	public void printHelp(String... parameterOrder) {

		if (purpose != null)
			System.out.println(purpose);
		/*
		 * kann das weg? String[] keys = new String[params.size()]; int i=0; for(String
		 * key: params.keySet()) keys[i++] = key; Arrays.sort(keys);
		 */
		String argListPattern = " <arg1{" + listSeparator + "arg2...}";
		int max = 0;
		for (String k : params.keySet()) {
			Parameter p = params.get(k);
			int l = 2 + p.getName().length(); // 2 is for --
			if (p.getAbbreviation() != null && p.getAbbreviation().length() > 0)
				l += 2 + p.getAbbreviation().length(); // 2 is for |-
			if (!p.isMandatory())
				l += 2; // 2 is for [ and ]
			if (p.isArgumentList())
				l += argListPattern.length();
			if (l > max)
				max = l;
		}

		// distance between name column list and information column
		max += 4;

		for (String k : parameterOrder) {
			if ("".equals(k)) {
				System.out.println();
			} else {
				Parameter p = params.get(k);
				if (p == null) {
					System.out.print("no param found for key=" + k);
				} else {
					if (!p.isMandatory())
						System.out.print("[");
					System.out.print("--" + p.getName());
					int l = p.getName().length();
					if (p.getAbbreviation() != null && p.getAbbreviation().length() > 0) {
						System.out.print("|-" + p.getAbbreviation());
						l += 2;
					}
					if (!p.isMandatory())
						System.out.print("]");
					if (!p.isMandatory())
						l += 2;

					if (p.isArgumentList()) {
						l += argListPattern.length();
						System.out.print(argListPattern);
					}

					if (p.getMinValue() != null || p.getMaxValue() != null) {
						String s = " <";
						if (p.getMinValue() != null)
							s += p.getMinValue();
						s += "..";
						if (p.getMaxValue() != null)
							s += p.getMaxValue();
						s += ">";
						l += s.length();
						System.out.print(s);
					}

					if (!p.isFlag() && !p.isArgumentList() && p.getMaxValue() == null && p.getMinValue() == null) {
						String s = " <arg>";
						l += s.length();
						System.out.print(s);
					}

					for (int j = 0; j < max - l; j++)
						System.out.print(" ");

					if (p.getDescription() != null && p.getDescription().length() > 0)
						System.out.print(p.getDescription());

					if (!p.isMandatory() && !p.isFlag()) {
						System.out.print(" (default=" + p.getDefault() + ")");
					}
				}
				System.out.println();
			}
		}
	}
}
