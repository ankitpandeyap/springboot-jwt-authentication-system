// src/api/axiosInstance.js
import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8082/api',
  withCredentials: true, // ✅ VERY IMPORTANT for cookies
});

export default axiosInstance;
