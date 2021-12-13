package com.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter @Setter
public class Parameters  implements Serializable {

   private  String path;

   private String folder;
   private String owner;
   private Date currentData;
}
