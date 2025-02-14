import React, { useState } from 'react';
import axios from 'axios';
import Transakcje from './Transakcje';
import '../App.css';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

// Komponent Znaczek, który wyświetla szczegóły znaczka
const Znaczek = ({ znaczek, onTransactionAdded }) => {
  // Stan modalu (czy jest otwarty) i stan raportu
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [raport, setRaport] = useState(null);

  // Funkcja do przełączania stanu modalu
  const toggleModal = () => {
    setIsModalOpen(!isModalOpen);
  };

  // Funkcja do pobierania raportu dla danego znaczka
  const fetchRaport = async () => {
    const token = localStorage.getItem('access_token');
    try {
      const response = await axios.get(`http://127.0.0.1:5000/znaczek/${znaczek.id}/raport`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setRaport(response.data);

      const jsonRaport = JSON.stringify(response.data, null, 2);
      const blob = new Blob([jsonRaport], { type: 'application/json' });
      const url = URL.createObjectURL(blob);

      const a = document.createElement('a');
      a.href = url;
      a.download = `${znaczek.nazwa}_raport.json`; 
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    } catch (error) {
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Wystąpił błąd przy pobieraniu raportu.',
        timer: 1200,
        width: '300px',
        height: '300px',
        showConfirmButton: false,
      });
    }
  };

  return (
    // Komponent obiektu Znaczek
    <div className="znaczek-kontener">
      <div className="znaczek-zdjecie">
      {znaczek.zdjecie && (
        <div>
          <img
            src={`data:image/jpeg;base64,${znaczek.zdjecie}`} 
            alt="Zdjęcie Znaczka"
            style={{ maxHeight: '200px' }}
          />
        </div>
      )}
      </div>
      <div className="znaczek-opis">
      <p>Nazwa: {znaczek.nazwa} </p> 
      <p>Opis: {znaczek.opis} </p> 
      <p>Stan: {znaczek.stan} </p> 
      <p>Wysokość: {znaczek.wysokosc} cm </p> 
      <p>Szerokość: {znaczek.szerokosc} cm</p> 
      <p>Liczba transakcji: {znaczek.liczba_transakcji}</p> 
      </div>
      <div className="znaczek-buttons">
      <button onClick={toggleModal}>Dodaj Transakcję</button>
      <button onClick={fetchRaport}>Pobierz Raport</button>
      {isModalOpen && (
        <div className="modal">
          <div className="modal-content">
            <button onClick={toggleModal} className="close-button">
              X
            </button>
            <Transakcje 
              znaczekId={znaczek.id} 
              onTransactionAdded={() => {
                toggleModal();
                onTransactionAdded(znaczek.id);
              }} 
            />
          </div>
        </div>
      )}
      </div>
    </div>
  );
};


export default Znaczek;
