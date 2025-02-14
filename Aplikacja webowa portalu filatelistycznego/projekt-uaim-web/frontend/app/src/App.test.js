import React from 'react'; // Ensure React is imported
import { render, screen, fireEvent, waitFor, userEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import { BrowserRouter} from 'react-router-dom'; // or MemoryRouter
import Login from './components/Login'; // Importujemy komponent Login
import axios from 'axios';
import Swal from 'sweetalert2';
import App from './App';
import Znaczek from './components/Znaczek';
import ListaZnaczkow from './components/ListaZnaczkow';
import PanelStrony from './components/PanelStrony';
import DodawanieZnaczka from './components/DodawanieZnaczka';
import MockAdapter from 'axios-mock-adapter';
import Rejestracja from './components/Rejestracja';
import { MemoryRouter, useLocation  } from 'react-router-dom';


jest.mock('axios');  // Mockowanie axios
jest.mock('sweetalert2', () => ({
  fire: jest.fn(() => Promise.resolve()), // Mock Swal.fire to return a resolved promise
}));


//Mockowanie localStorage
beforeEach(() => {
  // Mockowanie setItem na localStorage
  global.Storage.prototype.setItem = jest.fn();
});

afterEach(() => {
  // Przywrócenie oryginalnego localStorage po teście
  jest.restoreAllMocks();
});




describe('Testy dla App', () => {
  it('Domyślne wyświetlanie strony logowania', () => {
    render (<App/>);
    screen.debug();
    expect(screen.getByPlaceholderText(/Login/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/Hasło/i)).toBeInTheDocument();
    expect(screen.getByText(/Zaloguj się/i)).toBeInTheDocument();
  });
  it('Przekierowanie na strone rejestracji', async () => {
    render(
      <App Router={MemoryRouter} initialEntries={['/login']} />
    );
    screen.debug();
    expect(screen.getByRole('link', { name: 'Zarejestruj się' })).toHaveAttribute('href', '/rejestracja')
  });
  it('Przekierowanie do /login gdy podczas próby osiągnięcia /panel bez tokena', () => {
    localStorage.removeItem('access_token'); // Ensure no token is present
    render(
      <App Router={MemoryRouter} initialEntries={['/panel']} />
    );
    screen.debug();
    expect(screen.getByPlaceholderText('Login')).toBeInTheDocument();
  });


});
afterEach(() => {
  // Przywrócenie oryginalnego localStorage po teście
  jest.restoreAllMocks();
});
describe('Testy dla Login', () => {
  it('Renderuje formularz logowania', () => {
    render(
      <MemoryRouter>
        <Login />
      </MemoryRouter>
    );
    expect(screen.getByPlaceholderText('Login')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Hasło')).toBeInTheDocument();
    expect(screen.getByText('Zaloguj się')).toBeInTheDocument();
  });
  
  it('Wyświetla odpowiednią wiadomość przy poprawnym logowaniu', async () => {
    axios.post.mockResolvedValue({ data: { access_token: 'fake_token' } });
    render(
      <MemoryRouter>
        <Login />
      </MemoryRouter>
    );
  
    fireEvent.change(screen.getByPlaceholderText('Login'), { target: { value: 'testuser' } });
    fireEvent.change(screen.getByPlaceholderText('Hasło'), { target: { value: 'password' } });
    fireEvent.click(screen.getByText('Zaloguj się'));
    await waitFor(() => expect(axios.post).toHaveBeenCalledWith('http://127.0.0.1:5000/logowanie', {
      login: 'testuser',
      haslo: 'password',
    }));
    await waitFor(() => expect(localStorage.setItem).toHaveBeenCalledWith('access_token', 'fake_token'));

    // Check if the success alert was fired
    await waitFor(() => expect(Swal.fire).toHaveBeenCalledWith(expect.objectContaining({
      icon: 'success',
      title: 'Hura!',
      text: 'Zalogowano pomyślnie.',
    })));
  
  });
  
  it('Wyświetla powiadomienie o błędzie', async () => {
    axios.post.mockRejectedValue(new Error('Failed to login'));
    render(
      <MemoryRouter>
        <Login />
      </MemoryRouter>
    );
  
    fireEvent.change(screen.getByPlaceholderText('Login'), {
      target: { value: 'testuser' },
    });
    fireEvent.change(screen.getByPlaceholderText('Hasło'), {
      target: { value: 'wrongpassword' },
    });
    fireEvent.click(screen.getByText('Zaloguj się'));
  
    await waitFor(() => expect(Swal.fire).toHaveBeenCalledWith(expect.objectContaining({
      icon: 'error',
      title: 'Oops...',
      text: 'Logowanie nie powiodło się.',
    })));
  });
});

describe('Testy rejestracji', () => {
  it('Poprawnie wyśiwetla formularz rejestracji',async () => {

    render(<BrowserRouter>
      <Rejestracja/>
  </BrowserRouter>);
    expect(screen.getByPlaceholderText(/Login/i)).toBeInTheDocument();
    expect(screen.getByPlaceholderText(/Hasło/i)).toBeInTheDocument();
    expect(screen.getByText(/Zarejestruj się/i)).toBeInTheDocument();
  });


  it('pokazuje komunikat sukcesu po udanej rejestracji', async () => {
    axios.post.mockResolvedValueOnce({ data: { message: 'Zarejestrowano pomyślnie.' } });
    render(<BrowserRouter>
        <Rejestracja/>
    </BrowserRouter>);
  
    // Wypełnienie formularza
    fireEvent.change(screen.getByPlaceholderText('Login'), { target: { value: 'testuser' } });
    fireEvent.change(screen.getByPlaceholderText('Hasło'), { target: { value: 'securepassword' } });
    fireEvent.click(screen.getByText('Zarejestruj się'));
  
    await waitFor(() => expect(axios.post).toHaveBeenCalledWith('http://127.0.0.1:5000/rejestracja', {
      login: 'testuser',
      haslo: 'securepassword',
    }));
    //await waitFor(() => expect(localStorage.setItem).toHaveBeenCalledWith('access_token', 'fake_token'));

    // Check if the success alert was fired
    await waitFor(() => expect(Swal.fire).toHaveBeenCalledWith(expect.objectContaining({
      icon: 'success',
      title: 'Hura!',
      text: 'Zarejestrowano pomyślnie.',
    })));


  });
  
  it('pokazuje komunikat błędu po nieudanej rejestracji', async () => {
    render(<BrowserRouter>
      <Rejestracja/>
  </BrowserRouter>);
  
    // Wypełnienie formularza z błędnymi danymi
    fireEvent.change(screen.getByPlaceholderText('Login'), { target: { value: 'error' } });
    fireEvent.change(screen.getByPlaceholderText('Hasło'), { target: { value: 'securepassword' } });
    fireEvent.click(screen.getByText('Zarejestruj się'));
  
    // Oczekiwanie na rezultat
    await waitFor(() => expect(Swal.fire).toHaveBeenCalledWith(expect.objectContaining({
      icon: 'error',
      title: 'Oops...',
      text: 'Rejestracja nie powiodła się.',
    })));
  });


});


describe('Testy dla PanelStrony', () => {
  beforeEach(() => {
    axios.get.mockClear(); // Clear mocks between tests
  });
  beforeEach(() => {
    // Tworzenie mocka localStorage
    const localStorageMock = (() => {
      let store = {};
      return {
        getItem: (key) => store[key] || null,
        setItem: (key, value) => {
          store[key] = value;
        },
        removeItem: (key) => {
          delete store[key];
        },
        clear: () => {
          store = {};
        },
      };
    })();
  
    Object.defineProperty(window, 'localStorage', {
      value: localStorageMock,
    });
  });

  test('renderuje poprawnie przycisk wylogowania i nagłówek', () => {
    axios.get.mockResolvedValueOnce({ data: [] }); // Mock for this test
    render(
      <MemoryRouter>
        <PanelStrony />
      </MemoryRouter>
    );

    expect(screen.getByText('Wyloguj się')).toBeInTheDocument();
    expect(screen.getByText('Portal Filatelistyczny')).toBeInTheDocument();
  });

  test('wylogowuje użytkownika po kliknięciu "Wyloguj się"', async () => {

    axios.get.mockResolvedValueOnce({
      data: [], // Przykładowa odpowiedź API dla listy znaczków
    });
    localStorage.setItem('access_token', 'testowy_token');

    render(
      <MemoryRouter>
        <PanelStrony />
      </MemoryRouter>
    );
    expect(axios.get).toHaveBeenCalledWith('http://127.0.0.1:5000/znaczki', {
      headers: { Authorization: 'Bearer testowy_token' },
    });

  
    // Sprawdzamy, czy token został dodany
    expect(localStorage.getItem('access_token')).toBe('testowy_token');
  
    // Klikamy przycisk "Wyloguj się"
    const button = screen.getByText(/Wyloguj się/i);
    fireEvent.click(button);
  
    // Sprawdzamy, czy token został usunięty
    expect(localStorage.getItem('access_token')).toBeNull();
  });
});

// Testy dla ListaZnaczkow
const mockAxios = new MockAdapter(axios);

describe('Testy dla ListaZnaczkow',  () => {
  afterEach(() => {
    mockAxios.reset(); // Resetuj mock po każdym teście
  });
  test('Renderuje listę znaczków', async () => {
   

    const znaczkiMockData = [
      { 
        id: 1, 
        nazwa: 'Znaczek A', 
        opis: 'Opis A', 
        stan: 'Nowy', 
        wysokosc: 5, 
        szerokosc: 4, 
        liczba_transakcji: 2 
      },
      { 
        id: 2, 
        nazwa: 'Znaczek B', 
        opis: 'Opis B', 
        stan: 'Używany', 
        wysokosc: 6, 
        szerokosc: 3, 
        liczba_transakcji: 3 
      },
    ];
  
    // Mockowanie odpowiedzi z axios
    axios.get.mockResolvedValue({
      data: znaczkiMockData,
    });
  
    render(<ListaZnaczkow/>);
    screen.debug();
    await waitFor(() => {
      expect(screen.getByText(/Znaczek A/)).toBeInTheDocument();
      expect(screen.getByText(/Znaczek B/)).toBeInTheDocument();
    });
  });

  test('pokazuje komunikat błędu, gdy żądanie API się nie powiedzie', async () => {
    axios.get.mockRejectedValue(new Error('Błąd pobierania'));

  // Renderowanie komponentu
    render(<ListaZnaczkow />);

  // Czekamy, aż funkcja fetchZnaczki zakończy się (w przypadku błędu).
    await waitFor(() => {
    // Sprawdzamy, czy wyświetla się komunikat o błędzie z SweetAlert2
      expect(Swal.fire).toHaveBeenCalledWith({
        icon: 'error',
        title: 'Błąd',
        text: 'Wystąpił błąd przy wyświetlaniu listy.',
        timer: 1200,
        width: '300px',
        height: '300px',
        showConfirmButton: false,
      });
    });
  });
});


// Testy dla Znaczek
describe('Testy dla Znaczek', () => {
  const mockZnaczek = {
    id: 1,
    nazwa: 'Znaczek Testowy',
    opis: 'Opis Testowy',
    stan: 'Dobry',
    liczba_transakcji: 3,
    szerokosc: 2,
    wysokosc: 4,
    zdjecie: null,
  };

  test('renderuje szczegóły znaczków', () => {
    render(
      <MemoryRouter>
        <Znaczek znaczek={mockZnaczek} onTransactionAdded={jest.fn()} />
      </MemoryRouter>
    );

    expect(screen.getByText('Nazwa: Znaczek Testowy')).toBeInTheDocument();
    expect(screen.getByText('Opis: Opis Testowy')).toBeInTheDocument();
    expect(screen.getByText('Stan: Dobry')).toBeInTheDocument();
  });

  test('pobiera raport po kliknięciu "Pobierz Raport"', async () => {
    axios.get.mockResolvedValueOnce({ data: { raport: 'Raport Testowy' } });

    render(
      <MemoryRouter>
        <Znaczek znaczek={mockZnaczek} onTransactionAdded={jest.fn()} />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText('Pobierz Raport'));

    await waitFor(() => {
      expect(axios.get).toHaveBeenCalledWith(
        'http://127.0.0.1:5000/znaczek/1/raport',
        expect.any(Object) // headers
      );
    });
  });

  test('otwiera formularz po kliknięciu "Dodaj Transakcję"', () => {
    render(
      <MemoryRouter>
        <Znaczek znaczek={mockZnaczek} onTransactionAdded={jest.fn()} />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText('Dodaj Transakcję'));

    expect(screen.getByText('X')).toBeInTheDocument(); // Przycisk zamknięcia modala
  });
});

describe('Testy dla Dodawanie Znaczka', () => {
  const onZnaczekAddedMock = jest.fn();

  beforeEach(() => {
    onZnaczekAddedMock.mockClear();
    jest.clearAllMocks();
  });

  test('Renderowanie wszystkich pół inputu', () => {
    render(<DodawanieZnaczka onZnaczekAdded={onZnaczekAddedMock} />);

    // Check if input fields are rendered
    expect(screen.getByPlaceholderText('Nazwa')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Opis')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Stan')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Wysokość [cm]')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Szerokość [cm]')).toBeInTheDocument();
    expect(screen.getByText('Wybierz plik')).toBeInTheDocument();
  });

  test('Wyświetla błąd przy złym rozszerzeniu', async () => {
    const { getByLabelText } = render(<DodawanieZnaczka onZnaczekAdded={onZnaczekAddedMock} />);
    const invalidFile = new File([''], 'file.txt', { type: 'text/plain' });

    fireEvent.change(getByLabelText(/wybierz plik/i), { target: { files: [invalidFile] } });

    fireEvent.click(screen.getByText('Dodaj Znaczek'));

    await waitFor(() => {
      expect(Swal.fire).toHaveBeenCalledWith({
        icon: 'error',
        title: 'Oops...',
        text: 'Tylko pliki PNG lub JPEG są dozwolone.',
        timer: 1200,
        showConfirmButton: false,
      });
    });
  });

  test('Wyświetla błąd przy błędnych wymiarach', async () => {
    render(<DodawanieZnaczka onZnaczekAdded={onZnaczekAddedMock} />);

    fireEvent.change(screen.getByPlaceholderText('Wysokość [cm]'), { target: { value: '-1' } });
    fireEvent.change(screen.getByPlaceholderText('Szerokość [cm]'), { target: { value: '10' } });

    fireEvent.click(screen.getByText('Dodaj Znaczek'));

    await waitFor(() => {
      expect(Swal.fire).toHaveBeenCalledWith({
        icon: 'error',
        title: 'Oops...',
        text: 'Wysokość nie może być ujemna.',
        timer: 1200,
        width: '300px',
        height: '300px',
        showConfirmButton: false,
      });
    });
  });

  test('Poprawnie wysyła formularz', async () => {
    const { getByLabelText, getByPlaceholderText } = render(
      <DodawanieZnaczka onZnaczekAdded={onZnaczekAddedMock} />
    );

    // Fill out the form
    fireEvent.change(getByPlaceholderText('Nazwa'), { target: { value: 'Znaczek testowy' } });
    fireEvent.change(getByPlaceholderText('Opis'), { target: { value: 'Opis testowy' } });
    fireEvent.change(getByPlaceholderText('Stan'), { target: { value: 'Nowy' } });
    fireEvent.change(getByPlaceholderText('Wysokość [cm]'), { target: { value: '50' } });
    fireEvent.change(getByPlaceholderText('Szerokość [cm]'), { target: { value: '50' } });

    const validFile = new File([''], 'image.jpg', { type: 'image/jpeg' });
    fireEvent.change(getByLabelText(/wybierz plik/i), { target: { files: [validFile] } });

    axios.post.mockResolvedValueOnce({}); // Mock successful API response

    fireEvent.click(screen.getByText('Dodaj Znaczek'));

    await waitFor(() => {
      expect(Swal.fire).toHaveBeenCalledWith({
        icon: 'success',
        title: 'Hura!',
        text: 'Znaczek dodany pomyślnie!',
        timer: 1200,
        width: '300px',
        height: '300px',
        showConfirmButton: false,
      });
    });

    expect(onZnaczekAddedMock).toHaveBeenCalled();
  });

  test('Wyświetla błąd gdy zapytanie do API się niepowiedzie', async () => {
    const { getByLabelText, getByPlaceholderText } = render(
      <DodawanieZnaczka onZnaczekAdded={onZnaczekAddedMock} />
    );

    // Fill out the form
    fireEvent.change(getByPlaceholderText('Nazwa'), { target: { value: 'Znaczek testowy' } });
    fireEvent.change(getByPlaceholderText('Opis'), { target: { value: 'Opis testowy' } });
    fireEvent.change(getByPlaceholderText('Stan'), { target: { value: 'Nowy' } });
    fireEvent.change(getByPlaceholderText('Wysokość [cm]'), { target: { value: '50' } });
    fireEvent.change(getByPlaceholderText('Szerokość [cm]'), { target: { value: '50' } });

    const validFile = new File([''], 'image.jpg', { type: 'image/jpeg' });
    fireEvent.change(getByLabelText(/wybierz plik/i), { target: { files: [validFile] } });

    axios.post.mockRejectedValueOnce(new Error('API call failed')); // Mock API failure

    fireEvent.click(screen.getByText('Dodaj Znaczek'));

    await waitFor(() => {
      expect(Swal.fire).toHaveBeenCalledWith({
        icon: 'error',
        title: 'Oops...',
        text: 'Wystąpił błąd przy dodawaniu znaczka.',
        timer: 1200,
        width: '300px',
        height: '300px',
        showConfirmButton: false,
      });
    });
  });
});