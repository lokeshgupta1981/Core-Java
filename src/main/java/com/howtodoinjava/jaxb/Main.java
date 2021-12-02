package com.howtodoinjava.jaxb;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

//For my testing only


@SuppressWarnings("unused")
public class Main {

    public static void main(final String[] args) throws JAXBException {
        JAXBContext context = getJAXBContext(Book.class);
        JAXBContext context2 = getJAXBContext(Book.class);
        JAXBContext context3 = getJAXBContext(Book.class);
        JAXBContext context4 = getJAXBContext(House.class);
        JAXBContext context5 = getJAXBContext(Car.class);
        JAXBContext context6 = getJAXBContext(Book.class);
        JAXBContext context7 = getJAXBContext(Book.class);
        JAXBContext context8 = getJAXBContext(Book.class);
    }

    @SuppressWarnings("rawtypes")
    private static Map<Class, JAXBContext> contextCache = new HashMap<>();

    @SuppressWarnings("rawtypes")
    public static JAXBContext getJAXBContext(final Class type) throws JAXBException {
        System.out.println("Classname " + type.getName());
        if (contextCache.containsKey(type)) {
            return contextCache.get(type);
        } else {
            final JAXBContext context = JAXBContext.newInstance(type);
            contextCache.put(type, context);
            return context;
        }
    }
}

@XmlRootElement(name = "XML_MESSAGE")
@XmlAccessorType(XmlAccessType.FIELD)
class Book {
    private String bookId;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}

@XmlRootElement(name = "XML_MESSAGE")
@XmlAccessorType(XmlAccessType.FIELD)
class Car {
    private String carId;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }
}

@XmlRootElement(name = "XML_MESSAGE")
@XmlAccessorType(XmlAccessType.FIELD)
class House {
    private String houseId;

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }
}