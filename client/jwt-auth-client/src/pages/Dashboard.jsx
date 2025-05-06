import React, { useEffect, useState } from 'react';

export default function Dashboard() {
  const [token, setToken] = useState('');

  useEffect(() => {
    // Read token from localStorage
    const savedToken = localStorage.getItem('accessToken');
    setToken(savedToken || 'No token found');
  }, []);

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-green-100">
      <div className="bg-white p-8 rounded-xl shadow-xl text-center w-full max-w-md">
        <h1 className="text-3xl font-bold mb-4">Dashboard</h1>
        <p className="text-lg text-gray-700 mb-4">Welcome! 🎉</p>
        <p className="text-sm break-words text-gray-500">
          <strong>Access Token:</strong><br /> {token}
        </p>
      </div>
    </div>
  );
}
