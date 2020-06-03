package cn.yaolianhua.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GithubUser implements IAuthUser, Serializable {
    private String gist_url;
    private String repos_url;
    private String following_url;
    private String twitter_username;
    private String bio;
    private String create_at;
    private String login;
    private String type;
    private String blog;
    private String subscriptions_url;
    private String updated_at;
    private boolean site_admin;
    private String company;
    private String id;
    private String public_repos;
    private String gravatar_id;
    private String email;
    private String organizations_url;
    private String hireable;
    private String starred_url;
    private String followers_url;
    private String public_gists;
    private String url;
    private String received_events_url;
    private String followers;
    private String avatar_url;
    private String events_url;
    private String html_url;
    private String following;
    private String name;
    private String location;
    private String node_id;




}
