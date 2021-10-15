package io.github.nullptrx.pangleflutter.bean;

import java.util.List;

public class FeedBeanWrap {

  String message;
  int code;
  int count;
  List<String> data;

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public List<String> getData() {
    return data;
  }

  public void setData(List<String> data) {
    this.data = data;
  }
}
