package com.pai.codegen.model;

public class CommonResult
{
  private boolean success = false;
  private String msgCode;
  private String msg;

  public boolean isSuccess()
  {
    return this.success;
  }
  public void setSuccess(boolean success) {
    this.success = success;
  }
  public String getMsgCode() {
    return this.msgCode;
  }
  public void setMsgCode(String msgCode) {
    this.msgCode = msgCode;
  }
  public String getMsg() {
    return this.msg;
  }
  public void setMsg(String msg) {
    this.msg = msg;
  }
}