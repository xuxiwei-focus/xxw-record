package com.example.enums;

public enum StageProcessedResultEnum {
    IDEMPOTENT_FAIL("0", "失败"),
    SUCCESS("1", "成功"),

    WRITE_USER_RIGHTS_RECORD_DB_FAIL("2","写权益记录失败"),
    SKIP_ALL("3", "跳过"),
    WRITE_USER_RIGHTS_FLOW_DB_FAIL("4","WRITE_USER_RIGHTS_FLOW_DB_FAIL");

    private String result;
    private String desc;
    StageProcessedResultEnum(String result, String desc) {
        this.result = result;
        this.desc = desc;
    }

    public boolean isSuccess() {
        return this.result.equals("1");
    }
}
