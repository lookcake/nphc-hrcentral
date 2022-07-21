package com.hrcentral.nphc.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrcentral.nphc.helper.CustomException;
import com.hrcentral.nphc.helper.EmployeeJson;
import com.hrcentral.nphc.helper.ResponseMessage;
import com.hrcentral.nphc.model.Employee;
import com.hrcentral.nphc.helper.ResponseMessage;
import com.hrcentral.nphc.repository.EmployeeRepository;
import com.hrcentral.nphc.service.EmployeeService;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmployeeRepository repo;

	@Autowired
	private EmployeeService service;
	
	
	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void deleteAllBeforeTests() throws Exception {
		repo.deleteAll();
	}

	@Test
	public void ShouldReturnTableName() throws Exception {

		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$._links.TB_User").exists());
	}

	@Test
	public void shouldSaveEmployeeThenReturnDetail() throws Exception {
		
		EmployeeJson emplJson = new EmployeeJson("f0001", "loginfj", "fanjie", "1000.0", "2022-04-17");
		String jsonString = objectMapper.writeValueAsString(emplJson);
		
		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonString)).andExpect(
				status().isOk())
		.andExpect(jsonPath("$.id").value("f0001"))
		.andExpect(jsonPath("$.login").value("loginfj"))
		.andExpect(jsonPath("$.name").value("fanjie"))
		.andExpect(jsonPath("$.salary").value("1000.0"))
		.andExpect(jsonPath("$.startDate").value("2022-04-17"));
	}
	
	@Test
	public void shouldSaveInvalidEmployeeReturnError() throws Exception {
		
		EmployeeJson emplJson = new EmployeeJson("", "loginfj", "fanjie", "1000.0", "2022-04-17");
		String jsonString = objectMapper.writeValueAsString(emplJson);
		
		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(jsonString)).andExpect(
				status().isBadRequest())
		.andExpect(content().string(containsString(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE_BAD)));
	}
	
	@Test
	public void shouldGetEmployeeByIdReturnDetail() throws Exception {
		EmployeeJson emplJson = new EmployeeJson("f0001", "loginfj", "fanjie", "1000.0", "2022-04-17");
		String jsonString = objectMapper.writeValueAsString(emplJson);
		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)).andExpect(
						status().isOk()).andReturn();
		
		mockMvc.perform(get("/users/f0001")).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value("f0001"))
		.andExpect(jsonPath("$.login").value("loginfj"));
	}
	
	@Test
	public void shouldGetInvalidEmployeeByIdReturnError() throws Exception {
		EmployeeJson emplJson = new EmployeeJson("f0001", "loginfj", "fanjie", "1000.0", "2022-04-17");
		String jsonString = objectMapper.writeValueAsString(emplJson);
		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)).andExpect(
						status().isOk()).andReturn();
		
		mockMvc.perform(get("/users/f0002")).andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").value(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE_BAD_INPUT));
	}
	
	@Test
	public void shouldPutEmployeeReturnDetail() throws Exception {
		EmployeeJson emplJson = new EmployeeJson("f0001", "loginfj", "fanjie", "1000.0", "2022-04-17");
		String jsonString = objectMapper.writeValueAsString(emplJson);
		mockMvc.perform(put("/users/f0001")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)).andExpect(
						status().isOk())		
		.andExpect(jsonPath("$.id").value("f0001"))
		.andExpect(jsonPath("$.login").value("loginfj"))
		.andExpect(jsonPath("$.name").value("fanjie"))
		.andExpect(jsonPath("$.salary").value("1000.0"))
		.andExpect(jsonPath("$.startDate").value("2022-04-17"));

	}
	
	@Test
	public void shouldPatchEmployeeReturnDetail() throws Exception {
		EmployeeJson emplJson = new EmployeeJson("f0002", "logincg", "chengong", "2000.0", "2022-04-17");
		String jsonString = objectMapper.writeValueAsString(emplJson);
		mockMvc.perform(patch("/users/f0002")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)).andExpect(
						status().isOk())		
		.andExpect(jsonPath("$.id").value("f0002"))
		.andExpect(jsonPath("$.login").value("logincg"))
		.andExpect(jsonPath("$.name").value("chengong"))
		.andExpect(jsonPath("$.salary").value("2000.0"))
		.andExpect(jsonPath("$.startDate").value("2022-04-17"));
		
	}
	
	@Test
	public void shouldDeleteEmployeeByIdReturnOk() throws Exception {
		EmployeeJson emplJson = new EmployeeJson("f0001", "logincg", "chengong", "2000.0", "2022-04-17");
		String jsonString = objectMapper.writeValueAsString(emplJson);
		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)).andExpect(
						status().isOk()).andReturn();
		
		mockMvc.perform(delete("/users/f0001")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())		
		.andExpect(jsonPath("$.message").value(ResponseMessage.MSG_SUC_DELETED));
		
	}
	
	@Test
	public void shouldDeleteEmployeeByIdReturnError() throws Exception {
		EmployeeJson emplJson = new EmployeeJson("f0001", "logincg", "chengong", "2000.0", "2022-04-17");
		String jsonString = objectMapper.writeValueAsString(emplJson);
		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonString)).andExpect(
						status().isOk()).andReturn();
		
		mockMvc.perform(delete("/users/f0003")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())		
		.andExpect(jsonPath("$.message").value(ResponseMessage.MSG_ERR_NO_SUCH_EMPLOYEE_BAD_INPUT));
		
	}
	
	

}
