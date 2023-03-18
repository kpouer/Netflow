package com.kpouer.netflow.model;

import com.kpouer.netflow.Inventory;
import com.kpouer.netflow.model.http.Http;
import com.kpouer.netflow.model.protocol.Protocol;
import com.kpouer.netflow.model.sip.Sip;

import java.io.IOException;

public class StarUmlPrinter {
    private final Inventory      inventory;
    private final ColorPalette   colorPalette = new ColorPalette();
    private final NetflowOptions netflowOptions;
    private       StringBuilder  builder      = new StringBuilder();
    private       String         result;

    public StarUmlPrinter() throws IOException {
        inventory = new Inventory();
        netflowOptions = new NetflowOptions();
        builder.append("@startuml\n");
    }


    public void printPacket(Packet packet) {
        var  layers   = packet.get_source().getLayers();
        var  ip       = layers.getIp();
        Sip  sip      = layers.getSip();
        Http http     = layers.getHttp();
        var  diameter = layers.getDiameter();
        if (sip != null) {
            if (sip.shouldPrint(netflowOptions)) {
                int statusCode = sip.getStatusCode();
                putOrigDest(builder, ip, sip);
                if (statusCode != 0) {
                    builder.append(statusCode).append(' ');
                }
                builder.append(sip.getHeaders().getCseq().getMethod());
                builder.append('\n');
            }
        } else if (http != null) {
            putOrigDest(builder, ip, http);
            if (http.getStatus() != 0) {
                builder.append(" ").append(http.getStatus());
            } else {
                builder.append(http.getMethod());
            }
            builder.append('\n');
        } else if (diameter != null) {
            putOrigDest(builder, ip, diameter);
            var commandName = diameter.getCommandName();
            if (diameter.isRequest()) {
                builder.append(commandName);
            } else {
                builder.append(commandName, 0, 2).append("A ").append(diameter.getResultCode());
            }
            builder.append('\n');
        }
    }

    private void putOrigDest(StringBuilder builder, Ip ip, Protocol protocol) {
        String src = inventory.get(ip.getIp_src());
        String dst = inventory.get(ip.getIp_dst());
        builder
            .append(src)
            .append(" -[")
            .append(colorPalette.getColor(protocol.id()))
            .append("]> ")
            .append(dst)
            .append(" : ");
    }

    public String getUml() {
        if (result == null) {
            builder.append("@enduml\n");
            result = builder.toString();
            builder = null;
        }
        return result;
    }
}
