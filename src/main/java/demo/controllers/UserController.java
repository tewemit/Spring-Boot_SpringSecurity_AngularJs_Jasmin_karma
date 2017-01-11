package demo.controllers;

import com.google.gson.Gson;
import demo.models.User;
import demo.models.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to test interactions with the MySQL database using the UserDao class.
 *
 * @author Tewelle
 */
@RestController
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);
  private static Gson gson = new Gson();

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * /create  --> Create a new user and save it in the database.
   * 
   * @param InUser The new user to be created
   * @return The details of the user created.
   */
  @RequestMapping("/createUser")

  public ResponseEntity<?> create(@RequestBody User InUser, HttpServletRequest request, HttpServletResponse response) {
  //public String create(String email, String name,String password) {
    User user = null;
    Map<String,String> error =new HashMap<>();
    try {
      user = new User();
      user.setPassword(InUser.getPassword());
      user.setName(InUser.getName());
      user.setEmail(InUser.getEmail());
      user.setUsername(InUser.getUsername());
      user = userDao.save(user);
    }
    catch (Exception ex) {
      log.warn("User not created for the following reason: " + ex.toString());
      error.put("error","not created. Reason : " +ex.toString());
      return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    if(user==null)
    {
      return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    response.setHeader("Location", request.getRequestURL().append("/").append(user.getId()).toString());
    return new ResponseEntity<>(user,HttpStatus.CREATED);
  }
  
  /**
   * /delete  --> Delete the user having the passed id.
   * 
   * @param id The id of the user to delete
   * @return A string describing if the user is succesfully deleted or not.
   */
  @RequestMapping("/deleteuser")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public String delete(@RequestParam long id) {
    try {
      userDao.delete(id);
    }
    catch (Exception ex) {
      return "Error deleting the user: " + ex.toString();
    }

    return "User succesfully deleted!";
  }
  
  /**
   * /get-by-email  --> Return the id for the user having the passed email.
   * 
   * @param email The email to search in the database.
   * @return The user id or a message error if the user is not found.
   */
  @RequestMapping("/getByEmail")
  @ResponseBody
  public String getByEmail(String email) {
    String userId;
    email= email==null?"test@test.com":email;
    User user;
    try {
      user = userDao.findByEmail(email);
      userId = String.valueOf(user.getId());
    }
    catch (Exception ex) {
      return "User not found";
    }
    return gson.toJson(user);//"{'id':" + userId+"}";
  }

  /**
   * @Description The Currently Authenticated User
   * @param user
   * @return
   */
  @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

  /**
   *
   * @param id
   * @return
   */
  @RequestMapping("/userById")
  public ResponseEntity<?> getById(@RequestParam("id") Long id) {
    User user;
    Map<String,String> message =new HashMap<>();
    try {
      user = userDao.findOne(id);
    }
    catch (Exception ex) {

      message.put("error","User not found with Id:" +id);
      return new ResponseEntity<>(gson.toJson(message),HttpStatus.NOT_FOUND);
    }
    if(user==null) {
      message.put("error","User not found with Id:" +id);
      return new ResponseEntity<>(gson.toJson(message), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(gson.toJson(user),HttpStatus.OK);
  }

  /**
   *
   * @param username
   * @param password
   * @return
   */
  @RequestMapping("/userlogin")
  @ResponseBody
  public String login(@RequestParam(value="username") String username,@RequestParam(value="password") String password) {
    User user;
    try {
      user = userDao.findByUsernameAndPassword(username,password);

    }
    catch (Exception ex) {
      Map<String,String> error =new HashMap<>();
      error.put("error","User not found");
      return gson.toJson(error);
    }
    return gson.toJson(user);
  }

/*
  @RequestMapping("/login")
  public String login() {
    Map<String, String> user = new HashMap<>();
    user.put("name","testuser");
    user.put("password","test123");
    return gson.toJson(user);
  }*/
/*
  @RequestMapping(value = "/logout")
  public Boolean logout(HttpServletRequest request)
  {
    SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    HttpSession session= request.getSession(false);
    SecurityContextHolder.clearContext();
    if(session != null) {
      session.invalidate();
    }
    return true;
  }*/

  /**
   * /update  --> Update the email and the name for the user in the database 
   * having the passed id.
   * 
   * @param user The user to be updated.
   * @return A Json form of the user is if succesfully updated.
   */
  @RequestMapping("/update")
  @ResponseBody
  public String updateUser(@RequestBody User user) {
    User newuser;

    try {
      newuser = user;
      userDao.save(newuser);
    }
    catch (Exception ex) {
      return "Error updating the user: " + ex.toString();
    }
    return gson.toJson(user);
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private UserDao userDao;
  
} // class UserController
