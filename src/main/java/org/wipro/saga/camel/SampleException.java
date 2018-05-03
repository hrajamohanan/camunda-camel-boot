package org.wipro.saga.camel;

public class SampleException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public SampleException(String message) {
    super(message);
  }
}
