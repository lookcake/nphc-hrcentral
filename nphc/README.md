# Required
## JDK
`17.0.3.1`
## Maven
`3.8.6`
# Build
`./mvnw clean install`
# Run
`./mvnw spring-boot:run`
# Test
`./mvnw test`
# Default localhost path
`localhost:8080`
# H2 Database
## Console
`http://localhost:8080/h2-console`
## JDBC URL
`jdbc:h2:mem:jd`
## Credentials
| User  | Password  | 
| :------------ |:---------------:|
| sa      | &nbsp; |

### Software for testing

[Postman https://www.postman.com/](https://www.postman.com/)

### Webservice
Available end points

Name | URL | Request method | Response Type
------------ | ------------ | ------------- | -------------
 CSVUpload API | /users/upload | POST | application/json
 Fetch API | /users | GET | application/json
 Get API | /users/{$id} | GET | application/json
 Create API | /users/ | POST | application/json
 Create Mutilple API | /users/saveall | POST | application/json
 Update API | /users/{$id} | PUT/PATCH | application/json
 Delete API | /users/{$id} | DELEE | application/json
 Size API | /users{?size}| GET | application/json
 Search API | /users/Search/ | GET | application/json
 Search API | /users/serach/findByid?id={$id} | GET | application/json
 Search API | /users/serach/findByname?name={$name} | GET | application/json
 Search API | /users/serach/findByLogin?login={$login} | GET | application/json
 Search API | /users/serach/findByLogin?date={$date} | GET | application/json
 Search API | /users/serach/findByLogin?date={$date} | GET | application/json
 Sort API | /users{?sort}| GET | application/json
 Sort API | /users?sort=name,asc/desc | GET | application/json
 Sort API | /users?sort=id,asc/desc | GET | application/json
 Sort API | /users?sort=login,asc/desc | GET | application/json
 Sort API | /users?sort=salary,asc/desc | GET | application/json
 Sort API | /users?sort=name,asc&sort=login,desc | GET | application/json
 Sort API | /users?sort=login,asc&sort=salary,desc | GET | application/json
 Sort API | /users?sort=id,asc&sort=startdate,desc | GET | application/json
 Page API | /users{?page}| GET | application/json
 Page API | /users?page={$number}&Size={$number} | GET | application/json

# Added Features
+ `GET /users` Added **sort** as query parameter. 
+ `GET /users` Added **page** as query parameter. 
+ `GET /users` Added **size** as query parameter. 
+ `GET /users` Added **search** as query parameter. 
 
### File Upload API
```
POST http://localhost:8080/users/upload
```


## Assumption

<ul>
  <li>Only to serve web service</li>
  <li>All API return responses are in JSON format</li>
  <li>Allow different error message structure on top specification</li>
  <li>Pre-set first employee data used when launching</li>
  <li>Request parameter name is case sensitive</li>
  <li>All request parameter, request body is provided</li>
  <li>Request body/data is correctly formed</li>
  <li>User Story 1
    <ul>
      <li>Validation required against database to check if id or login is unique</li>
      <li>CSV provided complies to specification of 5 columns id,login,name,salary,startDate</li>
    </ul>
  </li>
  <li>User Story 2
    <ul>
      <li>Request parameter will be default value when not provided</li>
      <li>Additional parameter is not used when not provided</li>
      <li>Allow sorting for all columns</li>
	  <li>Allow paging and sizing of API</li>
	  <li>Allow searching for all columns</li>
	  <li>CSV file must be in UTF-8 Encoding.</li>	   
	  <li>Allow filter the salary column by LessThan, GreaterThan, LessOrEuqal, GreterOrEqual</li> 
      <li>"no limit" refer to 2147483647 rows, max value of Integer</li>
	  <li>File size limit 10MB.</li>
     </ul>
  </li>
  <li>GET /users
    <ul>
      <li>If no query parameters is provided, use get all the employee  parameters.</li>
      <li>if not existing employee found, will create</li>
      <li>**page** must be used together with **size**</li>
	  <li>if **sortBy** is invalid COLUMN_NAME, will default to **id**.</li>      
     </ul>
  </li>
  <li>PUT/PATCH /users
    <ul>
      <li>Will only process if it is a valid JSON User object, no partials allowed.</li>
      <li>Will only return an error if query parameters are invalid.</li>
      <li>**page** must be used together with **size**</li>
	  <li>if **sortBy** is invalid COLUMN_NAME, will default to **id**.</li>      
     </ul>
  </li>
</ul>

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  

