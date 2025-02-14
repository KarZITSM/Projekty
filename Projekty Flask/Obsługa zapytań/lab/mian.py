from flask import Flask
from flask_sqlalchemy import  SQLAlchemy
import os
from datetime import time, date, datetime
from flask import request, jsonify
from sqlalchemy import extract



app = Flask(__name__)
basedir = os.path.abspath(os.path.dirname(__file__))

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + os.path.join(basedir, 'data.sqlite')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SQLALCHEMY_COMMIT_ON_TEARDOWN'] = True
db = SQLAlchemy(app)

# MODELS
class Teacher (db.Model):
    __tablename__ = "Nauczyciele"
    id_nauczyciela = db.Column(db.Integer, primary_key=True)
    imie = db.Column(db.String(50), nullable=False)
    nazwisko = db.Column(db.String(50), nullable=False)
    prowadzone_przedmioty = db.Column(db.String(200), nullable=False)
    opis = db.Column(db.Text)
    ocena_nauczyciela = db.Column(db.Float, nullable=False )
    numer_telefonu = db.Column(db.String(12), nullable=False)
    stawka = db.Column(db.Integer, nullable=False)
    waluta = db.Column(db.String(3), nullable=False)
    email = db.Column(db.String(128), nullable=False)

    lekcja = db.relationship("Lesson")
    dostepnosc = db.relationship("TeacherCalendar")
    
    # def __init__(self, id_nauczyciela, imie, nazwisko, prowadzone_przedmioty, ocena_nauczyciela, numer_telefonu, email, stawka, waluta):
    #     self.id_nauczyciela = id_nauczyciela
    #     self.imie = imie
    #     self.nazwisko = nazwisko
    #     self.prowadzone_przedmioty = prowadzone_przedmioty
    #     self.ocena_nauczyciela = ocena_nauczyciela
    #     self.numer_telefonu = numer_telefonu
    #     self.email = email
    #     self.stawka = stawka 
    #     self.waluta = waluta

class Student (db.Model):
    __tablename__ = "Uczniowie"
    id_studenta = db.Column(db.Integer, primary_key=True)
    imie = db.Column(db.String(50), nullable=False)
    nazwisko = db.Column(db.String(50), nullable=False)
    email = db.Column(db.String(100), nullable=False)

    lessons = db.relationship("Lesson")
    def __repr__(self):
       return f"<Student(id={self.id_studenta}, imie='{self.imie}', nazwisko='{self.nazwisko}', email='{self.email}')>"

class SubjectsList(db.Model):
    __tablename__ = "Przedmioty"
    id_przedmiotu = db.Column(db.Integer, primary_key=True)
    nazwa_przedmiotu = db.Column(db.String(50), nullable=False)

    lessons = db.relationship('Lesson')

class Lesson (db.Model):
    __tablename__ = "Lekcje"
    id_lekcji = db.Column(db.Integer, primary_key=True)
    id_nauczyciela = db.Column(db.Integer, db.ForeignKey('Nauczyciele.id_nauczyciela'), nullable=False)
    id_studenta = db.Column(db.Integer, db.ForeignKey('Uczniowie.id_studenta'), nullable=False)
    id_przedmiotu = db.Column(db.Integer, db.ForeignKey('Przedmioty.id_przedmiotu'), nullable=False)
    data_lekcji = db.Column(db.Date, nullable=False)

    teacher = db.relationship('Teacher')
    student = db.relationship('Student')
    subject = db.relationship('SubjectsList')
    def __repr__(self):
        return f"<Lesson(id={self.id_lekcji}, imie_nauczyciela='{self.teacher.imie}, naziwsko_nauczyciela='{self.teacher.nazwisko}, przedmiot='{self.subject.nazwa_przedmiotu}', data_lekcji:'{self.data_lekcji}')"


class TeacherCalendar(db.Model):
    __tablename__ = "Kalendarz_nauczycieli"
    id = db.Column(db.Integer, primary_key=True)
    id_nauczyciela = db.Column(db.Integer, db.ForeignKey('Nauczyciele.id_nauczyciela'), nullable=False)
    dostepny_od = db.Column(db.Time, nullable=False)
    dostepny_do = db.Column(db.Time, nullable=False)

    teacher = db.relationship('Teacher')


with app.app_context():
    db.create_all()
    #db.drop_all()  


def populate_database():
    #Dodawanie przedmiotów
    subjects =  ['matematyka','fizyka','chemia', 'historia', 'biologia', 'geografia', 'WoS'] 
    for i, subject in enumerate(subjects):
        db.session.add(SubjectsList(id_przedmiotu=i + 1, nazwa_przedmiotu = subject))
    
    #Dodawanie nauczycieli
    teachers = [Teacher(id_nauczyciela=1, imie="Jan", nazwisko="Kowalski", prowadzone_przedmioty='["matematyka"]', opis="Doświadczony nauczyciel matematyki",
                        ocena_nauczyciela=4.5, numer_telefonu="123456789", stawka=100, waluta="PLN", email="jan.kowalski@example.com"),
                Teacher(id_nauczyciela=2, imie="Anna", nazwisko="Nowak", prowadzone_przedmioty='["fizyka", "chemia"]', opis="Specjalistka w naukach przyrodniczych", 
                        ocena_nauczyciela=4.8, numer_telefonu="987654321", stawka=120, waluta="PLN", email="anna.nowak@example.com"),
                Teacher(id_nauczyciela=3, imie="Marek", nazwisko="Wiśniewski", prowadzone_przedmioty='["historia"]', opis="Pasjonat historii",
                        ocena_nauczyciela=4.0, numer_telefonu="555666777", stawka=90, waluta="PLN", email="marek.wisniewski@example.com"),
                Teacher(id_nauczyciela=4, imie="Ewa", nazwisko="Zielińska", prowadzone_przedmioty='["biologia"]', opis="Ekspertka w biologii", 
                        ocena_nauczyciela=4.9, numer_telefonu="444333222", stawka=130, waluta="PLN", email="ewa.zielinska@example.com"),
                Teacher(id_nauczyciela=5, imie="Tomasz", nazwisko="Krawczyk", prowadzone_przedmioty='["matematyka", "fizyka"]', opis="Wszechstronny nauczyciel", 
                        ocena_nauczyciela=4.2, numer_telefonu="111222333", stawka=110, waluta="PLN", email="tomasz.krawczyk@example.com"),
                ]
    db.session.add_all(teachers)
    # Dodawanie studentów
    students = [
        Student(id_studenta=1, imie="Ola", nazwisko="Kwiatkowska", email="ola.k@example.com"),
        Student(id_studenta=2, imie="Piotr", nazwisko="Nowicki", email="piotr.n@example.com"),
        Student(id_studenta=3, imie="Karolina", nazwisko="Zając", email="karolina.z@example.com"),
    ]
    db.session.add_all(students)
    # Dodawanie lekcji
    lessons = [
        Lesson(id_lekcji=1, id_nauczyciela = 1, id_studenta=1, id_przedmiotu=1,data_lekcji=date(2024, 12, 10)),
        Lesson(id_lekcji=2, id_nauczyciela = 2, id_studenta=2, id_przedmiotu=2,data_lekcji=date(2024, 12, 7)),
        Lesson(id_lekcji=3, id_nauczyciela = 1, id_studenta=3, id_przedmiotu=1,data_lekcji=date(2024, 12, 11)),
        Lesson(id_lekcji=4, id_nauczyciela = 1, id_studenta=1, id_przedmiotu=1,data_lekcji=date(2024, 12, 11)),
        Lesson(id_lekcji=5, id_nauczyciela = 4, id_studenta=3, id_przedmiotu=1,data_lekcji=date(2024, 12, 12)),
        Lesson(id_lekcji=6, id_nauczyciela = 2, id_studenta=2, id_przedmiotu=3,data_lekcji=date(2024, 12, 8)),
        Lesson(id_lekcji=7, id_nauczyciela = 4, id_studenta=3, id_przedmiotu=1,data_lekcji=date(2024, 12, 13)),
        Lesson(id_lekcji=8, id_nauczyciela = 1, id_studenta=1, id_przedmiotu=1,data_lekcji=date(2024, 12, 12)),
        Lesson(id_lekcji=9, id_nauczyciela = 2, id_studenta=2, id_przedmiotu=5,data_lekcji=date(2024, 12, 14)),
        Lesson(id_lekcji=10, id_nauczyciela = 1, id_studenta=1, id_przedmiotu=1,data_lekcji=date(2024, 12, 11)),
    ]
    db.session.add_all(lessons)

    # Dodawanie kalendarza nauczycieli
    schedules = [
        TeacherCalendar(id=1, id_nauczyciela=1, dostepny_od=time(9, 0), dostepny_do=time(17, 0)),
        TeacherCalendar(id=2, id_nauczyciela=2, dostepny_od=time(8, 0), dostepny_do=time(16, 0)),
        TeacherCalendar(id=3, id_nauczyciela=3, dostepny_od=time(14, 0), dostepny_do=time(20, 0)),
        TeacherCalendar(id=4, id_nauczyciela=4, dostepny_od=time(8, 0), dostepny_do=time(13, 0)),
        TeacherCalendar(id=5, id_nauczyciela=5, dostepny_od=time(14, 0), dostepny_do=time(18, 0)),
    ]
    db.session.add_all(schedules)

    db.session.commit()

@app.route('/')
def index():
    return '<h1>Serwer działa!</h1>'

@app.route('/teacher-list', methods=['GET'])
def get_teacher_list():
    teachers = db.session.query(Teacher).all()
    response = [
        {
            "id_nauczyciela": teacher.id_nauczyciela,
            "imie": teacher.imie,
            "naziwsko": teacher.nazwisko,
            "proadzone_przedmioty": teacher.prowadzone_przedmioty
        } for teacher in teachers
    ]
    return jsonify(response), 200

@app.route('/teacher-details', methods=['GET'])
def get_teacher_details():
    id_nauczyciela = request.args.get('id_nauczyciela')
    nauczyciel = db.session.query(Teacher).filter(Teacher.id_nauczyciela == id_nauczyciela).first()
    if not nauczyciel:
        return jsonify({"error": "Nauczyciel nie istnieje"}), 404
    
    result = {
        "id_nauczyciela" : nauczyciel.id_nauczyciela,
        "imie" : nauczyciel.imie,
        "nazwisko" : nauczyciel.nazwisko,
        "prowadzone_przedmioty" : nauczyciel.prowadzone_przedmioty,
        "opis" : nauczyciel.opis,
        "ocena_nauczyciela" : nauczyciel.ocena_nauczyciela, 
        "numer_telefonu" : nauczyciel.numer_telefonu,
        "stawka" : nauczyciel.stawka,
        "waluta" : nauczyciel.waluta,
        "email" : nauczyciel.email,
        "dostepnosc" : [
            {
                "dostepny_od" : x.dostepny_od.strftime('%H:%M:%S'),
                "dostepny_do" : x.dostepny_do.strftime('%H:%M:%S'),
            } for x in nauczyciel.dostepnosc
        ]
    }
    return jsonify(result), 200

@app.route('/book-lesson', methods=['POST'])
def book_lesson():
    data = request.get_json()
    id_studenta = data.get('id_studenta')
    id_nauczyciela = data.get('id_nauczyciela')
    nazwa_przedmiotu = data.get('nazwa_przedmiotu')
    termin = data.get('termin')

    student = db.session.query(Student).filter(Student.id_studenta == id_studenta).first()
    nauczyciel = db.session.query(Teacher).filter(Teacher.id_nauczyciela == id_nauczyciela).first()
    przedmiot = db.session.query(SubjectsList).filter(SubjectsList.nazwa_przedmiotu == nazwa_przedmiotu).first()
    dostepnosc = db.session.query(TeacherCalendar).filter(TeacherCalendar.id_nauczyciela == id_nauczyciela).first()
    termin_date = datetime.strptime(termin, "%Y-%m-%dT%H:%M:%S")

    if termin_date.time() < dostepnosc.dostepny_od or termin_date.time() > dostepnosc.dostepny_do:
        return jsonify({"error" : "Nauczyciel nie jest dostępny w tych godzinach"}), 400
    
    other_lessosn = db.session.query(Lesson).filter(Lesson.id_nauczyciela == id_nauczyciela, Lesson.data_lekcji == termin_date.date()).all()
    for lesson in other_lessosn:
        if lesson.data_lekcji == termin_date.date():
            return jsonify({"error":"Nauczyciel jest już zajęty w podanym terminie!"}), 400
    try:
        new_lesson = Lesson(
            id_nauczyciela = id_nauczyciela,
            id_studenta = id_studenta,
            id_przedmiotu = przedmiot.id_przedmiotu,
            data_lekcji = termin_date.date()
        )    
        db.session.add(new_lesson)
        db.session.commit()
        return jsonify({"message": "Lekcja została zarezerwowana"}), 200
    except Exception as e:
        return jsonify({"error": "Nie udało się zarezerwować lekcji"}), 400

@app.route('/add-teacher', methods=['POST'])
def add_teacher():
    data = request.get_json()
    try:
        new_teacher = Teacher(
            imie = data['imie'],
            nazwisko = data['nazwisko'],
            prowadzone_przedmioty = data['prowadzone_przedmioty'],
            opis = data.get('opis'),
            ocena_nauczyciela = data['ocena_nauczyciela'],
            numer_telefonu = data['numer_telefonu'],
            stawka = data['stawka'],
            waluta = data['waluta'],
            email = data['email']
        )
        db.session.add(new_teacher)
        db.session.commit()
        return jsonify({"id_nauczyciela": new_teacher.id_nauczyciela}), 200
    except KeyError as ke:
        # Błąd związany z brakiem klucza w danych wejściowych
        return jsonify({"error": f"Brak wymaganej wartości: {str(ke)}"}), 400
    except Exception as e:
        # Inne błędy (np. problemy z bazą danych)
        print(f"Błąd: {str(e)}")  # Wypisanie błędu do konsoli (logowanie)
        return jsonify({"error": f"Wystąpił błąd: {str(e)}"}), 500

@app.route('/get-lessons', methods=['GET'])
def lessons_by_student_and_date():
    id_studenta = request.args.get('id_studenta')
    data_od = request.args.get('data_od')
    data_do = request.args.get('data_do')

    student = db.session.query(Student).filter(Student.id_studenta == id_studenta).first()
    if not student:
        return jsonify({"error": "Student nie istnieje"}), 404

    lessons = db.session.query(Lesson).filter(Lesson.id_studenta == id_studenta, Lesson.data_lekcji.between(data_od, data_do)).all()

    response = [
        {
            "data_lekcji" : lesson.data_lekcji,
            "id_nauczyciela" : lesson.id_nauczyciela,
            "imie_nauczyciela" : lesson.teacher.imie,
            "nazwisko_nauczyciela" : lesson.teacher.nazwisko,
            "przedmiot" : lesson.subject.nazwa_przedmiotu
        } for lesson in lessons
    ]
    return jsonify(response), 200

@app.route('/lessons', methods=['GET'])
def get_all_lessons():
    lessons = db.session.query(Lesson).all()
    response = [
        {
            "id_lekcji" : lesson.id_lekcji,
            "id_nauczyciela" : lesson.id_nauczyciela,
            "id_studenta" : lesson.id_studenta,
            "id_przedmiotu" : lesson.id_przedmiotu,
            "data" : lesson.data_lekcji
        } for lesson in lessons
    ]
    return jsonify(response), 200




if __name__ == '__main__':
    app.run(debug=True)
    #with app.app_context():
        #populate_database()
    