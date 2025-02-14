import requests
import json


# TEST REJESTRACJI
url = 'http://localhost:5000/rejestracja'

data = {
    'login': 'test_user',
    'haslo': 'test_password'
}

response = requests.post(url, json=data)
print(response.status_code)
print(response.json())

# TEST DODANIA ZNACZKA
url = 'http://localhost:5000/znaczki'
token = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTczNjUxNDQzNywianRpIjoiZjBkMDBhNTQtY2JiNi00ZjdkLTlhOTItZjNkY2NhMDI5ZGY4IiwidHlwZSI6ImFjY2VzcyIsInN1YiI6IjUiLCJuYmYiOjE3MzY1MTQ0MzcsImNzcmYiOiIyYmRkNzBjMS1lMDI0LTQ5ZTgtYmI0Yy01MWVmYTAwNmMzMmUiLCJleHAiOjE3MzY1MTYyMzd9.nQgwwAndiMSPEczWfHQLGBw44z2e3dPB7sL5VHjSYvg'  # Zamień na rzeczywisty token JWT

headers = {
    'Authorization': f'Bearer {token}'
}

files = {
    'zdjecie': open('Z.png', 'rb')
}

data = {
    'nazwa': 'Znaczek 1',
    'opis': 'Opis znaczka 1',
    'stan': 'Nowy',
    'wysokosc': 10.5,
    'szerokosc': 7.5
}

response = requests.post(url, headers=headers, data=data, files=files)
print(response.status_code)
print(response.json())

# TEST PRZETERMINOWANEGO TOKENU 

url = 'http://localhost:5000/znaczki'

token = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTczNjUxNDQzNywianRpIjoiZjBkMDBhNTQtY2JiNi00ZjdkLTlhOTItZjNkY2NhMDI5ZGY4IiwidHlwZSI6ImFjY2VzcyIsInN1YiI6IjUiLCJuYmYiOjE3MzY1MTQ0MzcsImNzcmYiOiIyYmRkNzBjMS1lMDI0LTQ5ZTgtYmI0Yy01MWVmYTAwNmMzMmUiLCJleHAiOjE3MzY1MTYyMzd9.nQgwwAndiMSPEczWfHQLGBw44z2e3dPB7sL5VHjSYvg'  # Zamień na rzeczywisty token JWT

headers = {
    'Authorization': f'Bearer {token}'
}

response = requests.get(url, headers=headers)
print(response.status_code)
print(response.json())

# TEST DODANIA TRANSKACJI
url = 'http://localhost:5000/transakcje'

data = {
    'miejsce': 'Warszawa',
    'czas': '2025-01-10T10:00:00',
    'kwota': 120.5,
    'nazwa_id': 1
}

response = requests.post(url, headers=headers, json=data)
print(response.status_code)
print(response.json())

# TEST RAPORTU
znaczek_id = 1  # Zamień na odpowiedni ID znaczka
url = f'http://localhost:5000/znaczek/{znaczek_id}/raport'

response = requests.get(url, headers=headers)
print(response.status_code)
print(response.json())

# TEST NIEPOPRAWNEGO TOKENU
url = 'http://localhost:5000/znaczki'
invalid_token = 'invalid_token'  

headers = {
    'Authorization': f'Bearer {invalid_token}'
}

response = requests.get(url, headers=headers)
print(response.status_code)
print(response.json())