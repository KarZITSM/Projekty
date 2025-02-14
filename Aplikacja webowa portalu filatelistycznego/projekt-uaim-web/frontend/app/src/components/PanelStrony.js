import React from 'react';
import { useNavigate } from 'react-router-dom';
import ListaZnaczkow from './ListaZnaczkow';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

// Komponent panelu strony
const PanelStrony = () => {
  const navigate = useNavigate();

  // Funkcja obsługująca wylogowanie
  const handleLogout = () => {
    localStorage.removeItem('access_token');
    Swal.fire({
      icon: 'success',
      title: 'Hura!',
      text: 'Wylogowano pomyślnie',
      timer: 1500,
      showConfirmButton: false,
    }).then(() => {
      navigate('/login');
    });
  };

  return (
  // Komponent panelu strony
    <div>
      <div className='top-panel' >
        <button onClick={handleLogout}>Wyloguj się</button>
        <p>Portal Filatelistyczny</p>
      </div>
      <div style={{marginTop: '20px'}}>
        <ListaZnaczkow />
      </div>
    </div>
  );

};

export default PanelStrony;
