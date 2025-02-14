from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
import base64
from flask_bcrypt import Bcrypt
from flask_jwt_extended import JWTManager, create_access_token, jwt_required, get_jwt_identity
from datetime import timedelta
from PIL import Image
import io

app = Flask(__name__)
bcrypt = Bcrypt(app) # Inicjalizacja Bcrypt dla haszowania haseł
CORS(app)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://useruaim:projektuaim@baza-danych:3306/app_db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app) # Inicjalizacja bazy danych
app.config["JWT_SECRET_KEY"] = "3u2uh48u47u44hubea3uh3"
app.config["JWT_ACCESS_TOKEN_EXPIRES"] = timedelta(minutes=30) 
jwt = JWTManager(app) # Inicjalizacja Managera JWT

# Model użytkownika w bazie danych
class Uzytkownik(db.Model):
    __tablename__ = 'uzytkownicy'
    id = db.Column(db.Integer, primary_key=True)
    login = db.Column(db.String(50), unique=True, nullable=False)
    haslo = db.Column(db.String(100), nullable=False)

# Model znaczka w bazie danych
class Znaczek(db.Model):
    __tablename__ = 'znaczki'
    id = db.Column(db.Integer, primary_key=True)
    nazwa = db.Column(db.String(100), nullable=False)
    zdjecie = db.Column(db.LargeBinary, nullable=False)
    opis = db.Column(db.String(400), nullable=False)
    stan = db.Column(db.String(50), nullable=False)
    wysokosc = db.Column(db.Numeric(4, 2), nullable=False)
    szerokosc = db.Column(db.Numeric(4, 2), nullable=False)
    transakcja = db.relationship('Transakcja', backref='nazwa')
   
# Model transakcji w bazie danych
class Transakcja(db.Model):
    __tablename__ = 'transakcje'
    id = db.Column(db.Integer, primary_key=True)
    miejsce = db.Column(db.String(150), nullable=False)
    czas = db.Column(db.DateTime, nullable=False)
    kwota = db.Column(db.Numeric(10, 2), nullable=False)
    nazwa_id = db.Column(db.Integer, db.ForeignKey('znaczki.id'))

# Stworzenie bazy danych
with app.app_context():
    db.create_all()

# Endpoint do rejestracji użytkownika
@app.route('/rejestracja', methods=['POST'])
def rejestracja():
    data = request.json
    if len(data['login']) > 50:
        return jsonify({"message": "Login musi mieć  do 50 znaków"}), 400
    if len(data['haslo']) > 100:
        return jsonify({"message": "Hasło musi mieć do 100 znaków"}), 400
    uzytkownik = Uzytkownik(login=data['login'], haslo=bcrypt.generate_password_hash(data['haslo']).decode('utf-8')) 
    db.session.add(uzytkownik)
    db.session.commit()
    return jsonify({"message": "Dodano użytkownika"}), 201

# Endpoint do logowania
@app.route('/logowanie', methods=['POST'])
def login():
    data = request.json
    uzytkownik = Uzytkownik.query.filter_by(login=data['login']).first() 
    if uzytkownik and bcrypt.check_password_hash(uzytkownik.haslo, data['haslo']): 
        access_token = create_access_token(identity=str(uzytkownik.id))
        return jsonify({
            "access_token": access_token
        }), 200
    return jsonify({"message": "Invalid credentials"}), 401

# Sprawdzanie czasu ważności tokena
@jwt.expired_token_loader
def expired_token_callback(jwt_header, jwt_payload):
    return jsonify({"message": "Sesja wygasła. Zaloguj się ponownie."}), 401

# Sprawdzanie poprawności tokena
@jwt.invalid_token_loader
def handle_invalid_token(error):
    return jsonify({"message": "Token sesji jest nieprawidłowy"}), 401

# Endpoint do dodawania znaczka
@app.route('/znaczki', methods=['POST'])
@jwt_required() 
def dodanieZnaczka():
    try:
        nazwa = request.form['nazwa']
        opis = request.form['opis']
        stan = request.form['stan']
        wysokosc = request.form['wysokosc']  
        szerokosc = request.form['szerokosc']
        uploaded_image = request.files['zdjecie'] 
        image = Image.open(uploaded_image)

        if len(nazwa) > 100:
            return jsonify({"error": "Nazwa nie może mieć więcej niż 100 znaków."}), 400

        if len(opis) > 400:
            return jsonify({"error": "Opis nie może mieć więcej niż 400 znaków."}), 400

        if float(wysokosc) < 0 or float(szerokosc) < 0:
            return jsonify({"error": "Wysokość i szerokość nie mogą być ujemne."}), 400

        if float(wysokosc) > 1000 or float(szerokosc) > 1000:
            return jsonify({"error": "Wysokość i szerokość nie mogą przekroczyć 1000 cm."}), 400
        
        img_byte_arr = io.BytesIO()
        image.save(img_byte_arr, format=image.format)
        img_byte_arr = img_byte_arr.getvalue() 

        znaczek = Znaczek(
            nazwa=nazwa,
            zdjecie=img_byte_arr, 
            opis=opis,
            stan=stan,
            wysokosc=wysokosc,
            szerokosc=szerokosc,
        )
        db.session.add(znaczek)
        db.session.commit()

        return jsonify({"message": "Dodano znaczek"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 400

# Endpoint do ppobierania znaczków
@app.route('/znaczki', methods=['GET'])
@jwt_required() 
def wyswietlenieZnaczka():
    znaczki = Znaczek.query.all()
    result = []
    for znaczek in znaczki:
        znaczek_data = {
            'id': znaczek.id,  
            'nazwa': znaczek.nazwa,
            'opis': znaczek.opis,
            'stan': znaczek.stan,
            'wysokosc': znaczek.wysokosc,
            'szerokosc': znaczek.szerokosc,
            'liczba_transakcji': db.session.query(Transakcja).filter(Transakcja.nazwa_id == znaczek.id).count(),
            'zdjecie': base64.b64encode(znaczek.zdjecie).decode('utf-8') if znaczek.zdjecie else None,
        }
        result.append(znaczek_data)
    return jsonify(result)

# Endpoint do dodawania transakcji
@app.route('/transakcje', methods=['POST'])
@jwt_required() 
def dodanieTransakcji():
    try:
        data = request.json

        transakcja = Transakcja(
            miejsce=data['miejsce'],
            czas=data['czas'],
            kwota=data['kwota'],
            nazwa_id=data['nazwa_id']
        )

        if float(data['kwota']) < 0:
            return jsonify({"error": "Kwota transakcji nie może być ujemna"}), 400

        if float(data['kwota']) > 9999:
            return jsonify({"error": "Kwota transakcji nie może być większa niż 9999"}), 400

        if len(data['miejsce']) > 150:
            return jsonify({"error": "Opis nie może mieć więcej niż 400 znaków."}), 400

        db.session.add(transakcja)
        db.session.commit()
        return jsonify({"message": "Dodano transakcję"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 400

# Endpoint do pobierania raportu dla znaczka
@app.route('/znaczek/<int:znaczek_id>/raport', methods=['GET'])
@jwt_required()
def raportZnaczek(znaczek_id):
    try:
        znaczek = Znaczek.query.get(znaczek_id)
        if not znaczek:
            return jsonify({"message": "Znaczek nie znaleziony"}), 404

        transakcje = Transakcja.query.filter(Transakcja.nazwa_id == znaczek_id).all()
        liczba_transakcji = len(transakcje)
        srednia_kwota = sum([transakcja.kwota for transakcja in transakcje]) / liczba_transakcji if liczba_transakcji > 0 else 0

        raport = {
            'liczba_transakcji': liczba_transakcji,
            'srednia_kwota': round(srednia_kwota, 2)
        }
        return jsonify(raport)
    except Exception as e:
        return jsonify({"error": str(e)}), 400




if __name__ == '__main__':
    app.run(debug=True)
