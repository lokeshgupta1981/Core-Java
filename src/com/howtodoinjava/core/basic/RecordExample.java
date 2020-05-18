package com.howtodoinjava.core.basic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RecordExample {
	public static void main(String[] args) 
	{
		EmployeeRecord e1 = new EmployeeRecord
				(1l, "Lokesh", "Gupta", "howtodoinjava@gmail.com", 38);
		
		System.out.println(e1.id());
		System.out.println(e1.email());
		System.out.println(e1.minor());
		
		System.out.println(e1);
		
		EmployeeRecord e2 = new EmployeeRecord
				(1l, "Lokesh", "Gupta", "howtodoinjava@gmail.com", 38);
		
		System.out.println(e1.equals(e2));
		
		System.out.println(e1.hashCode());
		System.out.println(e2.hashCode());
		
		writeToFile(e1, "employee1");
        System.out.println(readFromFile("employee1"));
	}
	
	static void writeToFile(EmployeeRecord obj, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(path))){
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    static EmployeeRecord readFromFile(String path) {
    	EmployeeRecord result = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))){
            result = (EmployeeRecord) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}