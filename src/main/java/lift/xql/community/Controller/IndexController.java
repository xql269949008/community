package lift.xql.community.Controller;

import lift.xql.community.dto.QuestionDTO;
import lift.xql.community.mapper.QuestionMapper;
import lift.xql.community.mapper.UserMapper;
import lift.xql.community.model.Question;
import lift.xql.community.model.User;
import lift.xql.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model) {
        Cookie[] cookies =  request.getCookies();
        if(cookies != null && cookies.length != 0)
        for (Cookie cookie: cookies){
            if ("token".equals(cookie.getName())){
//            if (cookie.getName().equals("token")){
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
                if (user != null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }

        List<QuestionDTO> questionsList = questionService.list();
        model.addAttribute("questions", questionsList);
        return "index";
    }
}
