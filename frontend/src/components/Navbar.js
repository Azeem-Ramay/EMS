import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Users, Building2, TrendingUp, Home } from 'lucide-react';
import './Navbar.css';

const Navbar = () => {
  const location = useLocation();

  const navItems = [
    { path: '/', label: 'Dashboard', icon: Home },
    { path: '/employees', label: 'Employees', icon: Users },
    { path: '/departments', label: 'Departments', icon: Building2 },
    {
      path: '/salary-adjustment',
      label: 'Salary Adjustment',
      icon: TrendingUp,
    },
  ];

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <div className="navbar-brand">
          <h1>Employee Management</h1>
        </div>
        <ul className="navbar-nav">
          {navItems.map((item) => {
            const Icon = item.icon;
            const isActive = location.pathname === item.path;
            return (
              <li key={item.path} className="nav-item">
                <Link
                  to={item.path}
                  className={`nav-link ${isActive ? 'active' : ''}`}
                >
                  <Icon size={20} />
                  <span>{item.label}</span>
                </Link>
              </li>
            );
          })}
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;
