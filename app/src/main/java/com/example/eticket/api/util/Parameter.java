package com.example.eticket.api.util;

public class Parameter {
    private ParameterType type;
    private int ind;
    private String aValue;
    private boolean isEncoded;
    private String defaultValue;

    public Parameter() {

    }

    public Parameter(String type, int pos, String aValue) {
        this(ParameterType.valueOf(type), pos, aValue);
    }

    public Parameter(ParameterType type, String aValue) {
        this(type, 0, aValue);
    }

    public Parameter(ParameterType type, int ind, String aValue) {
        this.type = type;
        this.ind = ind;
        this.aValue = aValue;
    }

    public Parameter(ParameterType type, int ind, String aValue,
                     boolean isEncoded, String defaultValue) {
        this.type = type;
        this.ind = ind;
        this.aValue = aValue;
        this.isEncoded = isEncoded;
        this.defaultValue = defaultValue;
    }

    public int getIndex() {
        return ind;
    }

    public String getName() {
        return aValue;
    }

    public void setName(String value) {
        aValue = value;
    }

    public ParameterType getType() {
        return type;
    }

    public void setType(String stype) {
        type = ParameterType.valueOf(stype);
    }

    public boolean isEncoded() {
        return isEncoded;
    }

    public void setEncoded(boolean encoded) {
        isEncoded = encoded;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String dValue) {
        defaultValue = dValue;
    }
}

