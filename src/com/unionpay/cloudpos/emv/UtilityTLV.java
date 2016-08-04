
package com.unionpay.cloudpos.emv;

import java.util.Arrays;

class UtilityTLV
{
    private static byte[] getLenBytes(int len)
    {
        byte[] lengthBuf = null;
        int divided = len;
        int bytes = 0;
        while (divided > 0)
        {
            byte[] tmpBuf = null;
            byte lenByte = (byte) (divided % 256);
            divided /= 256;
            bytes++;

            tmpBuf = new byte[bytes];
            if (lengthBuf != null)
            {
                System.arraycopy(lengthBuf, 0, tmpBuf, 1, lengthBuf.length);
            }
            tmpBuf[0] = lenByte;

            lengthBuf = tmpBuf;
        }

        return lengthBuf;
    }

    private static int getLength(byte[] lengthBuf)
    {
        long len = 0;
        for (int i = 0; i < lengthBuf.length; i++)
        {
            len = len * 256 + (lengthBuf[i] & 0xff);
            // System.out.println("len:"+len);
        }
        return (int) len;
    }

    static byte[] addTlvData(byte[] oldData, byte[] tag, byte[] data)
    {
        byte[] tlv = null;
        byte[] len = null;
        int offset = 0;

        if (tag == null || data == null || tag.length != 2)
            return null;

        if (data.length > 127)
        {
            byte[] lenBytes = getLenBytes(data.length);
            len = new byte[lenBytes.length + 1];
            System.arraycopy(lenBytes, 0, len, 1, lenBytes.length);
            len[0] = (byte) (0x80 | lenBytes.length);
        }
        else
        {
            len = new byte[1];
            len[0] = (byte) data.length;
        }

        if (oldData == null)
        {
            tlv = new byte[tag.length + len.length + data.length];
            offset = 0;
        }
        else
        {
            tlv = Arrays.copyOf(oldData, oldData.length + tag.length + len.length + data.length);
            offset = oldData.length;
        }
        System.arraycopy(tag, 0, tlv, offset, tag.length);
        offset += tag.length;
        System.arraycopy(len, 0, tlv, offset, len.length);
        offset += len.length;
        System.arraycopy(data, 0, tlv, offset, data.length);
        offset += data.length;

        return tlv;
    }

    static byte[] getTlvData(byte[] tlv, byte[] tag)
    {
        byte[] tagCheck = new byte[tag.length];
        byte[] data = null;
        int offset = 0;
        if (tlv == null || tag == null || tlv.length < 4)
            return null;

        while (offset < tlv.length)
        {
            int lengthLen = 0;
            int dataLen = 0;
            byte[] lengthBuf = null;
            System.arraycopy(tlv, offset, tagCheck, 0, tag.length);
            offset += tag.length;

            if ((tlv[offset] & 0x80) != 0)
            {
                lengthLen = tlv[offset] & 0x7f;
                offset++;
                lengthBuf = Arrays.copyOfRange(tlv, offset, offset + lengthLen);
                dataLen = getLength(lengthBuf);
                offset += lengthLen;
                // System.out.println("lenbuf:"+
                // ByteUtil.arrayToHexStr(lengthBuf));
                // System.out.println("dataLen:"+dataLen);
            }
            else
            {
                dataLen = tlv[offset];
                offset += 1;
            }

            if (Arrays.equals(tagCheck, tag))
            {
                data = Arrays.copyOfRange(tlv, offset, offset + dataLen);
                break;
            }
            offset += dataLen;
        }

        return data;
    }
}
