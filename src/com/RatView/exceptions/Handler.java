/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.RatView.exceptions;

/**
 *
 * @author Simon
 */
public class Handler implements Thread.UncaughtExceptionHandler {
  public void uncaughtException(Thread t, Throwable e) {
    System.out.println("Throwable: " + e.getMessage());
    System.out.println(t.toString());
  }
}
