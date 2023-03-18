package com.kpouer.netflow.model.http;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kpouer.netflow.model.protocol.AbstractProtocol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonDeserialize(using = HttpDeserializer.class)
public class Http extends AbstractProtocol {
    private String method;
    private int status;

    @Override
    public boolean isRequest() {
        return status == 0;
    }

    @Override
    public String id() {
        return "http";
    }
}
