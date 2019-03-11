/**
 * The Following grammer was used in this parser
 * E := A | X
 * A := A+M | M --> A:= M+A | M
 * M := M*M | X
 * X := (E) | L
 * L := [0-9]+ | [a-z]
 */
public class SimpleExpressionParser implements ExpressionParser {
	/*
	 * Attempts to create an expression tree -- flattened as much as possible -- from the specified String.
         * Throws a ExpressionParseException if the specified string cannot be parsed.
	 * @param str the string to parse into an expression tree
	 * @param withJavaFXControls you can just ignore this variable for R1
	 * @return the Expression object representing the parsed expression tree
	 */
	public Expression parse (String str, boolean withJavaFXControls) throws ExpressionParseException {
		// Remove spaces -- this simplifies the parsing logic
		str = str.replaceAll(" ", "");
		Expression expression = parseExpression(str);
		if (expression == null) {
			// If we couldn't parse the string, then raise an error
			throw new ExpressionParseException("Cannot parse expression: " + str);
		}
		// Flatten the expression before returning
		expression.flatten();
		return expression;
	}
	
	/**
	 * This is the first parse function (E) with grammer E := A | X.
	 * @param str String to be parsed
	 * @param parent Expression lastParsed
	 * @returns an Expression based on the grammer.
	 */
	private Expression ParseE(String str, Expression parent) {
		Expression expression = null;
		
		expression = ParseA(str, parent);
		if(expression != null) {return expression;}
		return ParseX(str, parent);
	}
	
	/**
	 * This is the secound parse function (A) with grammer A := A+M || M --> A := M+A || M.
	 * @param str String to be parsed
	 * @param parent Expression lastParsed
	 * @returns an Expression based on the grammer.
	 */
	//I know I should have compressed ParseM and ParseA, but I couldn't quite figure out a way to use lambda functions or just better parsing methods for that matter
	private Expression ParseA(String str, Expression parent) {
		Expression expression = null;
		if (str.contains("+") && !(str.startsWith("(") && str.endsWith(")"))) {
			int crnt = str.indexOf("+");
			//check to see if + might be in parenthesis
			if(str.contains("(") && str.contains(")")) {
				String checker;
				String searcher = str.substring(crnt + 1);
				checker = searcher.substring(0, searcher.indexOf(")") + 1);
				while(true) {
					//if + is outside the parenthesis or the whole string has been checked the grammer is valid
					if(checker.contains("(") || checker.isEmpty()) {
						expression = new AdditionExpression();
						break;
					}else {
						//move to the next + if there is one
						if(searcher.contains("+")) {
							crnt += searcher.indexOf("+") + 1;
							searcher = searcher.substring(searcher.indexOf("+") + 1);
							checker = checker.substring(checker.indexOf(")") + 1);
						}else {break;}
					}
				}
			}else {
				expression = new AdditionExpression();
			}
			if(expression != null) {
				expression.setParent((CompoundExpression) parent);
				//M+A
				((AdditionExpression) expression).addSubexpression(ParseM(str.substring(0, crnt), expression));
				((AdditionExpression) expression).addSubexpression(ParseA(str.substring(crnt + 1), expression));
				
				return expression;
			}
		}
		return ParseM(str, parent);
	}
	
	/**
	 * This is the third parse function (M) with grammer M := M*M || X.
	 * @param str String to be parsed
	 * @param parent Expression lastParsed
	 * @returns an Expression based on the grammer.
	 */
	//I know I should have compressed ParseM and ParseA, but I couldn't quite figure out a way to use lambda functions or just better parsing methods for that matter
	private Expression ParseM(String str, Expression parent) {
		Expression expression = null;
		if (str.contains("*") && !(str.startsWith("(") && str.endsWith(")"))) {
			int crnt = str.indexOf("*");
			//check to see if * might be in parenthesis
			if(str.contains("(") && str.contains(")")) {
				String checker;
				String searcher = str.substring(crnt + 1);
				checker = searcher.substring(0, searcher.indexOf(")") + 1);
				while(true) {
					//if * is outside the parenthesis or the whole string has been checked the grammer is valid
					if(checker.contains("(") || checker.isEmpty()) {
						expression = new MultiplicationExpression();
						break;
					}else {
						//move to the next * if there is one
						if(searcher.contains("*")) {
							crnt += searcher.indexOf("*") + 1;
							searcher = searcher.substring(searcher.indexOf("*") + 1);
							checker = checker.substring(checker.indexOf(")") + 1);
						}else {break;}
					}
				}
			}else {
				expression = new MultiplicationExpression();
			}
			if(expression != null) {
				expression.setParent((CompoundExpression) parent);
				((MultiplicationExpression) expression).addSubexpression(ParseM(str.substring(0, crnt), expression));
				((MultiplicationExpression) expression).addSubexpression(ParseM(str.substring(crnt + 1), expression));
				return expression;
			}
		}
		return ParseX(str, parent);
	}
	
	/**
	 * This is the fourth parse function (X) with grammer X := (E) || L.
	 * Clarification: This function also uses isValidNotBasic to correct grammer misses for purposes of this assignment.
	 * Will return null if the () are empty instead of E.
	 * @param str String to be parsed
	 * @param parent Expression lastParsed
	 * @returns an Expression based on the grammer.
	 */
	private Expression ParseX(String str, Expression parent) {
		Expression expression = null;
		if(str.startsWith("(") && str.endsWith(")")) {
			String tester = str.replace(")", ""); tester = tester.replace("(", "");
			if(tester.isEmpty()) {return null;}
			expression = new ParenthesisExpression();
			expression.setParent((CompoundExpression) parent);
			((ParenthesisExpression) expression).addSubexpression(ParseE(str.substring(1, str.length() - 1), expression));
			return expression;
		}
		
		switch(isValidNotBasic(str)){
			case 0: return null; 
			case 1: return ParseE(str, parent);
		}
		return ParseL(str, parent);
	}
	
	/**
	 * This is the fifth parse function (L) with grammer L := [0-9]+ || [a-z].
	 * @param str String to be parsed
	 * @param parent Expression lastParsed
	 * @returns an Expression based on the grammer.
	 */
	private Expression ParseL(String str, Expression parent) {
		Expression expression = null;
		String test = "abcdefghijklmnopkrstuvwxyz1234567890";
		if(str.length() > 0) {
			if(test.contains(str.substring(0, 1))) {
				expression = new BasicExpression(str, (CompoundExpression) parent);
				return expression;
			}
		}
		return null;
	}
	
	/**
	 * This is semi-parse function that cleans up the grammer for the purposes of this assignment.
	 * @param str String to be parsed
	 * @return int based on input and grammer of parser: 0 -> invalid input, 1 -> grammer breaking but still valid, 2 -> valid.
	 */
	private int isValidNotBasic(String str) {
		boolean test = str.lastIndexOf("+") == str.length() - 1 || str.lastIndexOf("*") == str.length() - 1 || str.indexOf("+") == 0 || str.indexOf("*") == 0;
		boolean test2 = (str.contains("+") || str.contains("*")) || (str.contains("(") && str.contains(")"));
		if(test){
			return 0;
		}else if (test2) {
			return 1;
		}
		return 2;
	}
	
	/**
	 * Call to begin parsing.
	 * @param str String to be parsed
	 */
	protected Expression parseExpression (String str) {
		return ParseE(str, null);
	}
}
