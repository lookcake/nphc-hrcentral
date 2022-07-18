package com.hrcentral.nphc.model;

import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(name="TB_USER")
public class Employee {
	
	@Id
	private String id;
	@Column(unique = true)
	private String login;
	private String name;
	private double salary;
	@Column(precision = 2, scale = 11)
	private Date startDate;
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
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Override
	public String toString() {
		return "employee [id=" + id + ", login=" + login + ", name=" + name + ", salary=" + salary + ", startDate="
				+ startDate + "]";
	}
	
}
