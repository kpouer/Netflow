package com.kpouer.netflow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Ip {
    @JsonProperty("ip.src")
    private String ip_src;
    @JsonProperty("ip.dst")
    private String ip_dst;
}
