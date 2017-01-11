package demo.controllers;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {


  @RequestMapping("/index2")
  @ResponseBody
  public String index() {
    return "We are so happy that you landed here!" +
        "<a href='http://hmj.se'>HMJ</a> :)";
  }


}
