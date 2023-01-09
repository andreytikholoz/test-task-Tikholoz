# Test task Tikholoz Andrii

___

## General info
This project is the implementation of a test task.
Realized in this version: *project implementation with registration, spring security, using flyway.*

## How to start service locally
No additional configurations are needed to start the service. All configurations are in *application.yaml*.

### How to start Docker container
1. Build the jar file: for this you need to do: *mvn clean install*;
2. Build the Docker Image locally: *docker build . -t "test-task:0.0.1"*;
3. Run Docker container based on Image: *docker run -p 8080:8080 "test-task:0.0.1"*.

## What this service is about
The user of this service can perform next methods:
* Create account
* Authorize his account
* Upload avatar
* Update his account information
* Get user list (only for user with email "admin")
  
###Description of API:

| Endpoint name | Endpoint URL                      | Body                              | Headers     | Params             |Output data|Authorization|
|:-------------:|:---------------------------------:|:---------------------------------:|:-----------:|:------------------:|:---------:|:-----------:|
| createUser    | /user                             |email, passwordHash, avatarBase64  |-            |-                   |UserDTO    |-            |
| authorizeUser | /user                             |-                                  |Authorization|id                  |UserDTO    |Basic Auth   |
| uploadAvatar  | /user/avatar                      |avatarBase64                       |Authorization|-                   |-          |Basic Auth   |
| updateUser    | /user                             |id, email,passwordHash,avatarBase64|Authorization|-                   |UserDTO    |Basic Auth   |
| getUserList   | /user/list                        |-                                  |Authorization|pageSize, pageNumber|UserListDTO|Basic Auth   |


In the *resources* you can find file *TestTaskTikholoz.PostmanCollection.json* where a collection of requests for each method of this service is presented.

**IMPORTANT :**
* The hash of the password is passed to the entity, but not the password. As planned, the password should be hashed on the client side.
* On the client side, the avatar file must be converted to base64 string.
* Method "Get user list" can only be performed by admin user(user with email "admin").

The project uses the OpenAPI specification. In the *resources* you can find configuration file *userApi.yaml*.
      