import React, { useState, useEffect } from 'react';
import { Users, Building2, DollarSign, TrendingUp } from 'lucide-react';
import { employeeAPI, departmentAPI } from '../services/api';
import toast from 'react-hot-toast';
import './Dashboard.css';

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalEmployees: 0,
    totalDepartments: 0,
    averageSalary: 0,
    salaryAdjustments: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      const [employees, departments, salaryStats] = await Promise.all([
        employeeAPI.getAll(),
        departmentAPI.getAll(),
        employeeAPI.getSalaryStats(),
      ]);

      const totalSalary = employees.data.reduce(
        (sum, emp) => sum + parseFloat(emp.salary),
        0
      );
      const avgSalary =
        employees.data.length > 0 ? totalSalary / employees.data.length : 0;

      setStats({
        totalEmployees: employees.data.length,
        totalDepartments: departments.data.length,
        averageSalary: avgSalary,
        salaryAdjustments: salaryStats.data?.totalAdjustments || 0,
      });
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
      toast.error('Failed to load dashboard data');
    } finally {
      setLoading(false);
    }
  };

  const StatCard = ({ title, value, icon: Icon, color }) => (
    <div className="stat-card">
      <div className="stat-icon" style={{ backgroundColor: color }}>
        <Icon size={24} color="white" />
      </div>
      <div className="stat-content">
        <h3 className="stat-title">{title}</h3>
        <p className="stat-value">
          {loading ? (
            <div className="spinner"></div>
          ) : title === 'Average Salary' ? (
            `₹${value.toLocaleString('en-IN', {
              minimumFractionDigits: 0,
              maximumFractionDigits: 0,
            })}`
          ) : (
            value.toLocaleString()
          )}
        </p>
      </div>
    </div>
  );

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <h1>Dashboard</h1>
        <p>Overview of your employee management system</p>
      </div>

      <div className="stats-grid">
        <StatCard
          title="Total Employees"
          value={stats.totalEmployees}
          icon={Users}
          color="#3b82f6"
        />
        <StatCard
          title="Total Departments"
          value={stats.totalDepartments}
          icon={Building2}
          color="#10b981"
        />
        <StatCard
          title="Average Salary"
          value={stats.averageSalary}
          icon={DollarSign}
          color="#f59e0b"
        />
        <StatCard
          title="Salary Adjustments"
          value={stats.salaryAdjustments}
          icon={TrendingUp}
          color="#ef4444"
        />
      </div>

      <div className="dashboard-actions">
        <div className="action-card">
          <h3>Quick Actions</h3>
          <div className="action-buttons">
            <button
              className="btn btn-primary"
              onClick={() => (window.location.href = '/employees')}
            >
              <Users size={16} />
              Manage Employees
            </button>
            <button
              className="btn btn-secondary"
              onClick={() => (window.location.href = '/departments')}
            >
              <Building2 size={16} />
              Manage Departments
            </button>
            <button
              className="btn btn-success"
              onClick={() => (window.location.href = '/salary-adjustment')}
            >
              <TrendingUp size={16} />
              Adjust Salaries
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
