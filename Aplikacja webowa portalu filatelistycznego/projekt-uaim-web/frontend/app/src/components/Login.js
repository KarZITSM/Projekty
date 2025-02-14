import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

// Komponent logowania do strony
const Login = () => {
  // Stan do przechowywania danych logowania
  const [credentials, setCredentials] = useState({ login: '', haslo: '' });
  const navigate = useNavigate();

  // Funkcja wysyłająca dane logowanie użytkownika do backendu w celu uwierzytelnienia
  const handleLogin = () => {
    axios.post('http://127.0.0.1:5000/logowanie', credentials)
    .then((response) => {
      localStorage.setItem('access_token', response.data.access_token);
      Swal.fire({
        icon: "success",
        title: "Hura!",
        text: "Zalogowano pomyślnie.",
        timer: 1200,
        width: '300px',
        height: '300px',
        showConfirmButton: false,
      }).then(() => {
        // Przekierowanie użytkownika do panelu po poprawnym logowaniu
        navigate('/panel'); 
      });
    })
      .catch((error) => {
        Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "Logowanie nie powiodło się.",
          timer: 1200,
          width: '300px',
          height: '300px',
          showConfirmButton: false,
        });
      });
  };

  return (
    // Panel logowania
<div>
<div className='top-panel'>
      <p >Logowanie</p>
    </div>
    <div className='form-log-reg'> 
      <div>
      <div className='input'>
      <input
    type="text"
    placeholder="Login"
    value={credentials.login}
    onChange={(e) => setCredentials({ ...credentials, login: e.target.value })}
  />
  <input
    type="password"
    placeholder="Hasło"
    value={credentials.haslo}
    onChange={(e) => setCredentials({ ...credentials, haslo: e.target.value })}
  />
    <button onClick={handleLogin}>Zaloguj się</button>
  </div>   

      </div>
  <p className='reg'>Nie masz konta? <a href="/rejestracja">Zarejestruj się</a></p>
  </div>
</div>
  );
};

export default Login;
