import React, { useState } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

// Komponent do dodawania znaczka
const DodawanieZnaczka = ({ onZnaczekAdded }) => {
  // Stan przechowujący dane formularza dodawanego znaczka
  const [formData, setFormData] = useState({
    nazwa: '',
    opis: '',
    stan: '',
    wysokosc: '',
    szerokosc: '',
    zdjecie: null,
  });

  // Funkcja obsługująca wysyłanie formularza
  const handleSubmit = () => {
    const data = new FormData();
    Object.keys(formData).forEach((key) => {
      data.append(key, formData[key]);
    });
    const token = localStorage.getItem('access_token');

    // Sprawdzanie, czy zdjęcie ma odpowiedni rozmiar
    if (formData.zdjecie && formData.zdjecie.size > 2 * 1024 * 1024) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Rozmiar zdjęcia nie może przekraczać 2MB.',
        timer: 1200,
        showConfirmButton: false,
      });
      return;
    }

    // Sprawdzanie rozszerzenie pliku 
    const fileExtension = formData.zdjecie ? formData.zdjecie.name.split('.').pop().toLowerCase() : '';
    if (formData.zdjecie && !['png', 'jpeg', 'jpg', 'jfif'].includes(fileExtension)) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Tylko pliki PNG lub JPEG są dozwolone.',
        timer: 1200,
        showConfirmButton: false,
      });
      return;
    }

    // Sprawdzanie, czy wysokość zdjęcia jest większa od 0
    if (parseFloat(formData.wysokosc) < 0) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Wysokość nie może być ujemna.',
        timer: 1200,
        width: '300px',
        height: '300px',
        showConfirmButton: false,
      });
      return;
    }

    // Sprawdzanie, czy szerokość zdjęcia jest większa od 0
    if (parseFloat(formData.szerokosc) < 0) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Szerokość nie może być ujemna.',
        timer: 1200,
        width: '300px',
        height: '300px',
        showConfirmButton: false,
      });
      return;
    }

    // Sprawdzanie, czy wysokość zdjęcia jest mniejsza niż 1000
    if (parseFloat(formData.wysokosc) > 1000) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Wysokość nie może przekroczyć 1000 cm.',
        timer: 1200,
        width: '300px',
        height: '300px',
        showConfirmButton: false,
      });
      return;
    }

    // Sprawdzanie, czy szerokość zdjęcia jest mniejsza niż 1000
    if (parseFloat(formData.szerokosc) > 1000) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Szerokość nie może przekroczyć 1000 cm.',
        timer: 1200,
        width: '300px',
        height: '300px',
        showConfirmButton: false,
      });
      return;
    }

    // Sprawdzanie, czy nazwa nie ma więcej niż 100 znaków
    if (formData.nazwa.length > 100) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Nazwa nie może mieć więcej niż 100 znaków.',
        timer: 1200,
        showConfirmButton: false,
      });
      return;
    }

    // Sprawdzanie, czy opis nie ma więcej niż 400 znaków
    if (formData.opis.length > 400) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Opis nie może mieć więcej niż 400 znaków.',
        timer: 1200,
        showConfirmButton: false,
      });
      return;
    }

    // Przesłanie danych nowego znaczka do bazy danych poprzez endpoint
    axios.post('http://127.0.0.1:5000/znaczki', data, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(() => {
        Swal.fire({
          icon: 'success',
          title: 'Hura!',
          text: 'Znaczek dodany pomyślnie!',
          timer: 1200,
          width: '300px',
          height: '300px',
          showConfirmButton: false,
        });
        onZnaczekAdded();
      })
      .catch(() => {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Wystąpił błąd przy dodawaniu znaczka.',
          timer: 1200,
          width: '300px',
          height: '300px',
          showConfirmButton: false,
        });
      });
  };

  return (
    // Komponent formularza dodawania znaczka
    <div className='dodaj-znaczek-kontener'>
      <p style={{ textAlign: 'left', fontVariant: 'small-caps', fontFamily: ' Poppins, sans-serif;', fontSize: '20px', marginBottom: '10px', marginLeft: '10px', marginTop: '0px' }}>
        Dodaj Nowy Znaczek
      </p>
      <div className='dodajInputy'>
      <input
        type="text"
        placeholder="Nazwa"
        value={formData.nazwa}
        onChange={(e) => setFormData({ ...formData, nazwa: e.target.value })}
      />
      <input
        type="text"
        placeholder="Opis"
        value={formData.opis}
        onChange={(e) => setFormData({ ...formData, opis: e.target.value })}
      />
      <input
        type="text"
        placeholder="Stan"
        value={formData.stan}
        onChange={(e) => setFormData({ ...formData, stan: e.target.value })}
      />
      <input
        type="number"
        placeholder="Wysokość [cm]"
        value={formData.wysokosc}
        onChange={(e) => setFormData({ ...formData, wysokosc: e.target.value })}
      />
      <input 
        type="number"
        placeholder="Szerokość [cm]"
        value={formData.szerokosc}
        onChange={(e) => setFormData({ ...formData, szerokosc: e.target.value })}
      />
      <label className="file-upload">
          <input
            type="file"
            accept="image/png, image/jpeg, image/jfif"
            onChange={(e) => setFormData({ ...formData, zdjecie: e.target.files[0] })}
          />
          Wybierz plik
      </label>
      <button onClick={handleSubmit}>Dodaj Znaczek</button>
      </div>
    </div>
  );
};

export default DodawanieZnaczka;
