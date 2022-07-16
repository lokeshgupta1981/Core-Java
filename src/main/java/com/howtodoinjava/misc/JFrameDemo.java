package com.howtodoinjava.misc;

import javax.swing.*;

public class JFrameDemo {

  public static void main(String args[]){
    JFrame jf = new JFrame("Hi");
    jf.setVisible(true);
    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    System.out.println("Hi");
  }
}
