# ECSE321 Project Group 1 
![Travis Build Status](https://travis-ci.com/McGill-ECSE321-Winter2020/project-group-01.svg?token=fsmRFkAy9TTnJy5UEPzf&branch=master) [![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy?template=https://github.com/McGill-ECSE321-Winter2020/project-group-01)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/McGill-ECSE321-Winter2020/project-group-01)
![GitHub forks](https://img.shields.io/github/forks/McGill-ECSE321-Winter2020/project-group-01?style=social)
![GitHub stars](https://img.shields.io/github/stars/McGill-ECSE321-Winter2020/project-group-01?style=social)
![GitHub issues](https://img.shields.io/github/issues/McGill-ECSE321-Winter2020/project-group-01)

## Project Overview
We are developing a website and an Android app to facilitate the pet adoption process of a local pet shelter. In this system,
users can post advertisements to put their pets up for adoption. They can also apply to adopt a pet on an advertisement
posted by a user or by the shelter itself. The system is implemented in teams of 5. The implementation of this system
requires requirements engineering, software development, validation of the system through unit testing and automation of the software
delivery process.

## Running the Project
The backend is hosted at https://petshelter-backend.herokuapp.com/. The backend can be run locally by cloning this repository, and running the PetShelter-Backend repository as a Spring Boot App. Using a terminal/command line from the root of the project, one can instead use "gradle run" to run the project locally. In that case, the application will be hosted at http://localhost:8080/. To use the application, see the API documentation [here](https://github.com/McGill-ECSE321-Winter2020/project-group-01/wiki/RESTful-services-documentation).

## Using the Application
The frontend is hosted at https://pet-pawlace.herokuapp.com/. To use it locally, see https://github.com/McGill-ECSE321-Winter2020/project-group-01/blob/master/petshelter-frontend/README.md for more information. The skip preflight check environment variable is used because I am running webpack 4.42.0 while yarn expects 4.41.5. This will be patched eventually as at the moment I am writing this webpack 4.42 was released yesterday so yarn start is not up to date with the webpack versioning.

### About us
We are 5 McGill University students from the Faculty of Engineering

| Name | GitHub | Major | Year |
| ------------- | ------------- | ------------- | ------------- |
|Louis Barrette-Vanasse | [Louismousine](https://github.com/Louismousine) | Software Engineering | U2 |
|Mathieu Bissonnette | [Mat-san](https://github.com/Mat-san) | Computer Engineering | U2 |
|Christopher Boustros | [cboustros](https://github.com/cboustros) | Software Engineering | U2 |
|Ding Ma | [ding-ma](https://github.com/ding-ma) | Software Engineering | U2 |
|Katrina Poulin | [katrinapoulin](https://github.com/katrinapoulin) | Computer Engineering | U2 |

## Overview Tables
### Project
| NAME                   | ROLE |  SPRINT 1 | SPRINT 2 | SPRINT 3 | SPRINT 4 |
|------------------------|------|-----------|----------|----------|----------|
| Louis Barrette-Vanasse | Project Manager | 25        |   32       |    20      |          |
| Mathieu Bissonnette    |Software Developer| 10        |     25     |     0     |          |
| Christopher Boustros   | Software Developer| 17        |      17   |      0    |          |
| Ding Ma                |   Testing Lead | 18        |     47     |      17    |          |
| Katrina Poulin         | Documentation Lead | 16        |    20      |    0      |          |
### Deliverable 1

The project report for this deliverable can be found [here](https://github.com/McGill-ECSE321-Winter2020/project-group-01/wiki/Project-Report:-Sprint-1).

| Name | Contributions | Hours |
| ------------- | ------------- | ------------- |
| Louis Barrette-Vanasse | Domain model, UML code generation, Gradle, Travis CI, Spring and Heroku setup, Requirements, Repositories creation, Lombok annotations, Detailed use cases, Persistence layer tests | 25 |
| Mathieu Bissonnette |Requirements, Persistence layer testing, Wiki. | 10 |
| Christopher Boustros | Gradle, Spring and Heroku setup, Travis CI, Requirements, User stories, Wiki setup, README.md setup, Meeting 1 minutes, Use case specification, Backlog and kanban board setup (making sure each issue has an assignee, label, project, ...) | 17 |
| Ding Ma | UML code generation, Requirements, Persistence layer testing, Travis CI, Detailed use cases | 18 |
| Katrina Poulin | Requirements, Use Case diagrams, Detailed use cases, Documentation, Persistence layer testing | 16 |

### Deliverable 2

The project report for this deliverable can be found [here](https://github.com/McGill-ECSE321-Winter2020/project-group-01/wiki/Project-Report:-Sprint-2).

| Name | Contributions | Hours |
| ------------- | ------------- | ------------- |
| Louis Barrette-Vanasse | <ul><li>Fixing the team's bugs</li><li>User and pet controller and service</li><li>Extra features such as encryption, mailing, token generation and password strength</li><li>Mockito tests for user and pet</li></ul>| 32 |
| Mathieu Bissonnette | <ul><li>Forum and comment controllers, services and tests</li><li>Postman tests</li><li>Advertisement service bug resolving</li></ul>| 25 |
| Christopher Boustros | <ul><li>Setting up backlog</li><li>Application DTO, service, and controller</li><li>Javadocs</li> | 17 |
| Ding Ma | <ul><li>RESTful services tests through Postman, Newman, and Documentation </li><li>Donation, Application and User service tests </li><li>Donation controller and Service </li><li>Heroku deployment</li><li>PetSerives and Application Tests rewrite</li></ul> | 47 |
| Katrina Poulin |<ul><li>Project minutes</li><li>Pet and advertisement service; advertisement controller, Test plan, Sprint report</li></ul>| 20 |

### Deliverable 3

The project report for this deliverable can be found [here](https://github.com/McGill-ECSE321-Winter2020/project-group-01/wiki/Project-Report:-Sprint-3).

The project was cancelled due to the coronavirus pandemic, so that is why the contributions below are disproportionate. 

| Name | Contributions | Hours |
| ------------- | ------------- | ------------- |
| Louis Barrette-Vanasse | <ul><li>Landing page</li><li>Navbar</li><li>Logo</li><li>Deploying to heroku</li><li>Bootstrap themes + overarching css</li><li>Sign in and register</li><li>Forgot password and email verification</li></ul>| 20 |
| Mathieu Bissonnette | N/A| 0 |
| Christopher Boustros | N/A | 0 |
| Ding Ma |<ul><li>Donation</li><li>Dashboard</li><li>Fixed auto deploy to heroku</li></ul> |17 |
| Katrina Poulin | N/A| 0 |

(*) *From ECSE321_Project_Description.pdf written by M. Kanaan, posted on myCourses.*

