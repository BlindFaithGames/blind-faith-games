package com.minesweeper.server;

import java.util.Calendar;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletContext;

import com.google.android.c2dm.server.PMF;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

/**
 * 
 * @author Gloria Pozuelo, Javier Álvarez y Gonzalo Benito
 * Clase encargada de almacenar los datos a través de JDO
 *
 */

public class LogDataStore {

  /**
   * Remove this object from the data store.
   */
  public void delete(Long id) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      Log item = pm.getObjectById(Log.class, id);
      pm.deletePersistent(item);
    } finally {
      pm.close();
    }
  }

  /**
     * Find a {@link Log} by id.
     * 
     * @param id the {@link Log} id
     * @return the associated {@link Log}, or null if not found
     */
    @SuppressWarnings("unchecked")
    public Log find(Long id) {
      if (id == null) {
        return null;
      }
      PersistenceManager pm = PMF.get().getPersistenceManager();
      try {
        Query query = pm.newQuery("select from " + Log.class.getName()
            + " where emailAddress=='" + getUserEmail() + "'");
        List<Log> list = (List<Log>) query.execute();
        return list.size() == 0 ? null : list.get(0);
      } catch (RuntimeException e) {
        System.out.println(e);
        throw e;
      } finally {
        pm.close();
      }
    }

@SuppressWarnings("unchecked")
public List<Log> findAll() {
  PersistenceManager pm = PMF.get().getPersistenceManager();
  try {
      Query query = pm.newQuery("select from " + Log.class.getName());
      List<Log> list = (List<Log>) query.execute();
      if (list.size() == 0) {
          list.size();
        }

    return list;
  } catch (RuntimeException e) {
    System.out.println(e);
    throw e;
  } finally {
    pm.close();
  }
}

  /**
   * Persist this object in the datastore.
   */
  public Log update(Log item) {
    //set the user id 
    item.setUserId("1");
    item.setEmailAddress("hola@dummy.com");
    
    if (item.getDueDate() == null) {
        Calendar c = Calendar.getInstance();
        item.setDueDate(c.getTime());
    }
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      pm.makePersistent(item);
      return item;
    } finally {
      pm.close();
    }
  }

  public static String getUserId() {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    return user.getUserId();
  }
  
  public static String getUserEmail() {
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    return user.getEmail();
	  }
	  
  public static void sendC2DMUpdate(String message) {
	    UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
		ServletContext context = RequestFactoryServlet.getThreadLocalRequest().getSession().getServletContext();
		SendMessage.sendMessage(context, user.getEmail(), message);
}




}
