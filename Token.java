package lexan;

public final class Token {
  // Character Class Constants
  public static final int LETTER = 0;
  public static final int DIGIT = 1;
  public static final int END_INPUT = 98;
  public static final int UNKNOWN = 99;

  // Token Code Constants
  public static final int INT_LIT = 10;
  public static final int IDENT = 11;
  public static final int ASSIGN_OP = 20;
  public static final int ADD_OP = 21;
  public static final int SUB_OP = 22;
  public static final int MULT_OP = 23;
  public static final int DIV_OP = 24;
  public static final int LEFT_PAREN = 25;
  public static final int RIGHT_PAREN = 26;
  public static final int END_EXPR = 27;

  // Other char Constants
  public static final char CHAR_PERIOD = '.';
  public static final char CHAR_NEGATIVE = '-';

  private Token() {};
}
