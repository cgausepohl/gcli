/*
 * Author Christian Gausepohl
 * License: CC0 (no copyright if possible, otherwise fallback to public domain)
 * https://github.com/cgausepohl/gcli
 */
package com.cg.cli;

import java.io.PrintStream;
import java.util.StringTokenizer;

public class CLIParsed {

	private CLIRules rules;
	private String[] args;

	public CLIParsed(CLIRules rules, String[] args) throws CLIParsedException {
		this.rules = rules;
		this.args = args;

		// todo empty args

		// parse from left to right
		for (int i = 0; i < args.length; i++) {
			String currToken = args[i];
			// check if currToken is any possible parameter/flag. If parameter, parse
			// arguments.
			for (Parameter p : rules.params.values()) {
				if (p.appliesTo(currToken)) {
					if (p.wasParsed())
						throw new CLIParsedException("Parameter given twice: " + currToken);

					if (!p.isFlag()) {
						i++;
						if (i > args.length)
							throw new CLIParsedException("Parameter " + currToken + " needs an argument");
						p.setArgument(args[i]);
					}

					p.setParsed();
				}
			}
		}

		// check that all mandatory parameters are given
		for (Parameter p : rules.params.values()) {
			if (p.isMandatory() && !p.wasParsed())
				throw new CLIParsedException("Missing parameter: " + p.getName());
		}

		// check values (min..max), values, regexp
		for (Parameter p : rules.params.values()) {
			if (p.wasParsed() && !p.isArgumentList() && !p.isFlag() && p.getArgument() != null) {
				// check min max
				Long l = null;
				if (p.getMinValue() != null || p.getMaxValue() != null) {
					try {
						l = Long.parseLong(p.getArgument());
					} catch (NumberFormatException nfe) {
						throw new CLIParsedException("Argument " + p.getArgument() + " for parameter " + p.getName()
								+ " must be an integer");
					}
				}
				if (p.getMinValue() != null) {
					if (p.getMinValue() > l)
						throw new CLIParsedException("Argument " + l + " for parameter " + p.getName()
								+ " is less than minValue " + p.getMinValue());
				}
				if (p.getMaxValue() != null) {
					if (p.getMaxValue() < l)
						throw new CLIParsedException("Argument " + l + " for parameter " + p.getName()
								+ " is greater than maxValue " + p.getMinValue());
				}
				//check valid values
				if (p.getValidValues().size()>0) {
					boolean ok = false;
					for (String s: p.getValidValues())
						if (s!=null && s.equals(p.getArgument()))
							ok = true;
					if (!ok) {
						StringBuffer sb = new StringBuffer(100);
						boolean first = true;
						for (String s: p.getValidValues())
							sb.append(first!=true?",":"").append(s);
						throw new CLIParsedException("Argument " + l + " for parameter " + p.getName()
						+ " is not a valid value, valid values are=" + sb.toString());
					}
				}
			}
		}
	}
	
	public Integer getIntegerArgument(String parameter) {
		String s = getArgument(parameter);
		if (s==null) return null;
		return Integer.parseInt(s);
	}

	public String getArgument(String parameter) {
		Parameter p = rules.params.get(parameter);
		if (p == null)
			return null;
		if (p.isFlag())
			throw new IllegalStateException(
					"not a parameter with argument, use hasFlag(String) instead, given name=" + parameter);
		return p.getArgument();
	}

	public boolean hasFlag(String name) {
		Parameter p = rules.params.get(name);
		if (p == null)
			return false;
		if (!p.isFlag())
			throw new IllegalStateException("not a flag, use get(String) instead, given name=" + name);
		return p.wasParsed();
	}

	public String[] getArray(String parameter) {
		String all = getArgument(parameter);
		if (all == null || all.length() == 0)
			return new String[0];
		int size = 0;
		for (int i = 0; i < all.length(); i++)
			if (all.charAt(i) == rules.listSeparator)
				size++;
		String[] arr = new String[size + 1];
		StringTokenizer st = new StringTokenizer(all, "" + rules.listSeparator);
		int i = 0;
		while (st.hasMoreTokens())
			arr[i++] = st.nextToken();
		return arr;
	}

	public void printParams(PrintStream out) {
		out.println("start: printParams");
		if (args != null) {
			String s = "";
			for (String a : args)
				s += a + " ";
			out.println("args[]   : " + s);
		}
		for (Parameter p : rules.params.values()) {
			String name = p.getName() + (p.getAbbreviation() != null ? "(" + p.getAbbreviation() + ")" : "");
			if (p.isFlag()) {
				out.println("Flag     : " + name);
			} else if (p.isArgumentList()) {
				String s = null;
				if (p.hideValueIfPossible()) {
					s = "*hidden*";
				} else {
					s = "[";
					boolean first = true;
					for (String a : p.getArguments()) {
						s += (first ? "\"" : ",\"") + a + "\"";
						first = false;
					}
					s += "]";
				}
				out.println("Parameter: " + name + " = " + s);
			} else {
				out.println("Parameter: " + name + " = " + (p.hideValueIfPossible() ? "*hidden*" : p.getArgument()));
			}
		}
		out.println("end: printParams");
	}

}
