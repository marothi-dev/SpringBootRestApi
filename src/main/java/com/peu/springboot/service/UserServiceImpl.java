
package com.peu.springboot.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.peu.springboot.model.User;



@Service("userService")
public class UserServiceImpl implements UserService {

  private static final AtomicLong counter = new AtomicLong();

  private static List<User> users;

  static {
    users = populateDummyUsers();
  }

  public List<User> findAllUsers() {
    return users;
  }

  public User findById(long id) {
    for (User user : users) {
      if (user.getId() == id) {
        return user;
      }
    }
    return null;
  }

  @Override
  public User findByName(String name) {
    for (User user : users) {
      if (user.getName().equalsIgnoreCase(name)) {
        return user;
      }
    }
    return null;
  }

  @Override
  public void add(User user) {
    user.setId(counter.incrementAndGet());
    users.add(user);
  }

  @Override
  public void updateUser(User user) {
    int index = users.indexOf(user);
    users.set(index, user);
  }

  @Override
  public void deleteUserById(long id) {

    for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
      User user = iterator.next();
      if (user.getId() == id) {
        iterator.remove();
      }
    }
  }


  @Override
  public User login(String name, String password) {
    for (User user : users) {
      if (user.getName().equalsIgnoreCase(name) && user.getPassword().equalsIgnoreCase(password)) {
        user.setToken(Long.toString(counter.incrementAndGet()));
        return user;
      }
    }
    return null;
  }

  @Override
  public void logoff(long id) {
    for (User user : users) {
      if (user.getId() == id) {
        user.setToken(null);
      }
    }
  }

  public boolean isUserExist(User user) {
    return findByName(user.getName()) != null;
  }

  public void deleteAllUsers() {
    users.clear();
  }

  private static List<User> populateDummyUsers() {
    List<User> users = new ArrayList<>();
    User user = new User();
    user.setId(counter.incrementAndGet());
    user.setName("Simon");
    user.setPassword("pass");
    user.setPhone("014");
    users.add(user);

    user = new User();
    user.setId(counter.incrementAndGet());
    user.setName("Bopape");
    user.setPassword("pass");
    user.setPhone("0144545");
    users.add(user);

    user = new User();
    user.setId(counter.incrementAndGet());
    user.setName("Marothi");
    user.setPassword("pass");
    user.setPhone("0144545");
    users.add(user);

    return users;
  }



}
