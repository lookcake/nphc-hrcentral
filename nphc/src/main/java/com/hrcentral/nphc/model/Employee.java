package com.hrcentral.nphc.model;


import java.util.Date;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="TB_USER")
public class Employee {
	
	@Id
	private String id;
	@Column(unique = true)
	private String login;
	private String name;
	@Column(precision = 2, scale = 2)
	private Double salary;
	private String startDate;
	
//	public Employee(String id) {
//		this.id = id;
//	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public Double getSalary() {
		return salary;
	}
	public String getStartDate() {
		return startDate;
	}
	@Override
	public String toString() {
		return "employee [id=" + id + ", login=" + login + ", name=" + name + ", salary=" + salary + ", startDate="
				+ startDate + "]";
	}
	
}
