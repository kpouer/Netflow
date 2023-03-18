package com.kpouer.netflow.model.diameter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kpouer.netflow.model.protocol.AbstractProtocol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonDeserialize(using = DiameterDeserializer.class)
public class Diameter extends AbstractProtocol {
    private int code;
    private int resultCode;

    public String getCommandName() {
        return switch (code) {
            case 265 -> "AAR";
            case 268 -> "DER";
            case 274 -> "ASR";
            case 271 -> "ACR";
            case 272 -> "CCR";
            case 257 -> "CER";
            case 280 -> "DWR";
            case 282 -> "DPR";
            case 258 -> "RAR";
            case 275 -> "STR";
            case 283 -> "UAR";
            case 284 -> "SAR";
            case 285 -> "LIR";
            case 286 -> "MAR";
            case 287 -> "RTR";
            case 288 -> "PPR";
            case 300 -> "UAR";
            case 301 -> "SAR";
            case 302 -> "LIR";
            case 303 -> "MAR";
            case 304 -> "RTR";
            case 305 -> "PPR";
            case 306 -> "UDR";
            case 307 -> "PUR";
            case 308 -> "SNR";
            case 309 -> "PNR";
            case 310 -> "BIR";
            case 311 -> "MPR";
            case 316 -> "ULR";
            case 317 -> "CLR";
            case 318 -> "AIR";
            case 319 -> "IDR";
            case 320 -> "DSR";
            case 321 -> "PER";
            case 323 -> "NR";
            case 8388620 -> "PLR";
            case 8388622 -> "RIR";
            case 260 -> "AMR";
            case 262 -> "HAR";
            case 8388718 -> "CIR";
            case 8388719 -> "RIR";
            case 8388726 -> "NIR";
            default -> "Unknown (" + code + ")";
        };
    }

    @Override
    public boolean isRequest() {
        return resultCode == 0;
    }

    @Override
    public String id() {
        return "diameter";
    }
}
