package com.hcl.exceptions;

import com.hcl.entity.ReasonCodes;

public class CustomException extends RuntimeException{
   private String msg;
    public CustomException(String msg)
    {
        super(msg);
//        System.out.println(msg);
        this.msg = msg;
    }
}
