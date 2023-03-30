package com.kpouer.netflow.model.sip;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kpouer.netflow.model.sip.CSeq;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SipHeaders {
    @JsonProperty("sip.CSeq_tree")
    private CSeq cseq;
    @JsonProperty("sip.Call-ID")
    private String callId;
}
