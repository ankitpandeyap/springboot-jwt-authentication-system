  import React, { useState, useContext } from 'react';
  import { useNavigate } from 'react-router-dom';
  import { AuthContext } from '../context/AuthContext';
  import { Navigate } from 'react-router-dom';
  import axiosInstance from '../api/axiosInstance';

  export default function Login() {
    // Step 1: Form fields
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMsg, setErrorMsg] = useState('');
    const { login } = useContext(AuthContext);
    const navigate = useNavigate();
    
    // Step 2: Message to show login success or error
    const [message, setMessage] = useState('');

    // Step 3: Submit handler
    const handleLoginSubmit = async (e) => {
      e.preventDefault();
      try {
        const response = await axiosInstance.post('/auth/login', {
          username,
          password,
        });
    
        console.log('Login response:', response);
        console.log('Headers:', response.headers);
        // Axios normalizes all header keys to lowercase
        const token = response.headers['authorization'] || response.headers['Authorization'];
        console.log('Extracted token:', token);
    
        if (token) {
          login(token); // store in localStorage + update auth state
          setMessage('Login successful!');
          navigate('/dashboard'); // ✅ Now this should work
        } else {
          setErrorMsg('Login failed: No token received');
        }
      } catch (error) {
        console.log('Login error:', error.response);
        setErrorMsg(
          error.response?.data?.message || 'Invalid username or password'
        );
      }
    };

    // Step 6: JSX UI
    return (
      <div className="min-h-screen flex justify-center items-center bg-gray-100">
        <form onSubmit={handleLoginSubmit} className="bg-white p-6 rounded shadow-md w-96">
          <h2 className="text-2xl mb-4 font-bold text-center">Login</h2>

          {errorMsg && <p className="text-red-600">{errorMsg}</p>}

          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Username"
            required
            className="w-full border p-2 mb-4 rounded"
          />
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password"
            required
            className="w-full border p-2 mb-4 rounded"
          />
          <button type="submit" className="bg-blue-600 text-white w-full py-2 rounded">
            Login
          </button>
        </form>
      </div>
    );
  }