import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Znaczek from './Znaczek';
import DodawanieZnaczka from './DodawanieZnaczka';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

// Komponent ListaZnaczkow - wyświetla listę znaczków
const ListaZnaczkow = () => {
  const [znaczki, setZnaczki] = useState([]);

  // Stan do przechowywania listy znaczków
  useEffect(() => {
    fetchZnaczki();
  }, []);

   // Funkcja do pobierania znaczków z bazy danych poprzez endpoint
  const fetchZnaczki = () => {
    const token = localStorage.getItem('access_token');
    axios.get('http://127.0.0.1:5000/znaczki', {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((response) => setZnaczki(response.data))
      .catch(() => {
        Swal.fire({
          icon: "error",
          title: "Błąd",
          text: "Wystąpił błąd przy wyświetlaniu listy.",
          timer: 1200,
          width: '300px',
          height: '300px',
          showConfirmButton: false,
        });
      });
  };

  // Funkcja wywoływana po dodaniu nowego znaczka - odświeża listę
  const handleTransactionAdded = (znaczekId) => {
    fetchZnaczki();
  };

  return (
    <div >
      {/* Komponent do dodawania nowych znaczków */}
      <DodawanieZnaczka  onZnaczekAdded={fetchZnaczki} />
      <p style={{textAlign: 'center', fontVariant: 'small-caps', fontFamily: ' Poppins, sans-serif;', fontSize: '26px', marginBottom: '5px' , marginTop: '0'}}>Kolekcja Znaczków</p>
      {/* Komponent do wyświetlania listy znaczków*/}
      <ul>
        {znaczki.map((znaczek) => (
          <Znaczek 
            key={znaczek.id} 
            znaczek={znaczek} 
            onTransactionAdded={handleTransactionAdded} 
          />
        ))}
      </ul>
    </div>
  );
};

export default ListaZnaczkow;
