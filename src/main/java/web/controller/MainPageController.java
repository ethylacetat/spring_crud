package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.User;
import web.util.Page;

@Controller
@RequestMapping(path = "/")
public class MainPageController {
    @GetMapping
    public String getMainPage() {
        return "./index";
    }
}
