
package com.peu.springboot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.peu.springboot.model.User;
import com.peu.springboot.service.UserService;
import com.peu.springboot.util.CustomErrorType;

@RestController
@RequestMapping("/api")
public class RestApiController {

  public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

  @Autowired
  UserService userService; // Service which will do all data retrieval/manipulation work

  // -------------------Retrieve All Users---------------------------------------------

  @RequestMapping(value = "/users/", method = RequestMethod.GET)
  public ResponseEntity<List<User>> listAllUsers() {
    List<User> users = userService.findAllUsers();
    if (users.isEmpty()) {
      return new ResponseEntity(HttpStatus.NO_CONTENT);
      // You many decide to return HttpStatus.NOT_FOUND
    }
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  // -------------------Retrieve Single User------------------------------------------

  @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
  public ResponseEntity<?> getUser(@PathVariable("id") long id) {
    logger.info("Fetching User with id {}", id);
    User user = userService.findById(id);
    if (user == null) {
      logger.error("User with id {} not found.", id);
      return new ResponseEntity(new CustomErrorType("User with id " + id + " not found"),
          HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  // -------------------Create a User-------------------------------------------

  @RequestMapping(value = "/user/add/", method = RequestMethod.POST)
  public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
    logger.info("Creating User : {}", user);

    if (userService.isUserExist(user)) {
      logger.error("Unable to create. A User with name {} already exist", user.getName());
      return new ResponseEntity(
          new CustomErrorType(
              "Unable to create. A User with name " + user.getName() + " already exist."),
          HttpStatus.CONFLICT);
    }
    userService.add(user);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  // ------------------- Update a User ------------------------------------------------

  @RequestMapping(value = "/user/update/{id}", method = RequestMethod.PUT)
  public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) {
    logger.info("Updating User with id {}", id);

    User currentUser = userService.findById(id);

    if (currentUser == null) {
      logger.error("Unable to update. User with id {} not found.", id);
      return new ResponseEntity(
          new CustomErrorType("Unable to upate. User with id " + id + " not found."),
          HttpStatus.NOT_FOUND);
    }

    currentUser.setName(user.getName());
    currentUser.setPassword(user.getPassword());
    currentUser.setPhone(user.getPhone());

    userService.updateUser(currentUser);
    return new ResponseEntity<>(currentUser, HttpStatus.OK);
  }

  // ------------------- Delete a User-----------------------------------------

  @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
    logger.info("Fetching & Deleting User with id {}", id);

    User user = userService.findById(id);
    if (user == null) {
      logger.error("Unable to delete. User with id {} not found.", id);
      return new ResponseEntity(
          new CustomErrorType("Unable to delete. User with id " + id + " not found."),
          HttpStatus.NOT_FOUND);
    }
    userService.deleteUserById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  // -------------------User Log In-------------------------------------------

  @RequestMapping(value = "/user/login/", method = RequestMethod.POST)
  public ResponseEntity<?> login(@RequestBody User user, UriComponentsBuilder ucBuilder) {
    logger.info("logging in User : {}", user);

    user = userService.login(user.getName(), user.getPassword());
    if (user == null) {
      logger.error("User with id {} not found.", user.getName());
      return new ResponseEntity(
          new CustomErrorType("Please enter Correct Username and Password for " + user.getName()),
          HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  // -------------------User Log Off-------------------------------------------

  @RequestMapping(value = "/user/logoff/", method = RequestMethod.POST)
  public ResponseEntity<?> logoff(@RequestBody User user, UriComponentsBuilder ucBuilder) {
    logger.info("logging off User : {}", user);

    if (userService.isUserExist(user)) {
      logger.error("Unable to create. A User with name {} already exist", user.getName());
      return new ResponseEntity(
          new CustomErrorType(
              "Unable to create. A User with name " + user.getName() + " already exist."),
          HttpStatus.CONFLICT);
    }
    userService.add(user);

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
    return new ResponseEntity<String>(headers, HttpStatus.CREATED);
  }

}
