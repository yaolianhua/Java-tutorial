package cn.yaolianhua.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.zhyd.oauth.enums.AuthUserGender;
import me.zhyd.oauth.model.AuthToken;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class IAuthUser implements Serializable {
    private String uuid;
    private String username;
    private String nickname;
    private String avatar;
    private String blog;
    private String company;
    private String location;
    private String email;
    private String remark;
    private AuthUserGender gender;
    private String source;
    private AuthToken token;

    private IAuthUser() { }

    public IAuthUser(String uuid, String username, String nickname, String avatar, String blog, String company, String location, String email, String remark, AuthUserGender gender, String source, AuthToken token) {
        this.uuid = uuid;
        this.username = username;
        this.nickname = nickname;
        this.avatar = avatar;
        this.blog = blog;
        this.company = company;
        this.location = location;
        this.email = email;
        this.remark = remark;
        this.gender = gender;
        this.source = source;
        this.token = token;
    }
}
