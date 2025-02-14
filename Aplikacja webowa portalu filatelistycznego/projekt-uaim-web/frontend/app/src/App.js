import React from 'react';
import { BrowserRouter as DefaultRouter, Routes, Route, Navigate } from 'react-router-dom';
import Rejestracja from './components/Rejestracja';
import PanelStrony from './components/PanelStrony';
import Login from './components/Login';
import axios from 'axios';
import { jwtDecode } from "jwt-decode";
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

// PrivateRoute chroniący dostęp do stron wymagających autoryzacji
const PrivateRoute = ({ children }) => {
  const token = localStorage.getItem('access_token');

  // Sprawdzanie czy token wygasł
  const isTokenExpired = () => {
    if (!token) return true;
    try {
      const decoded_token = jwtDecode(token);
      const currentDate = new Date();
      return decoded_token.exp * 1000 < currentDate.getTime();
    } catch (error) {
      return true;
    }
  };

  // Alert dla nieważnego tokena
  if (isTokenExpired()) {
    Swal.fire({
      icon: 'info',
      title: 'Oops...',
      text: 'Sesja wygasła. Zaloguj się ponownie.',
      timer: 1200,
      width: '300px',
      height: '300px',
      showConfirmButton: false,
    });
    localStorage.removeItem('access_token');
    return <Navigate to="/login" />;
  }

  return children;
};

const App = ({ Router = DefaultRouter }) => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<Login />} />
        <Route path="/rejestracja" element={<Rejestracja />} />
        <Route path="/panel" element={<PrivateRoute><PanelStrony /></PrivateRoute>} />
      </Routes>
    </Router>
  );
};

export default App;
