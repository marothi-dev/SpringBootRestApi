
package com.peu.springboot.model;

import lombok.Data;

@Data
public class User {

  private long id;
  private String name;
  private String password;
  private String phone;
  private String token;

}
