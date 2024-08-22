package com.example.enums;

public enum CommonCharEnum {
    MINUS("-","分隔符")
    ;

    public final String value;
    public final String desc;

    CommonCharEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static CommonCharEnum getByValue(String value) {
        for (CommonCharEnum commonCharEnum : CommonCharEnum.values()) {
            if (commonCharEnum.value.equals(value)) {
                return commonCharEnum;
            }
        }
        return null;
    }


    public String getValue() {
        return value;
    }

}
