package com.kpouer.netflow.model.protocol;

public interface Protocol {
    boolean isRequest();
    String id();
}
