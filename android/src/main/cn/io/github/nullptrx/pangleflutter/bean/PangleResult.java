package io.github.nullptrx.pangleflutter.bean;

import java.util.HashMap;
import java.util.List;

public class PangleResult {
  public int code;
  public String message;
  public Boolean verify;
  public Integer count;
  public List data;

  public HashMap<String, Object> toMap() {
    HashMap<String, Object> map = new HashMap<>();
    map.put("code", code);
    map.put("message", message);
    map.put("verify", verify);
    map.put("count", count);
    map.put("data", data);
    return map;
  }
}
