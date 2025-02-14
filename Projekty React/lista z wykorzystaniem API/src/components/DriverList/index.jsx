import React, { useState, useEffect } from "react";
import { FixedSizeList as List } from "react-window";

const loadJSON = key =>
  key && JSON.parse(localStorage.getItem(key));
  
const saveJSON = (key, data) =>
  localStorage.setItem(key, JSON.stringify(data));


function DriverList({changeOffset, offset}) {
    const [drivers, setDrivers] = useState([]);
    const [limit, setLimit] = useState(30);  
    const maxOffset = 860;

    


    useEffect(() => {
      
      const saveDriversToLocalStorage = (driversList) => {
          driversList.forEach((driver) => {
              const { givenName, familyName, nationality, permanentNumber } = driver;
              const driverData = { givenName, familyName, nationality, permanentNumber };
              saveJSON(`driverId:${driver.driverId}`, driverData); 
          });
      };

      
      const cachedDrivers = [];
      let allDriversCached = true;

      for (let i = 0; i < limit; i++) {
          const driverData = loadJSON(`driverId:${offset + i}`);
          if (driverData) {
              cachedDrivers.push(driverData);
          } else {
              allDriversCached = false;
              break;
          }
      }

      if (allDriversCached) {
          setDrivers(cachedDrivers);
          return;
      }


      fetch(`https://api.jolpi.ca/ergast/f1/drivers.json?offset=${offset}&limit=${limit}`)
          .then(response => response.json())
          .then(data => {
              const fetchedDrivers = data?.MRData?.DriverTable?.Drivers;
              if (fetchedDrivers) {
                  console.log("Fetched drivers:", fetchedDrivers);
                  setDrivers(fetchedDrivers);
                  saveDriversToLocalStorage(fetchedDrivers); 
              } else {
                  console.error('Błąd: Nie znaleziono danych kierowców.');
              }
          })
          .catch(error => console.error('Błąd podczas pobierania danych:', error));
    }, [offset, limit]);


   
    const pageNumber = Math.floor(offset / limit) + 1;

    const Row = ({ index, style }) => {
        const driver = drivers[index];
        return (
            <div className="driver-row" style={style} key={driver.driverId}>
                {driver.givenName} {driver.familyName} - {driver.nationality}
            </div>
        );
    };

    return (
        <div className="driver-list-container">
            <h1>F1 Driver List</h1>
            <p className="page-number">Page: {pageNumber}</p>

            <label className="limit-selector">
                Drivers per page:
                <select value={limit} onChange={(e) => setLimit(Number(e.target.value))}>
                    <option value={10}>10</option>
                    <option value={20}>20</option>
                    <option value={30}>30</option>
                    <option value={50}>50</option>
                </select>
            </label>

            <div className="pagination-buttons">
                <button onClick={() => changeOffset(offset - limit)} disabled={offset === 0}>
                    Previous Page
                </button>
                <button onClick={() => changeOffset(offset + limit)} disabled={offset + limit > maxOffset}>
                    Next Page
                </button>
            </div>

            {/* Wirtualizowana lista */}
            <List
                className="driver-list"
                height={300} // wysokość widocznej listy w pikselach
                itemCount={drivers.length}
                itemSize={35} // wysokość pojedynczego elementu w pikselach
                width="100%"
            >
                {Row}
            </List>
        </div>
    );
}

export default DriverList;