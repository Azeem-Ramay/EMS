import React, { useState, useEffect } from 'react';
import { Plus, Edit, Trash2, Eye, Search } from 'lucide-react';
import { employeeAPI, departmentAPI } from '../services/api';
import { format } from 'date-fns';
import toast from 'react-hot-toast';
import './Employees.css';

const Employees = () => {
  const [employees, setEmployees] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingEmployee, setEditingEmployee] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    salary: '',
    joiningDate: '',
    departmentId: '',
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [employeesRes, departmentsRes] = await Promise.all([
        employeeAPI.getAll(),
        departmentAPI.getAll(),
      ]);
      setEmployees(employeesRes.data);
      setDepartments(departmentsRes.data);
    } catch (error) {
      console.error('Error fetching data:', error);
      toast.error('Failed to load data');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingEmployee) {
        await employeeAPI.update(editingEmployee.id, formData);
        toast.success('Employee updated successfully');
      } else {
        await employeeAPI.create(formData);
        toast.success('Employee created successfully');
      }
      setShowModal(false);
      setEditingEmployee(null);
      resetForm();
      fetchData();
    } catch (error) {
      console.error('Error saving employee:', error);
      toast.error('Failed to save employee');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this employee?')) {
      try {
        await employeeAPI.delete(id);
        toast.success('Employee deleted successfully');
        fetchData();
      } catch (error) {
        console.error('Error deleting employee:', error);
        toast.error('Failed to delete employee');
      }
    }
  };

  const handleEdit = (employee) => {
    setEditingEmployee(employee);
    setFormData({
      name: employee.name,
      email: employee.email,
      salary: employee.salary,
      joiningDate: employee.joiningDate,
      departmentId: employee.departmentId,
    });
    setShowModal(true);
  };

  const resetForm = () => {
    setFormData({
      name: '',
      email: '',
      salary: '',
      joiningDate: '',
      departmentId: '',
    });
  };

  const filteredEmployees = employees.filter(
    (employee) =>
      employee.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      employee.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
      employee.departmentName?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getDepartmentName = (departmentId) => {
    const dept = departments.find((d) => d.id === departmentId);
    return dept ? dept.name : 'Unknown';
  };

  return (
    <div className="employees-page">
      <div className="page-header">
        <div>
          <h1>Employees</h1>
          <p>Manage your organization's employees</p>
        </div>
        {departments.length === 0 ? (
          <div className="info-message">
            <p>
              ⚠️ Please add departments first before creating employees.
              Employees cannot exist without a department.
            </p>
            <button
              className="btn btn-primary"
              onClick={() => (window.location.href = '/departments')}
            >
              Go to Departments
            </button>
          </div>
        ) : (
          <button
            className="btn btn-primary"
            onClick={() => {
              setEditingEmployee(null);
              resetForm();
              setShowModal(true);
            }}
          >
            <Plus size={16} />
            Add Employee
          </button>
        )}
      </div>

      <div className="search-bar">
        <div className="search-input">
          <Search size={20} />
          <input
            type="text"
            placeholder="Search employees..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      {loading ? (
        <div className="loading">
          <div className="spinner"></div>
          <p>Loading employees...</p>
        </div>
      ) : (
        <div className="employees-table">
          <table className="table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Department</th>
                <th>Salary</th>
                <th>Joining Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredEmployees.map((employee) => (
                <tr key={employee.id}>
                  <td>
                    <div className="employee-info">
                      <div className="employee-avatar">
                        {employee.name.charAt(0).toUpperCase()}
                      </div>
                      <span>{employee.name}</span>
                    </div>
                  </td>
                  <td>{employee.email}</td>
                  <td>
                    <span className="badge badge-info">
                      {getDepartmentName(employee.departmentId)}
                    </span>
                  </td>
                  <td>₹{parseFloat(employee.salary).toLocaleString('en-IN')}</td>
                  <td>
                    {format(new Date(employee.joiningDate), 'MMM dd, yyyy')}
                  </td>
                  <td>
                    <div className="action-buttons">
                      <button
                        className="btn btn-outline"
                        onClick={() => handleEdit(employee)}
                        title="Edit"
                      >
                        <Edit size={14} />
                      </button>
                      <button
                        className="btn btn-danger"
                        onClick={() => handleDelete(employee.id)}
                        title="Delete"
                      >
                        <Trash2 size={14} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {filteredEmployees.length === 0 && (
            <div className="empty-state">
              <p>No employees found</p>
            </div>
          )}
        </div>
      )}

      {showModal && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>{editingEmployee ? 'Edit Employee' : 'Add Employee'}</h2>
              <button className="close-btn" onClick={() => setShowModal(false)}>
                ×
              </button>
            </div>
            <form onSubmit={handleSubmit} className="modal-form">
              <div className="form-group">
                <label className="form-label">Name</label>
                <input
                  type="text"
                  className="form-input"
                  value={formData.name}
                  onChange={(e) =>
                    setFormData({ ...formData, name: e.target.value })
                  }
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">Email</label>
                <input
                  type="email"
                  className="form-input"
                  value={formData.email}
                  onChange={(e) =>
                    setFormData({ ...formData, email: e.target.value })
                  }
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">Salary</label>
                <input
                  type="number"
                  className="form-input"
                  value={formData.salary}
                  onChange={(e) =>
                    setFormData({ ...formData, salary: e.target.value })
                  }
                  min="0"
                  step="0.01"
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">Joining Date</label>
                <input
                  type="date"
                  className="form-input"
                  value={formData.joiningDate}
                  onChange={(e) =>
                    setFormData({ ...formData, joiningDate: e.target.value })
                  }
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">Department</label>
                <select
                  className="form-input"
                  value={formData.departmentId}
                  onChange={(e) =>
                    setFormData({ ...formData, departmentId: e.target.value })
                  }
                  required
                >
                  <option value="">Select Department</option>
                  {departments.map((dept) => (
                    <option key={dept.id} value={dept.id}>
                      {dept.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="modal-actions">
                <button
                  type="button"
                  className="btn btn-outline"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>
                <button type="submit" className="btn btn-primary">
                  {editingEmployee ? 'Update' : 'Create'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Employees;
