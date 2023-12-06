/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leeuw.services;

import com.leeuw.controllers.AdminService;
import com.leeuw.entities.Admin;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author HP
 */

// AdminBean.java (dans le package approprié)
@ManagedBean(name="adminBean")
@SessionScoped
public class AdminController implements Serializable {
    private String userName;
    private String password;
    private boolean loggedIn;

    @EJB
    private AdminService adminService;

    public String login() {
        if (adminService.validateLogin(userName, password)) {
            loggedIn = true;
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedInAdmin", true);

            return "/faces/web/machine/index?faces-redirect=true"; // Rediriger vers la page de bienvenue après une connexion réussie
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed", "Invalid credentials"));
            return null; // Stay on the same page
        }
    }

    public String logout() {
        loggedIn = false;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("loggedInAdmin");

        // Rediriger vers la page de déconnexion ou une autre page
        return "/index?faces-redirect=true";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
    
}

