package com.ssi.business;

public class Person {

	private String firstName;
	private String lastName;
	private String fullName;
	private int age;
	private String phone;
	public Person() {}
	public Person( String inData ) {
		String columns[] = inData.split( "\t" );
		setFullName( columns[0] );
		setAge( new Integer(columns[1]));
		setPhone( columns[2] );
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		String names[] = fullName.split( "," );
		setFirstName( names[1] );
		setLastName( names[0] );
		this.fullName = fullName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String extract() {
		return fullName +"\t"+ age +"\t" + phone +"\n";
	}
	public String toString() {
		return "firstName="+firstName+", lastName="+lastName+", age="+age+", phoneNumber="+phone;
	}
}
