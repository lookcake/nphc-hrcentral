/**
 * 
 */
package com.hrcentral.nphc.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;

import com.hrcentral.nphc.model.Employee;


@RepositoryRestResource(collectionResourceRel = "TB_User",path = "users")
public interface EmployeeRepository extends JpaRepository<Employee, String> {

	Optional<Employee> findById(@Param("id") String id);
	List<Employee> findByName(@Param("name") String name);
	List<Employee> findByLogin(@Param("login") String login);
	List<Employee> findByStartDate(@DateTimeFormat(pattern = "yyyy-MM-dd") @Param("date") Date date);
	
	@RestResource(path = "minSalary")
	List<Employee> findBySalaryLessThan(@Param("salary") Double salary);
	@RestResource(path = "maxSalary")
	List<Employee> findBySalaryGreaterThan(@Param("salary") Double salary );
	
	@RestResource(path = "lessequal")
	List<Employee> findBySalaryLessThanEqual(@Param("salary") Double salary);
	@RestResource(path = "greaterequal")
	List<Employee> findBySalaryGreaterThanEqual(@Param("salary") Double salary );
	
	public boolean existsByLoginAndIdNot(String login, String id);

	
	
}
