package com.ssi.util;

import java.util.Comparator;

import com.ssi.business.Person;

public class SortbyName implements Comparator<Person> { 
    public int compare(Person a, Person b) { 
        return a.getLastName().compareTo( b.getLastName() ); 
    } 
} 
