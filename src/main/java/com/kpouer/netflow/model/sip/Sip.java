package com.kpouer.netflow.model.sip;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kpouer.netflow.model.NetflowOptions;
import com.kpouer.netflow.model.protocol.AbstractProtocol;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class Sip extends AbstractProtocol {
    @JsonProperty("sip.Status-Line_tree")
    private SipStatusLineTree statusLine;
    @JsonProperty("sip.msg_hdr_tree")
    private SipHeaders headers;

    public int getStatusCode() {
        return statusLine == null ? 0 : statusLine.getStatusCode();
    }

    @Override
    public boolean isRequest() {
        return statusLine == null;
    }

    @Override
    public String id() {
        return headers.getCallId();
    }

    public boolean shouldPrint(NetflowOptions netflowOptions) {
        if (isRequest()) {
            return true;
        }
        if (netflowOptions.isTrue("sip.skip.100") && getStatusCode() == 100) {
            return false;
        }
        if (netflowOptions.isTrue("sip.skip.1xx") && getStatusCode() < 200) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (isRequest()) {
            return headers.getCseq().getMethod();
        }
        return getStatusCode() + " " + headers.getCseq().getMethod();
    }
}
