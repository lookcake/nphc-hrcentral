/**
 * 
 */
package com.hrcentral.nphc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.hrcentral.nphc.model.Employee;


@RepositoryRestResource(collectionResourceRel = "TB_User",path = "users")
public interface EmployeeRepository extends JpaRepository<Employee, String> {

}
