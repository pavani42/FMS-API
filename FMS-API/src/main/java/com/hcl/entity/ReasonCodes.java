package com.hcl.entity;

public enum ReasonCodes {
    INVALID(899, "Invalid Customer Id"),
    MESSAGEFORMATERROR(800, "Message format error"),
    APPROVED(000, "Approved"),
    DUPLICATE_MESSAGE(333, "Duplicate Message");

    private final int code;
    private final String statusDesc;
    ReasonCodes(int code, String statusDesc) {
        this.code = code;
        this.statusDesc = statusDesc;
    }

    public int getCode()
    {
        return code;
    }

    public String getStatusDesc()
    {
        return statusDesc;
    }
}
