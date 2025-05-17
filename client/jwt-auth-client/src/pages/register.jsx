import React, { useState } from 'react';
import axiosInstance from '../api/axiosInstance';
import '../css/Register.css'
import { useNavigate } from 'react-router-dom';


export default function Register() {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [otp, setOtp] = useState('');
  const [step, setStep] = useState(1);
  const [message, setMessage] = useState('');
  const [otpVerified, setOtpVerified] = useState(false);
  const [role, setRole] = useState('CONSUMER');
  const navigate = useNavigate();

  const handleSendOtp = async (e) => {
    e.preventDefault();
    try {
      const res = await axiosInstance.post(`/auth/otp/request?email=${encodeURIComponent(email)}`);
      setMessage(res.data.message);
      setStep(2);
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed to send OTP');
    }
  };

  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    try {
      const res = await axiosInstance.post(
        `/auth/otp/verify?email=${encodeURIComponent(email)}&otp=${encodeURIComponent(otp)}`
      );
      setMessage(res.data);
      if (res.data.trim() === 'OTP verified') {
        setOtpVerified(true);
        setStep(3);
        console.log('OTP Verified:', otpVerified); // Log the state value
        console.log('Step:', step); // Log the state value
      }
    } catch (err) {
      setMessage(err.response?.data?.message || 'Invalid OTP');
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    if (!otpVerified) {
      setMessage('Please verify OTP first.');
      return;
    }
    try {
      const res = await axiosInstance.post('/auth/register', {
        email,
        username,
        password,
        role, // Include the selected role
      });
      setMessage('Registration successful!');
      navigate('/login');

    } catch (err) {
      setMessage(err.response?.data?.message || 'Registration failed');
    }
  };

  return (
    <div className="register-container">
      <div className="register-card">
        <h2 className="register-title">
          {step === 1 && 'Step 1: Send OTP'}
          {step === 2 && 'Step 2: Verify OTP'}
          {step === 3 && 'Step 3: Complete Registration'}
        </h2>
  
        {message && <div className="register-message">{message}</div>}
  
        {step === 1 && (
          <form onSubmit={handleSendOtp} className="register-form">
            <input
              type="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <button className="send-otp">Send OTP</button>
          </form>
        )}
  
        {step === 2 && (
          <form onSubmit={handleVerifyOtp} className="register-form">
            <input
              type="text"
              placeholder="Enter OTP"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              required
            />
            <button className="verify-otp">Verify OTP</button>
          </form>
        )}
  
        {step === 3 && otpVerified && (
          <form onSubmit={handleRegister} className="register-form">
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
              <select
              value={role}
              onChange={(e) => setRole(e.target.value)}
              className="register-dropdown"
            >
              <option value="CONSUMER">Consumer</option>
              <option value="ADMIN">Admin</option>
            </select>
            <button className="complete-register">Complete Registration</button>
          </form>
        )}
      </div>
    </div>
  );
}