import React, { useState, useEffect } from 'react';
import { Plus, Edit, Trash2, Search } from 'lucide-react';
import { departmentAPI } from '../services/api';
import toast from 'react-hot-toast';
import './Departments.css';

const Departments = () => {
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingDepartment, setEditingDepartment] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [formData, setFormData] = useState({
    name: '',
    code: ''
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const response = await departmentAPI.getAll();
      setDepartments(response.data);
    } catch (error) {
      console.error('Error fetching departments:', error);
      toast.error('Failed to load departments');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingDepartment) {
        await departmentAPI.update(editingDepartment.id, formData);
        toast.success('Department updated successfully');
      } else {
        await departmentAPI.create(formData);
        toast.success('Department created successfully');
      }
      setShowModal(false);
      setEditingDepartment(null);
      resetForm();
      fetchData();
    } catch (error) {
      console.error('Error saving department:', error);
      toast.error('Failed to save department');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this department?')) {
      try {
        await departmentAPI.delete(id);
        toast.success('Department deleted successfully');
        fetchData();
      } catch (error) {
        console.error('Error deleting department:', error);
        toast.error('Failed to delete department');
      }
    }
  };

  const handleEdit = (department) => {
    setEditingDepartment(department);
    setFormData({
      name: department.name,
      code: department.code
    });
    setShowModal(true);
  };

  const resetForm = () => {
    setFormData({
      name: '',
      code: ''
    });
  };

  const filteredDepartments = departments.filter(department =>
    department.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    department.code.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="departments-page">
      <div className="page-header">
        <div>
          <h1>Departments</h1>
          <p>Manage your organization's departments</p>
        </div>
        <button 
          className="btn btn-primary" 
          onClick={() => {
            setEditingDepartment(null);
            resetForm();
            setShowModal(true);
          }}
        >
          <Plus size={16} />
          Add Department
        </button>
      </div>

      <div className="search-bar">
        <div className="search-input">
          <Search size={20} />
          <input
            type="text"
            placeholder="Search departments..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      {loading ? (
        <div className="loading">
          <div className="spinner"></div>
          <p>Loading departments...</p>
        </div>
      ) : (
        <div className="departments-grid">
          {filteredDepartments.map((department) => (
            <div key={department.id} className="department-card">
              <div className="department-header">
                <div className="department-icon">
                  {department.name.charAt(0).toUpperCase()}
                </div>
                <div className="department-info">
                  <h3>{department.name}</h3>
                  <span className="department-code">{department.code}</span>
                </div>
              </div>
              <div className="department-actions">
                <button
                  className="btn btn-outline"
                  onClick={() => handleEdit(department)}
                  title="Edit"
                >
                  <Edit size={14} />
                </button>
                <button
                  className="btn btn-danger"
                  onClick={() => handleDelete(department.id)}
                  title="Delete"
                >
                  <Trash2 size={14} />
                </button>
              </div>
            </div>
          ))}
          {filteredDepartments.length === 0 && (
            <div className="empty-state">
              <p>No departments found</p>
            </div>
          )}
        </div>
      )}

      {showModal && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>{editingDepartment ? 'Edit Department' : 'Add Department'}</h2>
              <button 
                className="close-btn" 
                onClick={() => setShowModal(false)}
              >
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
                  onChange={(e) => setFormData({...formData, name: e.target.value})}
                  required
                />
              </div>
              <div className="form-group">
                <label className="form-label">Code</label>
                <input
                  type="text"
                  className="form-input"
                  value={formData.code}
                  onChange={(e) => setFormData({...formData, code: e.target.value})}
                  required
                />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-outline" onClick={() => setShowModal(false)}>
                  Cancel
                </button>
                <button type="submit" className="btn btn-primary">
                  {editingDepartment ? 'Update' : 'Create'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Departments; 