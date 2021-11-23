package lexan;

/*
 * Nikita Voronin
 * COMP-3350 Programming Languages
 * Summer 2021 Lab 5
 * Lexical Analyzer
 */

import java.util.HashMap;
import java.util.Map;

public final class Lexan {
  private static final int LEXEME_SIZE = 100;
  private static final Map<Character, Integer> TOKEN_CODES_MAP;

  // Mutable Vars
  private static int nextToken;
  private static int prevToken;
  private static char nextChar;
  private static int charClass;
  private static int currentIndex;
  private static boolean locked;
  private static int lexLen;
  private static char[] data;
  private static char[] lexeme;

  // Prevents instantiation of this class (everything is static, no need for instances)
  private Lexan() {};

  // Static constructor
  static {
    TOKEN_CODES_MAP = new HashMap<>();
    TOKEN_CODES_MAP.put('(', Token.LEFT_PAREN);
    TOKEN_CODES_MAP.put(')', Token.RIGHT_PAREN);
    TOKEN_CODES_MAP.put('+', Token.ADD_OP);
    TOKEN_CODES_MAP.put('-', Token.SUB_OP);
    TOKEN_CODES_MAP.put('*', Token.MULT_OP);
    TOKEN_CODES_MAP.put('/', Token.DIV_OP);
    TOKEN_CODES_MAP.put('=', Token.ASSIGN_OP);
    TOKEN_CODES_MAP.put(';', Token.END_EXPR);
    locked = false;
  }

  public static int getNextToken() {
    return nextToken;
  }

  public static char[] getLexeme() {
    return lexeme;
  }

  // Prevents user from changing input while we are not finished analysing it
  public static void setData(char[] d) {
    if (!isLocked()) {
      lock();
      data = d;
      currentIndex = 0;
    }
  }

  public static boolean isLocked() {
    return locked;
  }

  private static void lock() {
    locked = true;
  }

  private static void unlock() {
    locked = false;
  }

  /* 
   * getChar - a function accepts the next character of input 
   * and determine its character class
   * throws - IndexOutOfBoundsException
   */
  public static void getChar() throws IndexOutOfBoundsException {
    if (currentIndex >= data.length) {
      throw new IndexOutOfBoundsException("Lexical Analizer Error! Input index is out of bounds.");
    }

    nextChar = data[currentIndex];
    currentIndex += 1;

    if (nextChar != Token.CHAR_PERIOD) {
      if (Character.isLetter(nextChar)) {
        charClass = Token.LETTER;
      } else if (Character.isDigit(nextChar)) {
        charClass = Token.DIGIT;
      } else {
        charClass = Token.UNKNOWN;
      }
    } else {
      charClass = Token.END_INPUT;
    }
  }

  /*
   * addChar - function checks bounds of lexeme and adds nextChar to it if there is enough space
   * throws - IndexOutOfBoundsException
   */
  private static void addChar() throws IndexOutOfBoundsException {
    if (lexLen > LEXEME_SIZE) {
      throw new IndexOutOfBoundsException("Lexical Analizer Error! Lexeme is too large.");
    }
    lexeme[lexLen] = nextChar;
    lexLen++;
  }

  /*
   * lex - function builds a lexeme out of incoming characters
   * throws - IndexOutOfBounds exception and IllegalArgumentException
   */
  public static void lex() throws IndexOutOfBoundsException, IllegalArgumentException {
    if (currentIndex > data.length) {
      throw new IndexOutOfBoundsException("Lexical Analizer Error! Input index is out of bounds.");
    }

    lexLen = 0;
    lexeme = new char[LEXEME_SIZE];

    while(Character.isWhitespace(nextChar) || nextChar == '\n' || nextChar == '\t') {
      getChar();
    }

    switch(charClass) {
    case Token.LETTER:
      do {
        addChar();
        getChar();
      } while(charClass == Token.LETTER || charClass == Token.DIGIT);
      prevToken = nextToken;
      nextToken = Token.IDENT;
      break;

    case Token.DIGIT: 
      do {
        addChar();
        getChar();
      } while(charClass == Token.DIGIT);
      prevToken = nextToken;
      nextToken = Token.INT_LIT;
      break;

    case Token.UNKNOWN:
      lookup();
      addChar();
      getChar();

      // Sometimes '-' can mean that value following it must be negative
      if (isNegativeInteger()) {
        do {
          addChar();
          getChar();
        } while(charClass == Token.DIGIT);
        prevToken = nextToken;
        nextToken = Token.INT_LIT;
      }
      break;
      
    case Token.END_INPUT:
      prevToken = nextToken;
      nextToken = Token.END_INPUT;
      lexeme[0] = Token.CHAR_PERIOD;
      break;
    
    default:
      throw new IllegalArgumentException("Lexical Analizer Exception! Unresolved character class.");
    }
  }

  /*
   * lookup - function checks if nextChar of UNKNOWN charClass is one of the known tokens or EOF
   */
  private static void lookup() {
    prevToken = nextToken;
    if (TOKEN_CODES_MAP.containsKey(nextChar)) {
      nextToken = TOKEN_CODES_MAP.get(nextChar);
    } else {
      nextToken = Token.END_INPUT;
      unlock();
    }
  }

  /*
   * To know that we have negative integer and not subtraction operation we must know that:
   * 1. nextToken is a '-'
   * 2. prevToken is not a 'number' or a ')'
   * 3. next character is a 'number'
   */
  private static boolean isNegativeInteger() {
    return nextToken == Token.SUB_OP && prevToken != Token.INT_LIT && prevToken != Token.RIGHT_PAREN;
  }
}
