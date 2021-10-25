package com.howtodoinjava.jaxb;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

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

    private static Map<Class, JAXBContext> contextCache = new HashMap<>();

    public static JAXBContext getJAXBContext(final Class type)
        throws JAXBException {
        System.out.println("Classname " + type.getName());
        if (contextCache.containsKey(type)) {
            return contextCache.get(type);
        } else {
            final JAXBContext context = JAXBContext.newInstance(type);
            contextCache.put(type, context);
            return context;
        }
    }

    public static JAXBContext getJaxbContext() throws JAXBException {
        JAXBContext c1 = getJAXBContext(Book.class);
        JAXBContext c2 = getJAXBContext(Car.class);
        JAXBContext c3 = getJAXBContext(House.class);
        return c1;
    }
}

@XmlRootElement(name = "XML_MESSAGE")
@XmlAccessorType(XmlAccessType.FIELD)
class Book {
    public String bookId;
}

@XmlRootElement(name = "XML_MESSAGE")
@XmlAccessorType(XmlAccessType.FIELD)
class Car {
    public String carId;
}

@XmlRootElement(name = "XML_MESSAGE")
@XmlAccessorType(XmlAccessType.FIELD)
class House {
    public String houseId;
}