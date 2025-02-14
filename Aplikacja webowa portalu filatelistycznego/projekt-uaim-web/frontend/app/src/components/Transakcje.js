import React, { useState } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

// Komponent Transakcje, który umożliwia dodawanie nowych transakcji
const Transakcje = ({ znaczekId, onTransactionAdded }) => {
  // Stan przechowujący dane formularza dodawania transakcji
  const [formData, setFormData] = useState({
    miejsce: '',
    czas: '',
    kwota: '',
  });

  // Funkcja obsługująca wysyłanie formularza
  const handleSubmit = () => {
    const token = localStorage.getItem('access_token');

    // Sprawdzenie czy kwota nie jest ujemna
    if (parseFloat(formData.kwota) < 0) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Kwota nie może być ujemna.',
        timer: 1200,
        showConfirmButton: false,
      });
      return;
    }

    // Sprawdzenie czy kwota nie jest większa od 9999
    if (parseFloat(formData.kwota) > 9999) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Kwota nie może przekroczyć 9999 PLN.',
        timer: 1200,
        width: '300px',
        height: '300px',
        showConfirmButton: false,
      });
      return;
    }

    // Sprawdzenie czy miejsce nie jest ma więcej znaków niż 150
    if (formData.miejsce.length > 150) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Miejsce nie może mieć więcej niż 150 znaków.',
        timer: 1200,
        showConfirmButton: false,
      });
      return;
    }
    
    // Wysłanie danych formularza dodawania transakcji do backendu
    axios.post('http://127.0.0.1:5000/transakcje',
      { ...formData, nazwa_id: znaczekId },
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    )
      .then(() => {
        Swal.fire({
          icon: 'success',
          title: 'Hura!',
          text: 'Transakcja dodana pomyślnie.',
          timer: 1200,
          showConfirmButton: false,
        });
        setFormData({ miejsce: '', czas: '', kwota: '' });
        if (onTransactionAdded) onTransactionAdded();
      })
      .catch(() => {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Wystąpił błąd przy dodawaniu transakcji.',
          timer: 1200,
          showConfirmButton: false,
        });
      });
  };


  return (
    // Komponent panelu dodawania transakcji
    <div>
      <h4>Dodaj Transakcję</h4>
      <input
        type="text"
        placeholder="Miejsce"
        value={formData.miejsce}
        onChange={(e) => setFormData({ ...formData, miejsce: e.target.value })}
      />
      <input
        type="datetime-local"
        value={formData.czas}
        onChange={(e) => setFormData({ ...formData, czas: e.target.value })}
      />
      <input
        type="number"
        placeholder="Kwota [PLN]"
        value={formData.kwota}
        onChange={(e) => setFormData({ ...formData, kwota: e.target.value })}
      />
      <button onClick={handleSubmit}>Dodaj Transakcję</button>
    </div>
  );
};

export default Transakcje;
