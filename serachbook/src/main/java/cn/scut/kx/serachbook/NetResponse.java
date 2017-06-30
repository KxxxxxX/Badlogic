package cn.scut.kx.serachbook;

/**
 * Created by DELL on 2017/1/19.
 */

public class NetResponse {

    private int code;          //响应码
    private Object message;    //响应详情

    public NetResponse(int code, Object message) {
        this.code = code;
        this.message = message;
    }

    public NetResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
