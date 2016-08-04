
package com.unionpay.cloudpos.emv;

class Tlv {

    /** 子域Tag标签 */
    private String tag;

    /** 子域取值的长度 */
    private int length;

    /** 子域取值 */
    private String value;

    Tlv(String tag, int length, String value) {
        this.length = length;
        this.tag = tag;
        this.value = value;
    }

    String getTag() {
        return tag;
    }

    void setTag(String tag) {
        this.tag = tag;
    }

    int getLength() {
        return length;
    }

    void setLength(int length) {
        this.length = length;
    }

    String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "tag=[" + this.tag + "]," + "length=[" + this.length + "]," + "value=[" + this.value
                + "]";
    }
}
