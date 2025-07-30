import React, { useState, useEffect } from 'react';
import { TrendingUp, DollarSign, Users, BarChart3 } from 'lucide-react';
import { employeeAPI, departmentAPI } from '../services/api';
import toast from 'react-hot-toast';
import './SalaryAdjustment.css';

const SalaryAdjustment = () => {
  const [departments, setDepartments] = useState([]);
  const [employees, setEmployees] = useState([]);
  const [adjustmentHistory, setAdjustmentHistory] = useState([]);
  const [stats, setStats] = useState({});
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState({
    departmentId: '',
    performanceScore: ''
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      const [departmentsRes, employeesRes, historyRes, statsRes] = await Promise.all([
        departmentAPI.getAll(),
        employeeAPI.getAll(),
        employeeAPI.getSalaryAdjustments(),
        employeeAPI.getSalaryStats()
      ]);
      setDepartments(departmentsRes.data);
      setEmployees(employeesRes.data);
      setAdjustmentHistory(historyRes.data);
      setStats(statsRes.data);
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
      await employeeAPI.adjustSalary(formData);
      toast.success('Salary adjustment completed successfully');
      setFormData({ departmentId: '', performanceScore: '' });
      fetchData();
    } catch (error) {
      console.error('Error adjusting salary:', error);
      toast.error('Failed to adjust salary');
    }
  };

  const getDepartmentName = (departmentId) => {
    const dept = departments.find(d => d.id === departmentId);
    return dept ? dept.name : 'Unknown';
  };

  const getPerformanceLevel = (score) => {
    if (score >= 90) return { level: 'Excellent', color: 'success' };
    if (score >= 70) return { level: 'Good', color: 'warning' };
    return { level: 'Needs Improvement', color: 'danger' };
  };

  const getAdjustmentPercentage = (score) => {
    if (score >= 90) return 15;
    if (score >= 70) return 10;
    return 0;
  };

  const getEmployeesInDepartment = (departmentId) => {
    return employees.filter(emp => emp.departmentId === departmentId);
  };

  return (
    <div className="salary-adjustment-page">
      <div className="page-header">
        <div>
          <h1>Salary Adjustment</h1>
          <p>Performance-based salary adjustments for departments</p>
        </div>
      </div>

      <div className="adjustment-container">
        <div className="adjustment-form-section">
          <div className="card">
            <div className="card-header">
              <h2>New Salary Adjustment</h2>
              <p>Select a department and performance score to adjust salaries</p>
            </div>
            <div className="card-body">
              <form onSubmit={handleSubmit}>
                <div className="form-group">
                  <label className="form-label">Department</label>
                  <select
                    className="form-input"
                    value={formData.departmentId}
                    onChange={(e) => setFormData({...formData, departmentId: e.target.value})}
                    required
                  >
                    <option value="">Select Department</option>
                    {departments.map((dept) => (
                      <option key={dept.id} value={dept.id}>
                        {dept.name} ({getEmployeesInDepartment(dept.id).length} employees)
                      </option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Performance Score (0-100)</label>
                  <input
                    type="number"
                    className="form-input"
                    value={formData.performanceScore}
                    onChange={(e) => setFormData({...formData, performanceScore: e.target.value})}
                    min="0"
                    max="100"
                    required
                  />
                </div>
                {formData.performanceScore && (
                  <div className="performance-preview">
                    <h4>Adjustment Preview</h4>
                    <div className="preview-grid">
                      <div className="preview-item">
                        <span>Performance Level:</span>
                        <span className={`badge badge-${getPerformanceLevel(formData.performanceScore).color}`}>
                          {getPerformanceLevel(formData.performanceScore).level}
                        </span>
                      </div>
                      <div className="preview-item">
                        <span>Base Increase:</span>
                        <span>{getAdjustmentPercentage(formData.performanceScore)}%</span>
                      </div>
                      <div className="preview-item">
                        <span>Tenure Bonus:</span>
                        <span>+5% (if >5 years)</span>
                      </div>
                      <div className="preview-item">
                        <span>Max Salary Cap:</span>
                        <span>$200,000</span>
                      </div>
                    </div>
                  </div>
                )}
                <button type="submit" className="btn btn-primary">
                  <TrendingUp size={16} />
                  Adjust Salaries
                </button>
              </form>
            </div>
          </div>
        </div>

        <div className="stats-section">
          <div className="stats-grid">
            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#3b82f6' }}>
                <Users size={24} color="white" />
              </div>
              <div className="stat-content">
                <h3>Total Employees</h3>
                <p>{employees.length}</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#10b981' }}>
                <DollarSign size={24} color="white" />
              </div>
              <div className="stat-content">
                <h3>Total Adjustments</h3>
                <p>{stats.totalAdjustments || 0}</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon" style={{ backgroundColor: '#f59e0b' }}>
                <BarChart3 size={24} color="white" />
              </div>
              <div className="stat-content">
                <h3>Average Increase</h3>
                <p>{stats.averageIncrease || 0}%</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="history-section">
        <div className="card">
          <div className="card-header">
            <h2>Adjustment History</h2>
          </div>
          <div className="card-body">
            {loading ? (
              <div className="loading">
                <div className="spinner"></div>
                <p>Loading history...</p>
              </div>
            ) : (
              <div className="history-table">
                <table className="table">
                  <thead>
                    <tr>
                      <th>Department</th>
                      <th>Performance Score</th>
                      <th>Level</th>
                      <th>Employees Affected</th>
                      <th>Date</th>
                    </tr>
                  </thead>
                  <tbody>
                    {adjustmentHistory.map((adjustment, index) => (
                      <tr key={index}>
                        <td>{getDepartmentName(adjustment.departmentId)}</td>
                        <td>{adjustment.performanceScore}</td>
                        <td>
                          <span className={`badge badge-${getPerformanceLevel(adjustment.performanceScore).color}`}>
                            {getPerformanceLevel(adjustment.performanceScore).level}
                          </span>
                        </td>
                        <td>{adjustment.employeesAffected || 'N/A'}</td>
                        <td>{new Date(adjustment.timestamp).toLocaleDateString()}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
                {adjustmentHistory.length === 0 && (
                  <div className="empty-state">
                    <p>No adjustment history found</p>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default SalaryAdjustment; 