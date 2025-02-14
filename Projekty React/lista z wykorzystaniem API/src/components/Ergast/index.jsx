import React, {useState, useEffect} from "react";

const loadJSON = key =>
key && JSON.parse(localStorage.getItem(key));

const saveJSON = (key, data) =>
localStorage.setItem(key, JSON.stringify(data));

function Ergast ({driverId}) {
    const [data,setData] = useState(
        loadJSON(`driverId:${driverId}`)
    );

    useEffect(()=>{
        if(!data) {console.log("DEBUG Exit"); return;}
        const {givenName, familyName, nationality, permanentNumber} = data;
        saveJSON(`driverId:${driverId}`,{
            givenName,
            familyName,
            nationality,
            permanentNumber
            });
    },[data, driverId]);


    useEffect(()=>{
    if(!driverId) return;
    if(data && data.driverId === driverId) return;
    fetch (`https://api.jolpi.ca/ergast/f1/drivers/${driverId}.json`)
        .then(response => response.json())
        .then(response1 => {
            const driverData = response1?.MRData?.DriverTable?.Drivers[0];
            setData(driverData);
            })
        .catch(console.error);
    },[driverId]);

    if(data) {
        return <pre>{JSON.stringify(data,null, 2)}</pre>
    }
    
}

export default Ergast;