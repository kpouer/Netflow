package com.kpouer.netflow.model;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorPalette {
    private final Map<String, String> colorMap = new HashMap<>();
    private int colorIndex;

    public String getColor(String name) {
        var color = colorMap.get(name);
        if (color == null) {
            var tmpColor = PALETTE[colorIndex];
            color = String.format("#%02x%02x%02x", tmpColor.getRed(), tmpColor.getGreen(), tmpColor.getBlue());
            colorMap.put(name, color);
            colorIndex = (colorIndex + 1) % PALETTE.length;
        }
        return color;
    }

    private static final Color[] PALETTE = {
        new Color(0x000000), // black
        new Color(0x0000ff), // blue
        new Color(0x00ff00), // green
        new Color(0x00ffff), // cyan
        new Color(0xff0000), // red
        new Color(0xff00ff), // magenta
        new Color(0xffa500), // orange
        new Color(0xffff00), // yellow
        new Color(0x800000), // maroon
        new Color(0x808000), // olive
        new Color(0x008000), // green
        new Color(0x008080), // teal
        new Color(0x000080), // navy
        new Color(0x800080), // purple
        new Color(0x808080), // gray
        new Color(0xc0c0c0), // silver
        new Color(0x008000), // green
        new Color(0x008080), // teal
        new Color(0x000080), // navy
        new Color(0x800080), // purple
        new Color(0x808080), // gray
        new Color(0xc0c0c0), // silver
    };
}
