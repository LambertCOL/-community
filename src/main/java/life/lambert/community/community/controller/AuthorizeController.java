package life.lambert.community.community.controller;

import life.lambert.community.community.dto.AccessTokenDTO;
import life.lambert.community.community.dto.GithubUser;
import life.lambert.community.community.mapper.UserMapper;
import life.lambert.community.community.model.User;
import life.lambert.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    public GithubProvider githubProvider;

    @Autowired
    public UserMapper userMapper;

    @Value("${github.client.id}")
    public String clientId;
    @Value("${github.client.secret}")
    public String clientSecret;
    @Value("${github.redirect.uri}")
    public String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);

        //登录成功
        if (null != githubUser) {
            //加入数据库
            User user = new User();
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setName(githubUser.getName());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            String token = String.valueOf(UUID.randomUUID());
            user.setToken(token);
            userMapper.insert(user);

            //加入session
            //request.getSession().setAttribute("user", githubUser);
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }
}
