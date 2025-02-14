import requests
import json
from datetime import time, date, datetime

BASE_URL = "http://127.0.0.1:5000"  
HEADERS = {
    "Authorization": "Karol Zelazowski" 
}

def endpoint_test(endpoint, method="GET", data=None, params=None):
    url = f"{BASE_URL}{endpoint}"
    if method == "GET":
        response = requests.get(url, headers=HEADERS, params=params)
    else:
        response = requests.post(url, headers=HEADERS, json=data)
    print(f"Endpoint: {method} {endpoint}")
    print(f"Status code: {response.status_code}")
    print("JSON:")
    print(json.dumps(response.json(), indent=4, ensure_ascii=False))

#Zapytanie o listę nauczycieli
#endpoint_test("/teacher-list")

# test pozytywny
#endpoint_test("/teacher-details", params={"id_nauczyciela": 1}) 
# test negatywny
#endpoint_test("/teacher-details", params={"id_nauczyciela": 88}) 

lekcja_do_zarezerwoawania = {
    "id_studenta": 1,
    "id_nauczyciela": 1,
    "nazwa_przedmiotu": "matematyka",
    "termin": "2024-12-17T10:00:00"
}
# test pozytywny
#endpoint_test("/book-lesson", method="POST", data=lekcja_do_zarezerwoawania) 

lekcja_juz_zajeta = {
    "id_studenta": 1,
    "id_nauczyciela": 1,
    "termin": "2024-12-17T10:00:00",
    "nazwa_przedmiotu": "matematyka"
}
# test negatywny 
#endpoint_test("/book-lesson", method="POST", data=lekcja_juz_zajeta)

lekcja_niepoprawny_termin = {
    "id_studenta": 1,
    "id_nauczyciela": 1,
    "termin": "2024-12-19T22:00:00",
    "nazwa_przedmiotu": "matematyka"
}

# test negatywny
#endpoint_test("/book-lesson", method="POST", data=lekcja_niepoprawny_termin) 

poprawny_nauczyciel = {
    "imie": "Stefan",
    "nazwisko": "Banach",
    "prowadzone_przedmioty": '["matematyka"]',
    "opis": "Wybitny matematyk.",
    "ocena_nauczyciela": 4.9,
    "numer_telefonu": "987654221",
    "stawka": 150,
    "waluta": "PLN",
    "email": "stf.banach@gmail.com"
}
# test pozytywny
#endpoint_test("/add-teacher", method="POST", data=poprawny_nauczyciel) 

nauczyciel_niepoprawny = {
    "imie": "Juliusz",
    "nazwisko": "Mickiewicz",
    "prowadzone_przedmioty": "fizyka",
    "opis": "Ziutek",
    "ocena_nauczyciela": 1.0,
    "numer_telefonu": "123123123",
    "stawka": 150,
    "waluta": "PLN",
}

# test negatywny 
#endpoint_test("/add-teacher", method="POST", data=nauczyciel_niepoprawny)  

# test pozytywny
#endpoint_test("/get-lessons", params={"id_studenta": 1, "data_od": "2024-12-01", "data_do": "2024-12-20"}) 


# test negatywny 
endpoint_test("/get-lessons", params={"id_studenta": 16, "data_poczatkowa": "2024-12-01", "data_koncowa": "2024-12-17"})