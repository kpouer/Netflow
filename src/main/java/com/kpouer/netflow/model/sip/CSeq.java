package com.kpouer.netflow.model.sip;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CSeq {
    @JsonProperty("sip.CSeq.method")
    private String method;
}
