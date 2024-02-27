package com.hcl.exceptions;

import com.hcl.entity.ReasonCodes;
import lombok.Data;

@Data
public class ErrorResponse{
    private long customerId;
    private int statusCode;
    private String failureReason;

}
