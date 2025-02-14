from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import CheckConstraint
from datetime import date, time
from sqlalchemy.sql import func


app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///school.db'
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


def f1_weekly_students():
    weekly_students= db.session.query(Lesson.id_studenta).join(Student).filter(func.strftime('%w', Lesson.data_lekcji).in_(['1', '2', '3', '4', '5'])).distinct().count()
    return weekly_students

def f2_weekends_teachers():
    query = db.session.query(Lesson.id_nauczyciela).join(Teacher).filter(func.strftime('%w', Lesson.data_lekcji).in_(['0','6'])).distinct().count()
    return query
    
def f3_student_with_most_lessons():
    subquery = db.session.query(Lesson.id_studenta, func.count(Lesson.id_lekcji).label('ilosc')).group_by(Lesson.id_studenta).order_by(func.count(Lesson.id_lekcji).desc()).first()
    if subquery:
        student = Student.query.get(subquery.id_studenta)
        return student
    return None
def f4_most_often_subject():
    query = (
         db.session.query(SubjectsList.nazwa_przedmiotu, func.count(Lesson.id_lekcji).label('ilosc'))
         .join(Lesson, Lesson.id_przedmiotu == SubjectsList.id_przedmiotu)
         .group_by(SubjectsList.nazwa_przedmiotu)
         .order_by(func.count(Lesson.id_lekcji).desc())
         .first()
    )
    return query

def f5_how_many_math_lessons():
    math_subject = SubjectsList.query.filter_by(nazwa_przedmiotu='matematyka').first()
    math_lessons = db.session.query(Lesson).filter_by(id_przedmiotu=math_subject.id_przedmiotu).count()

    return math_lessons

def f6_how_many_lessons_on_wednesday():
    query =  db.session.query(Lesson.id_lekcji).filter(func.strftime('%w',Lesson.data_lekcji).in_(['3'])).distinct().count()
    return query

def f7_teachers_lesson_on_day(nauczyciel_id, dzien):
    query = db.session.query(Lesson).filter(Lesson.id_nauczyciela == nauczyciel_id, Lesson.data_lekcji == dzien)
    return query.all()


if __name__ == '__main__':
    with app.app_context():
       # populate_database()
        print("Database populated!")
        print("F1: Ilość studentów z lekcjami w dni powszednie:", f1_weekly_students())
        print("F2: Ilość nauczycieli z lekcjami w weekedny:", f2_weekends_teachers())
        print("F3: Student z największą ilością lekcji:", f3_student_with_most_lessons())
        print("F4: Najczęściej umawiany przedmiot:", f4_most_often_subject())
        print("F5: Ilość lekcji z matematyki:", f5_how_many_math_lessons())
        print("F6: Ilość lekcji w środy:", f6_how_many_lessons_on_wednesday())
        print("F7:", f7_teachers_lesson_on_day(1, date(2024, 12, 11)))