package com.example.CS6650_A1.Server;

public class IntHelper {

    public static boolean isInteger(String s) {
      try {
        Integer.parseInt(s);
      } catch(NumberFormatException e) {
        return false;
      }
      return true;
    }
}
