import './App.css';
import React, {useState, useEffect} from "react";
import Ergast from "./components/Ergast";
import List from './components/List';
import {faker} from "@faker-js/faker";
import {FixedSizeList} from "react-window";
import DriverList from './components/DriverList';





function App() {
  const [offset, setOffset] = useState(0);

  const changeOffset = (newOffset) => {
    if (newOffset >= 0) {
      setOffset(newOffset);
    }
  };

  return(
    <div className="app-container">
      <DriverList changeOffset={changeOffset} offset={offset} />
  </div>
  );
}


export default App;
