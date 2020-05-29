package cn.yaolianhua.common.config;

public interface IAuthBase {
    /**
     * authorize api url
     * @return url
     */
    String authorize();

    /**
     * accessToken api url
     * @return url
     */
    String accessToken();

    /**
     * userInfo api url
     * @return url
     */
    String userInfo();
}
