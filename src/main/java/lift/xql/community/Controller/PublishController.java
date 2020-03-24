package lift.xql.community.Controller;

import lift.xql.community.dto.QuestionDTO;
import lift.xql.community.mapper.QuestionMapper;
import lift.xql.community.model.Question;
import lift.xql.community.model.User;
import lift.xql.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    // /{id}  是通过下面的@PathVariable中的值来获取    如果id不为空  则进入到这个方法
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        return "publish";
    }

//    @Autowired
//    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Integer id,
            HttpServletRequest request,
            Model model){

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if(title == null || title == ""){
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if(description == null || description == ""){
            model.addAttribute("error", "问题不能为空");
            return "publish";
        }
        if(tag == null || tag == ""){
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

//        User user = null;
//        Cookie[] cookies =  request.getCookies();
//        if(cookies != null && cookies.length != 0)
//        for (Cookie cookie: cookies){
//            if ("token".equals(cookie.getName())){
////            if (cookie.getName().equals("token")){
//                String token = cookie.getValue();
//                user = userMapper.findByToken(token);
//                if (user != null){
//                    request.getSession().setAttribute("user",user);
//                }
//                break;
//            }
//        }
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
