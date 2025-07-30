# Employee Management System - Frontend

A modern, responsive React frontend for the Employee Management System with a clean and professional design.

## Features

- **Dashboard**: Overview with key statistics and quick actions
- **Employee Management**: Full CRUD operations with search functionality
- **Department Management**: Create, update, and delete departments
- **Salary Adjustment**: Performance-based salary adjustments with business rules
- **Responsive Design**: Works perfectly on desktop, tablet, and mobile
- **Modern UI**: Clean, professional design with smooth animations

## Tech Stack

- **React 18**: Modern React with hooks
- **React Router**: Client-side routing
- **Axios**: HTTP client for API calls
- **React Hot Toast**: Beautiful notifications
- **Lucide React**: Modern icons
- **Date-fns**: Date formatting utilities
- **CSS3**: Custom styling with modern design patterns

## Getting Started

### Prerequisites

- Node.js (version 16 or higher)
- npm or yarn
- Backend Spring Boot application running on `http://localhost:8080`

### Installation

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

4. Open your browser and visit `http://localhost:3000`

## Project Structure

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
└── package.json
```

## Features Overview

### Dashboard
- Real-time statistics display
- Quick action buttons
- Responsive card layout

### Employee Management
- List all employees with search functionality
- Add new employees with form validation
- Edit existing employee details
- Delete employees with confirmation
- Employee avatars and department badges

### Department Management
- Grid layout for departments
- Add, edit, and delete departments
- Search functionality
- Department codes and icons

### Salary Adjustment
- Performance-based salary calculations
- Real-time adjustment preview
- Adjustment history tracking
- Statistics and analytics
- Business rules implementation:
  - Performance ≥90: 15% increase
  - Performance 70-89: 10% increase
  - Performance <70: No increase
  - Tenure bonus: +5% for >5 years
  - Maximum salary cap: $200,000

## API Integration

The frontend communicates with the Spring Boot backend through REST APIs:

- **Employees**: `/employees` (GET, POST, PUT, DELETE)
- **Departments**: `/departments` (GET, POST, PUT, DELETE)
- **Salary Adjustments**: `/employees/adjust-salary` (POST)

## Design System

### Colors
- Primary: #3b82f6 (Blue)
- Success: #10b981 (Green)
- Warning: #f59e0b (Yellow)
- Danger: #ef4444 (Red)
- Neutral: #6b7280 (Gray)

### Typography
- Font Family: Inter (Google Fonts)
- Responsive font sizes
- Consistent spacing

### Components
- Cards with subtle shadows
- Buttons with hover effects
- Form inputs with focus states
- Tables with hover effects
- Modals with backdrop

## Responsive Design

The application is fully responsive and works on:
- Desktop (1200px+)
- Tablet (768px - 1199px)
- Mobile (<768px)

## Development

### Available Scripts

- `npm start`: Start development server
- `npm build`: Build for production
- `npm test`: Run tests
- `npm eject`: Eject from Create React App

### Code Style

- Consistent component structure
- CSS modules for styling
- Proper error handling
- Loading states
- User feedback with toasts

## Deployment

To build for production:

```bash
npm run build
```

The build files will be created in the `build/` directory and can be deployed to any static hosting service.

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

1. Follow the existing code style
2. Add proper error handling
3. Include loading states
4. Test on different screen sizes
5. Ensure accessibility standards

## License

This project is part of the Employee Management System. 