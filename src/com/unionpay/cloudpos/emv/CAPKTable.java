
package com.unionpay.cloudpos.emv;

import android.text.TextUtils;

class CAPKTable
{
    private int _id;
    private String rid; // Registered Application Provider Identifier
    private byte capki; // Certificate Authority Key Index
    private byte hashIndex; // Hash Algorithm Indicator
    private byte arithIndex; // Certificate Authority Key Algorithm Indicator
    private String modul; // Certificate Authority Key Modulus
    private String exponent; // Certificate Authority Key Exponent
    private String checkSum; // Certificate Authority Key Check Sum
    private String expiry; // Certificate Expiration Date

    CAPKTable()
    {
        init();
    }

    void init()
    {
        _id = -1;
        rid = "";
        capki = 0;
        hashIndex = 0;
        arithIndex = 0;
        modul = "";
        exponent = "";
        checkSum = "";
        expiry = "";
    }

    // _id
    int getId()
    {
        return _id;
    }

    void setId(int id)
    {
        this._id = id;
    }

    // rid
    String getRID()
    {
        return rid;
    }

    void setRID(String rid)
    {
        this.rid = rid;
    }

    // capki
    byte getCapki()
    {
        return capki;
    }

    void setCapki(byte capki)
    {
        this.capki = capki;
    }

    // hashIndex
    byte getHashIndex()
    {
        return hashIndex;
    }

    void setHashIndex(byte hashIndex)
    {
        this.hashIndex = hashIndex;
    }

    // arithIndex
    byte getArithIndex()
    {
        return arithIndex;
    }

    void setArithIndex(byte arithIndex)
    {
        this.arithIndex = arithIndex;
    }

    // modul
    String getModul()
    {
        return modul;
    }

    void setModul(String modul)
    {
        this.modul = modul;
    }

    int getModulLength()
    {
        return modul.length();
    }

    // exponent
    String getExponent()
    {
        return exponent;
    }

    void setExponent(String exponent)
    {
        this.exponent = exponent;
    }

    int getExponentLength()
    {
        return exponent.length();
    }

    // checksum
    String getChecksum()
    {
        return checkSum;
    }

    void setChecksum(String checksum)
    {
        this.checkSum = checksum;
    }

    // expiry
    String getExpiry()
    {
        return expiry;
    }

    void setExpiry(String expiry)
    {
        this.expiry = expiry;
    }

    byte[] getDataBuffer()
    {
        // byte[] dataOut = new byte[292];
        // System.arraycopy(StringUtil.hexString2bytes(rid), 0, dataOut, 0,
        // rid.length()/2);
        // dataOut[5] = capki;
        // dataOut[6] = hashIndex;
        // dataOut[7] = arithIndex;
        // System.arraycopy(NumberUtil.intToByte4LowFirst(modul.length()/2), 0,
        // dataOut, 8, 4);
        // System.arraycopy(StringUtil.hexString2bytes(modul), 0, dataOut, 12,
        // modul.length()/2);
        // dataOut[260] = (byte)(exponent.length()/2);
        // System.arraycopy(StringUtil.hexString2bytes(exponent), 0, dataOut,
        // 261, exponent.length()/2);
        // System.arraycopy(StringUtil.hexString2bytes(checkSum), 0, dataOut,
        // 264, checkSum.length()/2);
        // System.arraycopy(expiry.getBytes(), 0, dataOut, 284,
        // expiry.length());

        byte[] data = new byte[512];
        int offset = 0;
        data[offset] = (byte) 0x9F;
        data[offset + 1] = 0x06;
        data[offset + 2] = (byte) (rid.length() / 2);
        System.arraycopy(StringUtil.hexString2bytes(rid), 0, data, offset + 3, rid.length() / 2);
        offset += (3 + rid.length() / 2);

        data[offset] = (byte) 0x9F;
        data[offset + 1] = 0x22;
        data[offset + 2] = 0x01;
        data[offset + 3] = capki;
        offset += 4;

        data[offset] = (byte) 0xDF;
        data[offset + 1] = 0x05;
        data[offset + 2] = (byte) expiry.length();
        System.arraycopy(expiry.getBytes(), 0, data, offset + 3, expiry.length());
        offset += (3 + expiry.length());

        data[offset] = (byte) 0xDF;
        data[offset + 1] = 0x06;
        data[offset + 2] = 0x01;
        data[offset + 3] = hashIndex;
        offset += 4;

        data[offset] = (byte) 0xDF;
        data[offset + 1] = 0x07;
        data[offset + 2] = 0x01;
        data[offset + 3] = arithIndex;
        offset += 4;

        if (!TextUtils.isEmpty(modul))
        {
            data[offset] = (byte) 0xDF;
            data[offset + 1] = 0x02;
            data[offset + 2] = (byte) 0x81;
            data[offset + 3] = (byte) (modul.length() / 2 & 0xFF);
            System.arraycopy(StringUtil.hexString2bytes(modul), 0, data, offset + 4,
                    modul.length() / 2);
            offset += (4 + modul.length() / 2);
        }
        data[offset] = (byte) 0xDF;
        data[offset + 1] = 0x04;
        data[offset + 2] = (byte) (exponent.length() / 2);
        System.arraycopy(StringUtil.hexString2bytes(exponent), 0, data, offset + 3,
                exponent.length() / 2);
        offset += (3 + exponent.length() / 2);

        if (checkSum != null && checkSum.length() > 0)
        {
            data[offset] = (byte) 0xDF;
            data[offset + 1] = 0x03;
            data[offset + 2] = (byte) (checkSum.length() / 2);
            System.arraycopy(StringUtil.hexString2bytes(checkSum), 0, data, offset + 3,
                    checkSum.length() / 2);
            offset += (3 + checkSum.length() / 2);
        }

        byte[] dataOut = new byte[offset];
        System.arraycopy(data, 0, dataOut, 0, dataOut.length);

        return dataOut;
    }
}
