package demo.controllers;

import com.google.gson.Gson;
import demo.models.Faq;
import demo.models.FaqDao;
import demo.services.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to test interactions with the MySQL database using the UserDao class.
 *
 * @author netgloo
 */
@RestController
public class FaqController {

// ------------------------
  // PRIVATE FIELDS
  // ------------------------
private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);
  @Autowired
  private FaqDao faqDao;

  private static Gson gson = new Gson();

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  

  /**
   * /delete  --> Delete the user having the passed id.
   * 
   * @param id The id of the user to delete
   * @return A string describing if the user is succesfully deleted or not.
   */
  @RequestMapping("/deleteFaq")
  @ResponseBody
  public String deleteFaq(long id) {
    Map<String,String> error =new HashMap<>();
    try {
      Faq faq = new Faq(id);
      faqDao.delete(id);
    }
    catch (Exception ex) {
      error.put("message","Faqs not found ");
      return gson.toJson(error);
    }
    error.put("message","user successfully deleted");
    return gson.toJson(error);
  }


  /**
   *
   * @param
   * @return
   */
  @RequestMapping("/faqs")
  @ResponseBody
  public String getFaqs() {
    Iterable<Faq> faqs;
    try {
      faqs = faqDao.findAll();
    }
    catch (Exception ex) {
      Map<String,String> error =new HashMap<>();
      error.put("error","Faqs not found ");
      return gson.toJson(error);
    }
    log.info("fetched some faqs: " + faqs.toString());
    return gson.toJson(faqs);
  }
  /**
   *
   * @param
   * @return
   */
  @RequestMapping("/addFaq")
  @ResponseBody
  public String addFaq(@RequestParam("category") String category,@RequestParam("question") String question,@RequestParam("answer") String answer) {
    Faq faq = new Faq();
    faq.setAnswer(answer);
    faq.setCategory(category);
    faq.setQuestion(question);
  System.out.print(faq.getAnswer());
    try {
    faqDao.save(faq);
    }
    catch (Exception ex) {
      Map<String,String> error =new HashMap<>();
      error.put("error","Faq not added ");
      return gson.toJson(error);
    }
    log.info("Faq saved successfully: " + faq.toString());
    return gson.toJson(faq);
  }

  /**
   *
   * @param
   * @return
   */
  @RequestMapping("/saveFaq")
  @ResponseBody
  public Faq addFaq(@RequestBody Faq Infaq) {
    Faq faq = new Faq();
    System.out.print(faq.getAnswer());

    try {
      faq = faqDao.save(Infaq);
    }
    catch (Exception ex) {
      Map<String,String> error =new HashMap<>();
      error.put("error","Faq not added ");
      return faq;
    }
    log.info("Faq saved successfully: " + faq.toString());
    return faq;
  }


  /**
   *
   * @param
   * @return
   */
  @RequestMapping("/getFaq")
  @ResponseBody
  public String getFaq(@RequestParam("id") Long id) {
    Faq faq = new Faq();

    try {
    faq =  faqDao.findOne(id);
    }
    catch (Exception ex) {
      Map<String,String> error =new HashMap<>();
      error.put("error","Faq not found ");
      return gson.toJson(error);
    }
    log.info("Faq retrieved successfully: " + faq.toString());
    return gson.toJson(faq);
  }


  /**
   *
   * @param
   * @return
   */
  @RequestMapping("/updateFaq")
  @ResponseBody
  public String updateFaq(@RequestParam("id") Long id,@RequestParam("category") String category,@RequestParam("question") String question,@RequestParam("answer") String answer) {
    Faq faq = new Faq();
    faq.setAnswer(answer);
    faq.setCategory(category);
    faq.setQuestion(question);
    faq.setId(id);
    log.info("Faq updated successfully: " + faq.getId() + faq.getQuestion());
    try {
      faq =  faqDao.saveAndFlush(faq);
    }
    catch (Exception ex) {
      Map<String,String> error =new HashMap<>();
      error.put("error","Faq not updated ");
      return gson.toJson(error);
    }
    log.info("Faq updated successfully: " + faq.getId());
    return gson.toJson(faq);
  }


} // class UserController
