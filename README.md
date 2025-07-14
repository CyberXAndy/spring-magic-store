# Spring Magic Store 🪄

## About The Project

This project is a web application for a magic-themed shop, "Owl's Secret Magic Store," built using the Spring Framework with a Java backend. The application started as a basic inventory management system with a simple user interface and was enhanced to meet specific customer requirements, making it a more robust and user-friendly e-commerce platform.

This project showcases skills in **Java**, **Spring Boot**, **Thymeleaf**, **HTML/CSS**, and **web application development**.

---

## Key Features

The following features and enhancements were implemented to improve the application:

### UI/UX Enhancements
* **Customized HTML User Interface**: The UI was updated to create a more immersive and visually appealing experience for the magic shop theme. This includes custom styling for forms, buttons, and tables.
* **'About' Page**: An "About" page was added to provide information about the store, with navigation back to the main screen.
* **Bootstrap Integration**: Bootstrap was used to improve the layout and responsiveness of the application.

### Core Functionality
* **Sample Inventory**: The application is pre-loaded with a sample inventory of 5 unique parts and 5 products to demonstrate the application's functionality.
* **'Buy Now' Button**: A "Buy Now" button was added to the product list, allowing users to purchase products directly. This feature decrements the product inventory and provides a success or failure message.
* **Inventory Validation**:
    * **Min/Max Inventory**: Added fields for minimum and maximum inventory levels for parts.
    * **Inventory Constraints**: Implemented validation to prevent inventory levels from going below the minimum or above the maximum when adding or updating parts and products.
    * **Low Inventory Errors**: The system now provides specific error messages for low inventory situations.
* **Unit Tests**: Added unit tests for the `minInv` and `maxInv` fields in the `PartTest` class to ensure data integrity and validation logic.

### Codebase Improvements
* **Code Cleanup**: Removed the unused `DeletePartValidator.java` file from the validators package to improve code maintainability.
* **Persistence**: The application's database file was renamed for clarity.

---

## Technologies Used

* **Java**
* **Spring Boot**
* **Spring MVC**
* **Spring Data JPA**
* **Thymeleaf**
* **H2 Database**
* **Maven**
* **HTML5 & CSS3**
* **Bootstrap**

---

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

* **JDK 17**
* **Maven**

### Installation

1.  **Clone the repo**
    ```sh
    git clone [https://github.com/CyberXAndy/spring-magic-store.git](https://github.com/CyberXAndy/spring-magic-store.git)
    ```
2.  **Navigate to the project directory**
    ```sh
    cd spring-magic-store
    ```
3.  **Run the application**
    ```sh
    mvn spring-boot:run
    ```
4.  **Open your browser** and navigate to `http://localhost:8080/mainscreen`.

---

## License

Distributed under the MIT License. See `LICENSE` for more information.