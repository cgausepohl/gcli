/*
 * Author Christian Gausepohl
 * License: CC0 (no copyright if possible, otherwise fallback to public domain)
 */
package com.cg.cli;

import java.util.ArrayList;

public class Example {

	static void minExample(String[] args) {
		CLIRules rules = new CLIRules("Exporting a database-table to stdout");
		rules.addRequired("jdbc");
		rules.addRequired("user");
		rules.addRequired("password").hideValue();
		rules.addRequired("tablenames").treatArgumentAsList();
		rules.addFlag("quite");

		rules.printHelp("jdbc", "user", "password", "tablenames", "quite");

		CLIParsed parsed = null;
		try {
			parsed = new CLIParsed(rules, args);
			System.out.println(parsed.getArgument("jdbc"));
			System.out.println(parsed.hasFlag("quite") ? "use quite mode" : "no quite requested");
		} catch (CLIParsedException e) {
			e.printStackTrace();
		} finally {
			parsed.printParams(System.out);
		}
	}

	static void maxExample(String[] args) {
		CLIRules rules = new CLIRules("Exporting a database-table to stdout");
		rules.addRequired("jdbc", "j").setDescription("JDBC connection String");
		rules.addRequired("user", "u").setDescription("database username, used during login");
		rules.addRequired("password", "p").hideValue().setDescription("password for user");
		rules.addOptional("tablenames", "t", "%").treatArgumentAsList()
				.setDescription("tablenames, you can also use SQL like expresions");
		rules.addFlag("quite", "q").setDescription("suppress output");

		rules.printHelp("jdbc", "user", "password", "tablenames", "quite");

		CLIParsed parsed = null;
		try {
			parsed = new CLIParsed(rules, args);
			System.out.println(parsed.getArgument("jdbc"));
			System.out.println(parsed.hasFlag("quite") ? "use quite mode" : "no quite requested");
		} catch (CLIParsedException e) {
			e.printStackTrace();
		} finally {
			parsed.printParams(System.out);
		}
	}

	public static void main(String[] args) {
		
		ArrayList<String[]> argsExamples = new ArrayList<>();

		// java com.binrock.cli.Example --jdbc jdbc:oracle:thin:@localhost:1521/xe
		// --user scott --password tiger --tablenames A
		String[] argsmin1 = { "--jdbc", "jdbc:oracle:thin:@localhost:1521/xe", "--user", "scott", "--password", "tiger",
				"--tablenames", "A" };
		argsExamples.add(argsmin1);
		// java com.binrock.cli.Example --jdbc jdbc:oracle:thin:@localhost:1521/xe
		// --user scott --password tiger --tablenames A,B
		String[] argsmin2 = { "--jdbc", "jdbc:oracle:thin:@localhost:1521/xe", "--user", "scott", "--password", "tiger",
				"--tablenames", "A,B" };
		argsExamples.add(argsmin2);
		// java com.binrock.cli.Example --jdbc jdbc:oracle:thin:@localhost:1521/xe
		// --user scott --password tiger --tablenames A,%
		String[] argsmin3 = { "--jdbc", "jdbc:oracle:thin:@localhost:1521/xe", "--user", "scott", "--password", "tiger",
				"--tablenames", "A,%" };
		argsExamples.add(argsmin3);
		// java com.binrock.cli.Example --jdbc jdbc:oracle:thin:@localhost:1521/xe
		// --user scott --password tiger --tablenames %
		String[] argsmin4 = { "--jdbc", "jdbc:oracle:thin:@localhost:1521/xe", "--user", "scott", "--password", "tiger",
				"--tablenames", "%" };
		argsExamples.add(argsmin4);
		// java com.binrock.cli.Example --jdbc jdbc:oracle:thin:@localhost:1521/xe
		// --user scott --password tiger --tablenames TMP\_%
		String[] argsmin5 = { "--jdbc", "jdbc:oracle:thin:@localhost:1521/xe", "--user", "scott", "--password", "tiger",
				"--tablenames", "TMP\\_" };
		argsExamples.add(argsmin5);
		// java com.binrock.cli.Example --jdbc jdbc:oracle:thin:@localhost:1521/xe
		// --user scott --password tiger --tablenames TMP_%
		String[] argsmin6 = { "--jdbc", "jdbc:oracle:thin:@localhost:1521/xe", "--user", "scott", "--password", "tiger",
				"--tablenames", "TMP_%" };
		argsExamples.add(argsmin6);
		// java com.binrock.cli.Example --jdbc jdbc:oracle:thin:@localhost:1521/xe
		// --user scott --password tiger --tablenames A --quite
		String[] argsmin7 = { "--jdbc", "jdbc:oracle:thin:@localhost:1521/xe", "--user", "scott", "--password", "tiger",
				"--tablenames", "A", "--quite" };
		argsExamples.add(argsmin7);
		// java com.binrock.cli.Example --jdbc jdbc:oracle:thin:@localhost:1521/xe
		// --quite --user scott --password tiger --tablenames A
		String[] argsmin8 = { "--jdbc", "jdbc:oracle:thin:@localhost:1521/xe", "--quite", "--user", "scott",
				"--password", "tiger", "--tablenames", "A" };
		argsExamples.add(argsmin8);

		for (String[] e : argsExamples) {
			minExample(e);
			maxExample(e);
		}
	}
}
