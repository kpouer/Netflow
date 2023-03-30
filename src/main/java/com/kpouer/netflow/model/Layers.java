package com.kpouer.netflow.model;

import com.kpouer.netflow.model.diameter.Diameter;
import com.kpouer.netflow.model.http.Http;
import com.kpouer.netflow.model.sip.Sip;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Layers {
    private Ip ip;
    private Sip  sip;
    private Http http;
    private Diameter diameter;

    public boolean isEmpty() {
        return sip == null && http == null && diameter == null;
    }
}
