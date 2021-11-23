package lexan;

import java.text.DecimalFormat;

public final class Parser {

  private static int saveToken;

  private Parser() {};

  public static void parse(char[] statement) {
    try {
      if (!Lexan.isLocked()) {
        Lexan.setData(statement);
        statement();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /* statementLexan.lex(); = {expression  ";" } "."    */
  private static void statement() {
    var df = new DecimalFormat("#.##");
    Lexan.getChar();
    Lexan.lex();
    while (Lexan.getNextToken() != Token.END_INPUT) {
      var result = expression();
      Lexan.lex();
      System.out.println("Result of expression: " + df.format(result));
    }
  }

  /* expression = term { ( "+" | "-" ) term } */
  private static double expression() {  
    var left = term();
    while (Lexan.getNextToken() == Token.ADD_OP || Lexan.getNextToken() == Token.SUB_OP) {
      saveToken = Lexan.getNextToken();
      Lexan.lex();
      if (saveToken == Token.ADD_OP) {
        left += term();
      } else if (saveToken == Token.SUB_OP) {
        left -= term();
      }
    }
    return left;
  }

  /* term = factor {("*" | "/") factor}  	*/
  private static double term() {
    var left = factor();
    while (Lexan.getNextToken() == Token.MULT_OP || Lexan.getNextToken() == Token.DIV_OP) {
      saveToken = Lexan.getNextToken();
      Lexan.lex();
      if (saveToken == Token.MULT_OP) {
        left *= factor();
      } else if (saveToken == Token.DIV_OP) {
        var right = factor();
        if (right != 0) {
          left /= right;
        } else {
          throw new IllegalArgumentException("Arithmetic Error! Division by zero!");
        }
      }
    }
    return left;
  }

  /* factor = number | "-"number | "(" expression ")"   */
  private static double factor() {
    double value;
    if (Lexan.getNextToken() == Token.INT_LIT) {
      value = Double.parseDouble(String.valueOf(Lexan.getLexeme()));
      Lexan.lex();
    } else if (Lexan.getNextToken() == Token.LEFT_PAREN) {
      Lexan.lex();
      value = expression();
      if (Lexan.getNextToken() != Token.RIGHT_PAREN) {
        throw new IllegalArgumentException("Syntax Error! Expected a ')'.");
      }
      Lexan.lex();
    } else {
      throw new IllegalArgumentException("Syntax Error! Expected 'number' or a '('.");
    }
    return value;
  }
}
