import React, { useState } from 'react';
import axios from 'axios';

export default function Login() {
  // Step 1: Form fields
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  
  // Step 2: Message to show login success or error
  const [message, setMessage] = useState('');

  // Step 3: Submit handler
  const handleLogin = async (e) => {
    e.preventDefault(); // prevent page reload
    try {
      // Step 4: Make POST request to backend
      const response = await axios.post(
        'http://localhost:8080/api/auth/login',
        { username, password },
        {
          withCredentials: true, // 🟢 required to receive refreshToken cookie
        }
      );

      // Step 5: Extract access token from header
      const token = response.headers['authorization']?.split(' ')[1];
      if (token) {
        localStorage.setItem('accessToken', token); // save for now
      }

      setMessage('Login successful!');
      // TODO: redirect to dashboard later
    } catch (error) {
      setMessage(
        error.response?.data?.message || 'Invalid username or password'
      );
    }
  };

  // Step 6: JSX UI
  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="w-full max-w-md bg-white p-8 rounded-xl shadow-xl">
        <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>

        {message && <div className="mb-4 text-blue-500">{message}</div>}

        <form onSubmit={handleLogin} className="space-y-4">
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="w-full border px-3 py-2 rounded"
            required
          />

          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full border px-3 py-2 rounded"
            required
          />

          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
          >
            Login
          </button>
        </form>
      </div>
    </div>
  );
}