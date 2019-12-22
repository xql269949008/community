package lift.xql.community.Controller;

import lift.xql.community.dto.AccessTokenDTO;
import lift.xql.community.dto.GithubUser;
import lift.xql.community.mapper.UserMapper;
import lift.xql.community.model.User;
import lift.xql.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("{github.client.secret}")
    private String clientSecret;
    @Value("{github.redirect.uri}")
    private String redirectUri;


    @Autowired(required = false)
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_id(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToker = githubProvider.getAccessToker(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToker);
        if(githubUser != null){
            User user = new User();
            final String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));
            // 登录成功,写入cookie和session
//            request.getSession().setAttribute("user", githubUser);
            return "/index";
        }else {
            // 登陆失败，请重新登陆
            return "/index";
        }
    }
}
