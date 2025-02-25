# Web Chat Application

A web chat application built with Java, utilizing Spring Boot, Spring Security, WebSockets, Thymeleaf, MySQL, and Redis. Users can register, log in, chat with others, and add contacts via profile links.

## Features

- **User Registration and Login**: Secure authentication with Spring Security.
- **Real-time Messaging**: WebSockets for instant communication.
- **User Profiles**: Accessible via `/profile/{id}`, allowing users to add contacts.
- **Persistent Storage**: MySQL for user data and Redis for caching.

## Tech Stack

- **Spring Boot**: Backend framework
- **Spring Security**: Authentication and authorization
- **WebSockets**: Real-time communication
- **Thymeleaf**: Template engine for dynamic web pages
- **MySQL**: Relational database management
- **Redis**: In-memory data structure store

## Getting Started

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Dmytro-Trofimov/HarmonyChat.git
   cd Pisomka
2. Set Up MySQL Database

 - Create a new MySQL database.
 - Update application.properties with your database details.

3. Configure Redis

- Ensure Redis is installed and running.
- Update application.properties with Redis configurations.

4. Build and Run the Application

5. Access the Application Open your browser and navigate to http://localhost:8080.

Contributing
Feel free to fork this repository and contribute by submitting a pull request.






# FUTURE: solve the n+1 problem
update db structure maybe change to mongo or cassandra
update logic
