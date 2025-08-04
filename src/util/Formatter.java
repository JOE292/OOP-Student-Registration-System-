package util;

public class Formatter {
    public String format(String name){
        return name.toUpperCase();
    }
    public String format(String firstName, String lastName){
        return lastName+ "," +firstName;
    }
}
