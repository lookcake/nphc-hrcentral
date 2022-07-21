package com.hrcentral.nphc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hrcentral.nphc.controller.EmployeeController;

@SpringBootTest
public class SmokeTest {

	@Autowired
	private EmployeeController<?> controller;
	
	@Test
	public void contextLoads() throws Exception{
		assertThat(controller).isNotNull();
	} 
	
}
