package com.pai.codegen.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GeneralException extends RuntimeException
{
  private Log logger = null;
  private static final long serialVersionUID = 2174655670701233591L;

  private GeneralException()
  {
  }

  public GeneralException(Class clazz, String message)
  {
    super(message);
    this.logger = LogFactory.getLog(clazz);
    this.logger.error(message);
  }

  public GeneralException(Class clazz, String message, Throwable cause) {
    super(message, cause);
    this.logger = LogFactory.getLog(clazz);
    this.logger.error(message);
  }

  public GeneralException(Class clazz, Throwable cause) {
    super(cause);
    this.logger = LogFactory.getLog(clazz);
    this.logger.error(cause.getMessage());
  }
}