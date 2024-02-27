package com.hcl.exceptions;

import com.hcl.daoService.CustomerDaoService;
import com.hcl.entity.ReasonCodes;
import com.hcl.entity.StatusMessage;
import com.hcl.entity.Transaction_Audit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler  {
    @Autowired
    CustomerDaoService customerDaoService;
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ErrorResponse handleNotFound(CustomException idNotFound)
    {
        LocalDateTime msgInTime = LocalDateTime.now();
        ErrorResponse error = new ErrorResponse();
        String[] exception = idNotFound.getMessage().split("\\s+", 3);
        System.out.println(exception[0] + " " + exception[1]);
        ReasonCodes reasonCode = null;
        System.out.println(idNotFound.getMessage().substring(3));
        if(exception[2].equals(ReasonCodes.INVALID.getStatusDesc())){
//            System.out.println(ReasonCodes.INVALID.getCode());
            reasonCode = ReasonCodes.INVALID;
            error.setCustomerId(Long.parseLong(exception[1]));
            error.setStatusCode(ReasonCodes.INVALID.getCode());
            error.setFailureReason(ReasonCodes.INVALID.getStatusDesc());
        }
        else if(exception[2].equals(ReasonCodes.DUPLICATE_MESSAGE.getStatusDesc())) {
            reasonCode = ReasonCodes.DUPLICATE_MESSAGE;
            error.setCustomerId(Long.parseLong(exception[1]));
            error.setStatusCode(ReasonCodes.DUPLICATE_MESSAGE.getCode());
            error.setFailureReason(ReasonCodes.DUPLICATE_MESSAGE.getStatusDesc());
        }
        else if(exception[2].equals(ReasonCodes.MESSAGEFORMATERROR.getStatusDesc())) {
            reasonCode = ReasonCodes.MESSAGEFORMATERROR;
            error.setCustomerId(Long.parseLong(exception[1]));
            error.setStatusCode(ReasonCodes.MESSAGEFORMATERROR.getCode());
            error.setFailureReason(ReasonCodes.MESSAGEFORMATERROR.getStatusDesc());
        }
        Transaction_Audit transactionAudit = Transaction_Audit.builder().iMessage("localhost:8081/api" + exception[0] + error.getCustomerId())
                .status(StatusMessage.FAILURE).messgInTime(msgInTime).reasonCode(reasonCode).messOutTime(LocalDateTime.now()).build();
        customerDaoService.saveTransaction(transactionAudit);
        return error;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse badRequestResponse(MissingServletRequestParameterException exception)
    {
        LocalDateTime msgInTime = LocalDateTime.now();
        exception.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(ReasonCodes.MESSAGEFORMATERROR.getCode());
        errorResponse.setFailureReason(ReasonCodes.MESSAGEFORMATERROR.getStatusDesc());
        Transaction_Audit transactionAudit = Transaction_Audit.builder().iMessage("localhost:8081/api/core/customer?customerId=" + errorResponse.getCustomerId())
                .status(StatusMessage.FAILURE).messgInTime(msgInTime).reasonCode(ReasonCodes.MESSAGEFORMATERROR).messOutTime(LocalDateTime.now()).build();
//        customerDaoService.saveTransaction(transactionAudit);
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse notFoundResponse(MethodArgumentTypeMismatchException exception)
    {
        LocalDateTime msgInTime = LocalDateTime.now();
        ErrorResponse errorResponse = new ErrorResponse();
        System.out.println("In Exception class" + exception.getRootCause());
        errorResponse.setStatusCode(ReasonCodes.MESSAGEFORMATERROR.getCode());
        errorResponse.setFailureReason(ReasonCodes.MESSAGEFORMATERROR.getStatusDesc());
        Transaction_Audit transactionAudit = Transaction_Audit.builder().iMessage("localhost:8081/api/core/customer?customerId=" + exception.getValue())
                .status(StatusMessage.FAILURE).messgInTime(msgInTime).reasonCode(ReasonCodes.MESSAGEFORMATERROR).messOutTime(LocalDateTime.now()).build();
        customerDaoService.saveTransaction(transactionAudit);
        return errorResponse;
    }

}
