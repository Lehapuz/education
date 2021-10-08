package main;

import main.model.Matter;
import main.model.MatterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Controller
public class DefaultController {

    @Autowired
    MatterRepository matterRepository;

    @RequestMapping("/")
    public String index(Model model) {
        Iterable<Matter> matterIterable = matterRepository.findAll();
        ArrayList<Matter> matters = new ArrayList<>();
        for (Matter matter : matterIterable) {
            matters.add(matter);
        }
        model.addAttribute("matters", matters);
        model.addAttribute("mattersCount", matters.size());
        return "index";
    }
}
