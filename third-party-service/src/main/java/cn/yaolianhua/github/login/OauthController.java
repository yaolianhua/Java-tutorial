package cn.yaolianhua.github.login;

import cn.yaolianhua.common.config.IAuthConfig;
import cn.yaolianhua.common.model.IAuthCallback;
import cn.yaolianhua.common.request.GithubRequest;
import cn.yaolianhua.common.request.IAuthRequest;
import cn.yaolianhua.common.utils.StringTools;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/github")
public class OauthController {
    @RequestMapping("/render")
    public void renderAuth(HttpServletResponse response) throws IOException {
        IAuthRequest authRequest = getAuthRequest();
        response.sendRedirect(authRequest.authorize(StringTools.getUUID()));
    }

    @RequestMapping("/callback")
    public Object login(IAuthCallback callback) {
        IAuthRequest authRequest = getAuthRequest();
        return authRequest.login(callback);
    }

    private IAuthRequest getAuthRequest() {
        return new GithubRequest(IAuthConfig.builder()
                .clientId("")
                .clientSecret("")
                .redirectUri("")
                .build());
    }
    @GetMapping("/oauth/authorize")
    @ResponseBody
    public Object index(IAuthCallback callback){
        System.out.println(callback);
        return getAuthRequest().login(callback);
    }
    @GetMapping("/loginPage")
    public Object loginPage(){
        return "github/login";
    }
}
