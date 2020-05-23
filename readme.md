# ContactBook

Test task for DeliaSoft

## Getting Started

The project is located at https: https://github.com/BliznyukAlex/contactBook

Source code can be download as Zip file or by using Git
https://github.com/BliznyukAlex/contactBook.git

# Running the application

There are several ways to run a Spring Boot application on your local machine. 
One way is to execute the main method in the com.testtask.contactbook.ContactBookApplication.java class from your IDE.

Another way is to go to project directory and run from command line
 
    mvn spring-boot:run
    
# DB configuration

DB locates on remote server so you don't need to configure it 

## Endpoints
    
    GET request to /user/contacts/ --> returns a list of user's contacts for user or list of all users contacts for admin
    
    GET request to /user/contacts/1 --> returns a contact with contact_id = 1
    
    POST request to /user/contacts/ --> creates a contact
    
    PUT request to /user/contacts/1 --> updates a contact with contact_id = 1
    
    DELETE request to /user/contacts/1 --> removes a contact with contact_id = 1
    
    POST request to /registration --> creates new user


## To view Swagger 2 API docs

Run the server and browse to localhost:8080/swagger-ui.html

## To login

In DB already exists 2 users:
username: admin / password: admin
username: user / password: user
Or you can create your own user with password

## Request examples
### Create user

    POST /registration
    Accept: application/json
    Content-Type: application/json
    {
        "userName": "user2",
        "password": "user2"
    }
    RESPONSE: HTTP 201 (Created)

### Create contact

    POST /user/contacts
    Accept: application/json
    Content-Type: application/json
    {
        "firstName": "dd3",
        "lastName": "dd3",
        "phone": "+97(244)555-55-55"
    }
    RESPONSE: HTTP 201 (Created)

### Update contact

    PUT /user/contacts/1
    Accept: application/json
    Content-Type: application/json
    {
        "firstName": "dd3",
        "lastName": "dd3",
        "phone": "+97(244)555-55-55"
    }
    RESPONSE: HTTP 200 (OK)