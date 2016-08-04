
package com.unionpay.cloudpos.emv;

/**
 * value的长度
 */
class LPositon {

    private int _vL;
    private int _position;

    LPositon(int _vL, int position) {
        this._vL = _vL;
        this._position = position;
    }

    int get_vL() {
        return _vL;
    }

    void set_vL(int _vL) {
        this._vL = _vL;
    }

    int get_position() {
        return _position;
    }

    void set_position(int _position) {
        this._position = _position;
    }
}
