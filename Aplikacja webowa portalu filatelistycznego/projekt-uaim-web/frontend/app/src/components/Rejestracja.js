import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

// Komponent rejestracji użytkownika
const Rejestracja = () => {
  // Stany do przechowywania danych rejestracyjnych i błędów
  const [userData, setUserData] = useState({ login: '', haslo: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  // Funkcja obsługująca rejestrację
  const handleRegister = () => {

    // Sprawdzenie długości loginu
    if (userData.login.length > 50) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Login musi mieć od 3 do 50 znaków.',
        timer: 1200,
        width: '300px',
        height: '300px',
        showConfirmButton: false,
      });
      return;
    }
    // Sprawdzenie długości hasła
    if (userData.haslo.length > 100) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Hasło musi mieć co najmniej 6 znaków.',
        timer: 1200,
        width: '300px',
        height: '300px',
        showConfirmButton: false,
      });
      return;
    }
    // Wysłanie danych rejestracyjnych do backendu
    axios.post('http://127.0.0.1:5000/rejestracja', userData)
      .then(() => {
        Swal.fire({
          icon: "success",
          title: "Hura!",
          text: "Zarejestrowano pomyślnie.",
          timer: 1200,
          width: '300px',
          height: '300px',
          showConfirmButton: false,
        }).then(() => {
          navigate('/login');
        });
      })
      .catch(() => {
        Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "Rejestracja nie powiodła się.",
          timer: 1200,
          width: '300px',
          height: '300px',
          showConfirmButton: false,
        });
      });
  };

  return (
  // Komponent panelu rejestracyjnego 
    <div>
      <div className='top-panel'>
        <p>Rejestracja</p>
      </div>
      <div className='form-log-reg'>
        <div className='input'>
          <input
            type="text"
            placeholder="Login"
            value={userData.login}
            onChange={(e) => setUserData({ ...userData, login: e.target.value })}
          />
          <input
            type="password"
            placeholder="Hasło"
            value={userData.haslo}
            onChange={(e) => setUserData({ ...userData, haslo: e.target.value })}
          />
          <button onClick={handleRegister}>Zarejestruj się</button>
        </div>
        {error && <p style={{ color: 'red' }}>{error}</p>}
        <p className='reg'>Masz już konto? <a href="/login">Zaloguj się</a></p>
      </div>
    </div>
  );
};

export default Rejestracja;
