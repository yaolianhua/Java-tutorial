package cn.yaolianhua.github.login;

import cn.yaolianhua.common.config.IAuthConfig;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/github")
public class OauthController {
    @RequestMapping("/render")
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @RequestMapping("/callback")
    public Object login(AuthCallback callback) {
        AuthRequest authRequest = getAuthRequest();
        return authRequest.login(callback);
    }

    private AuthRequest getAuthRequest() {
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId("e30a1885d607872bd340")
                .clientSecret("f3fbd9760ea56f1650361b2f1fb29114969e71dd")
                .redirectUri("http://localhost:8080/github/oauth/authorize")
                .build());
    }
    @GetMapping("/oauth/authorize")
    @ResponseBody
    public Object index(){
        return "success index";
    }
    @GetMapping("/loginPage")
    public Object loginPage(){
        return "github/login";
    }
}
