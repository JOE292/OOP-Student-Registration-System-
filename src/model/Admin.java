package model;

import abstractclasses.User;
import interfaces.Reportable;

public class Admin extends User implements Reportable{
    public Admin(int userId, String username){
        super(userId,username);
    }

    @Override
    public void login() {
        System.out.println("Admin"+ username +"logged in securely");
    }
    public void generateReport() {
        System.out.println("Generating admin report...");
    }

    @Override
    public String toReportString() {
        return "Admin," + userId + "," + username;
    }
}
