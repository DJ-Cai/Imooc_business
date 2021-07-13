package net.dongjian.lib_network.okhttp.exception;

/*
 * 自定义异常类,返回ecode,emsg到业务层
 * 主要是方便将错误定位到okhttp的网络框架来
 */
public class OkHttpException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * the server return code
     */
    private int ecode;

    /**
     * the server return error message
     */
    private Object emsg;

    /**
     *
     * @param ecode 错误码
     * @param emsg 错误类型
     */
    public OkHttpException(int ecode, Object emsg) {
        this.ecode = ecode;
        this.emsg = emsg;
    }

    public int getEcode() {
        return ecode;
    }

    public Object getEmsg() {
        return emsg;
    }
}