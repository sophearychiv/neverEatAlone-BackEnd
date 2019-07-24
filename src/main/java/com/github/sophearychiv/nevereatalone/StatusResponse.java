package com.github.sophearychiv.nevereatalone;

public enum StatusResponse {
    SUCCESS("Success"), ERROR("Error"), NOT_FOUND("Not_Found");

    final private String status;

    StatusResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}