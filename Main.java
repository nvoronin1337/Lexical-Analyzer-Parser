package lexan;

/*
 * Nikita Voronin
 * COMP-3350 Programming Languages
 * Summer 2021 Lab 5
 * Parser
 */

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

  private static final int MAX_INPUT_SIZE = 1000;
  private static Scanner scanner;
  
  // Reads statement from console
  private static void console() throws IndexOutOfBoundsException {
    var input = new char[MAX_INPUT_SIZE];
    do {
      System.out.println("Enter expression ending in dot (.)");
      input = scanner.nextLine().toCharArray();
    } while (input[input.length - 1] != Token.CHAR_PERIOD);
    Parser.parse(input);
  }

  // Reads statement from file
  private static void file(final String SOURCE) throws IOException {
    try (var br = new BufferedReader(new FileReader(SOURCE))) {
      var fileData = new char[MAX_INPUT_SIZE];
      var index = 0;
      int ch;
      while((ch=br.read()) != -1){
        if (index >= MAX_INPUT_SIZE) {
          throw new IndexOutOfBoundsException("Exceeded MAX_INPUT_SIZE OF " + MAX_INPUT_SIZE);
        }
        fileData[index] = (char)ch;
        index += 1;
      }
      
      if (fileData[index-1] == Token.CHAR_PERIOD) {
        Parser.parse(fileData);
      } else {
        throw new IllegalArgumentException("Missing '.' at the end of input");
      }
    }
  }

  public static void main(String[] args) {
    try {
      scanner = new Scanner(System.in);
      System.out.println("Select Mode:");
      System.out.println("1. Console");
      System.out.println("2. File");
      char in = scanner.nextLine().charAt(0);

      switch (in) {
      case '1':
        console();
        break;
      case '2':
        System.out.println();
        System.out.println("Enter file name:");
        final String SOURCE = scanner.nextLine();
        file(SOURCE);
        break;
      default:
        break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      scanner.close();
    }
  }
}
