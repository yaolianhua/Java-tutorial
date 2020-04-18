package cn.yaolianhua.netty.prorocoltcp;

/**
 * @author yaolianhua789@gmail.com
 * @date 2020-03-25 21:29
 **/
public class MessageProtocol {
    public MessageProtocol(int len, byte[] content) {
        this.len = len;
        this.content = content;
    }

    private int len;
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
