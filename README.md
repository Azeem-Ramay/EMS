# Department & Employee Management System

A comprehensive full-stack application with Spring Boot backend and React frontend for managing departments and employees with advanced salary adjustment features and elegant UI.

## Features

### Backend Features
- **Employee Management**: Full CRUD operations for employees
- **Department Management**: Full CRUD operations for departments
- **Salary Adjustment**: Automated salary adjustments based on performance and tenure
- **Data Validation**: Comprehensive input validation
- **Error Handling**: Global exception handling with proper HTTP status codes
- **Database Integration**: PostgreSQL database with JPA/Hibernate

### Frontend Features
- **Modern React UI**: Professional, responsive design with React 18
- **Interactive Dashboard**: Real-time statistics with animated neon effects
- **Employee Management**: Complete CRUD operations with search functionality
- **Department Management**: Card-based layout for department management
- **Salary Adjustment**: Dedicated interface for performance-based salary adjustments
- **Currency Display**: All salary values displayed in Pakistani Ruppees
- **Living Animations**: Hover effects with scale, rotation, and neon glow
- **Responsive Design**: Mobile-friendly interface with adaptive layouts

## Features

- **Employee Management**: Full CRUD operations for employees
- **Department Management**: Full CRUD operations for departments
- **Salary Adjustment**: Automated salary adjustments based on performance and tenure
- **Data Validation**: Comprehensive input validation
- **Error Handling**: Global exception handling with proper HTTP status codes
- **Database Integration**: PostgreSQL database with JPA/Hibernate

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.5.4
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA with Hibernate
- **Validation**: Jakarta Validation
- **Build Tool**: Gradle
- **Java Version**: 17

### Frontend
- **Framework**: React 18
- **Routing**: React Router DOM
- **HTTP Client**: Axios
- **Styling**: CSS3 with modern animations
- **Icons**: Lucide React
- **Notifications**: React Hot Toast
- **Date Formatting**: Date-fns
- **Build Tool**: Create React App

## Database Configuration

The application is configured to use PostgreSQL. Update the database configuration in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/employee_management
spring.datasource.username=postgres
spring.datasource.password=password
```

## API Endpoints

### Department Management

#### Create Department

- **POST** `/departments`
- **Request Body**:

```json
{
  "name": "Engineering",
  "code": "ENG"
}
```

- **Response**: Created department with ID

#### Get Department by ID

- **GET** `/departments/{id}`
- **Response**: Department details

#### Get All Departments

- **GET** `/departments`
- **Response**: List of all departments

#### Update Department

- **PUT** `/departments/{id}`
- **Request Body**:

```json
{
  "name": "Software Engineering",
  "code": "SWE"
}
```

- **Response**: Updated department

#### Delete Department

- **DELETE** `/departments/{id}`
- **Response**: 204 No Content

### Employee Management

#### Create Employee

- **POST** `/employees`
- **Request Body**:

```json
{
  "name": "John Doe",
  "email": "john.doe@company.com",
  "salary": 75000.0,
  "joiningDate": "2023-01-15",
  "departmentId": 1
}
```

- **Response**: Created employee with ID

#### Get Employee by ID

- **GET** `/employees/{id}`
- **Response**: Employee details with department information

#### Get All Employees

- **GET** `/employees`
- **Response**: List of all employees with department information

#### Update Employee

- **PUT** `/employees/{id}`
- **Request Body**:

```json
{
  "name": "John Doe",
  "email": "john.doe@company.com",
  "salary": 80000.0,
  "joiningDate": "2023-01-15",
  "departmentId": 1
}
```

- **Response**: Updated employee

#### Delete Employee

- **DELETE** `/employees/{id}`
- **Response**: 204 No Content

### Salary Adjustment

#### Adjust Salary for Department

- **POST** `/employees/adjust-salary`
- **Request Body**:

```json
{
  "departmentId": 1,
  "performanceScore": 85
}
```

- **Response**: Success message

## Data Models

### Department Entity

- `id` (Long): Primary key
- `name` (String): Department name (2-100 characters)
- `code` (String): Department code (2-10 characters, unique)
- `employees` (List<Employee>): Associated employees

### Employee Entity

- `id` (Long): Primary key
- `name` (String): Employee name (2-100 characters)
- `email` (String): Email address (unique)
- `salary` (BigDecimal): Salary amount
- `joiningDate` (LocalDate): Date of joining
- `department` (Department): Associated department

## Validation Rules

### Department Validation

- Name: Required, 2-100 characters
- Code: Required, 2-10 characters, unique

### Employee Validation

- Name: Required, 2-100 characters
- Email: Required, valid email format, unique
- Salary: Required, greater than 0
- Joining Date: Required
- Department ID: Required, must reference existing department

## Salary Adjustment Logic

The system includes automated salary adjustment functionality:

1. **Performance-based Adjustment**:

   - Score ≥ 90: 15% increase
   - Score ≥ 70: 10% increase
   - Score < 70: No increase

2. **Tenure Bonus**:

   - Employees with 5+ years tenure: 5% bonus

3. **Salary Cap**:
   - Maximum salary: $200,000

## Error Handling

The application includes comprehensive error handling:

- **ResourceNotFoundException**: 404 Not Found
- **DuplicateAdjustmentException**: 409 Conflict
- **Validation Errors**: 400 Bad Request
- **Generic Exceptions**: 500 Internal Server Error

## Running the Application

1. **Prerequisites**:

   - Java 17 or higher
   - PostgreSQL database
   - Gradle

2. **Database Setup**:

   ```sql
   CREATE DATABASE employee_management;
   ```

3. **Build and Run**:

   ```bash
   ./gradlew bootRun
   ```

4. **Access the Application**:
   - Backend API: `http://localhost:8080`
   - Frontend: `http://localhost:3000` (after starting frontend)

## Frontend Setup

1. **Navigate to frontend directory**:
   ```bash
   cd frontend
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Start the development server**:
   ```bash
   npm start
   ```

4. **Access the frontend**:
   - Application runs on: `http://localhost:3000`

## Frontend Features

### Dashboard
- **Animated Statistics Cards**: Real-time data with traveling neon light effects
- **Living Hover Effects**: Cards scale, rotate, and glow on hover
- **Currency Display**: All salary values in Indian Rupees (₹)
- **Quick Action Buttons**: Large, prominent buttons for navigation

### Employee Management
- **Search Functionality**: Real-time search across name, email, and department
- **Modal Forms**: Clean, responsive forms for adding/editing employees
- **Department Validation**: Informative message when no departments exist
- **Currency Formatting**: Salary displayed in Indian format (₹1,00,000)

### Department Management
- **Card Layout**: Modern card-based design for departments
- **CRUD Operations**: Full create, read, update, delete functionality
- **Responsive Design**: Adapts to different screen sizes

### Salary Adjustment
- **Performance-based Logic**: Visual interface for salary adjustments
- **Adjustment History**: Complete history of all salary adjustments
- **Statistics**: Detailed statistics about adjustment operations

## Testing the API

### Create a Department

```bash
curl -X POST http://localhost:8080/departments \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Engineering",
    "code": "ENG"
  }'
```

### Create an Employee

```bash
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@company.com",
    "salary": 75000.00,
    "joiningDate": "2023-01-15",
    "departmentId": 1
  }'
```

### Get All Employees

```bash
curl -X GET http://localhost:8080/employees
```

### Adjust Salary

```bash
curl -X POST http://localhost:8080/employees/adjust-salary \
  -H "Content-Type: application/json" \
  -d '{
    "departmentId": 1,
    "performanceScore": 85
  }'
```

## Project Structure

### Backend Structure
```
src/main/java/com/example/demo/
├── controller/
│   ├── DepartmentController.java
│   └── EmployeeController.java
├── dto/
│   ├── DepartmentDto.java
│   ├── EmployeeDto.java
│   └── SalaryAdjustmentDto.java
├── entity/
│   ├── Department.java
│   └── Employee.java
├── exception/
│   ├── DuplicateAdjustmentException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── repository/
│   ├── DepartmentRepository.java
│   └── EmployeeRepository.java
├── service/
│   ├── DepartmentService.java
│   └── EmployeeService.java
└── DemoApplication.java
```

### Frontend Structure
```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── Navbar.js
│   │   └── Navbar.css
│   ├── pages/
│   │   ├── Dashboard.js
│   │   ├── Dashboard.css
│   │   ├── Employees.js
│   │   ├── Employees.css
│   │   ├── Departments.js
│   │   ├── Departments.css
│   │   ├── SalaryAdjustment.js
│   │   └── SalaryAdjustment.css
│   ├── services/
│   │   └── api.js
│   ├── App.js
│   ├── App.css
│   ├── index.js
│   └── index.css
├── package.json
└── README.md
```

## Database Schema

The application automatically creates the database schema using Hibernate's `ddl-auto=update` setting.

### Tables

- `departments`: Stores department information
- `employees`: Stores employee information with foreign key to departments
