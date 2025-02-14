import csv
import random
import incremental_aq as number_v
import matplotlib.pyplot as plt

# Funkcja do wczytania danych o pingwinach
def load_penguin_data(file_path):
    dataset = []
    with open(file_path, 'r') as file:
        reader = csv.DictReader(file)
        for row in reader:
            # Pomijanie brakujących wartości
            if any(value == "NA" for value in row.values()):
                continue
            dataset.append({
                'features': {
                    'island': row['island'],
                    'bill_length_mm': float(row['bill_length_mm']),
                    'bill_depth_mm': float(row['bill_depth_mm']),
                    'flipper_length_mm': int(row['flipper_length_mm']),
                    'body_mass_g': int(row['body_mass_g']),
                    'sex': row['sex']
                },
                'class': row['species']  # Gatunek pingwina jako klasa
            })
    return dataset

# Funkcja do podziału zbioru na wsadowy, inkrementacyjne i testowy
def split_penguin_data(data, batch_ratio=0.05, incremental_ratio=0.15):
    random.shuffle(data)
    batch_size = int(len(data) * batch_ratio)
    incremental_size = int(len(data) * incremental_ratio / 3)
    train_data = data[:batch_size]
    incremental_data_1 = data[batch_size:batch_size + incremental_size]
    incremental_data_2 = data[batch_size + incremental_size:batch_size + 2 * incremental_size]
    incremental_data_3 = data[batch_size + 2 * incremental_size:batch_size + 3 * incremental_size]
    test_data = data[batch_size + 3 * incremental_size:]
    return train_data, incremental_data_1, incremental_data_2, incremental_data_3, test_data

# Funkcja do testowania algorytmu
def test_algorithm(algorithm, train_data, incremental_data, test_data):
    print("=== Test algorytmu Pingwiny ===")

    print("\n=== Trening wsadowy ===")
    algorithm.batch_initialization(train_data)
    results_after_batch_training = algorithm.evaluate(test_data)
    print("\nWyniki po wsadowym uczeniu:")
    for key, value in results_after_batch_training.items():
        print(f"{key}: {value}")

    for i, data in enumerate(incremental_data, 1):
        print(f"\n=== Inkrementacja {i} ===")
        algorithm.process_examples(data)
        results_after_increment = algorithm.evaluate(test_data)
        print(f"\nWyniki po inkrementacji {i}:")
        for key, value in results_after_increment.items():
            print(f"{key}: {value}")

# Funkcja do klasyfikacji ręcznego przykładu
def classify_manual_example(algorithm):
    print("\n=== Klasyfikacja ręcznego przykładu ===")
    example = {
        'features': {
            'island': input("island (Torgersen/Biscoe/Dream): "),
            'bill_length_mm': float(input("bill_length_mm: ")),
            'bill_depth_mm': float(input("bill_depth_mm: ")),
            'flipper_length_mm': int(input("flipper_length_mm: ")),
            'body_mass_g': int(input("body_mass_g: ")),
            'sex': input("sex (male/female): ")
        }
    }
    predicted_class = algorithm.classify(example)
    print(f"\nPrzykład został sklasyfikowany jako: {predicted_class}")


def simulate_and_visualize(data, attributes, num_simulations=50, batch_ratio=0.05, incremental_ratio=0.15):
    """
    Przeprowadza symulacje i wizualizuje wyniki dokładności dla wsadowej inicjalizacji oraz kolejnych inkrementacji.
    """
    # Zmienna do przechowywania dokładności dla każdej fazy w każdej symulacji
    accuracies = {
        "Batch Initialization": [],
        "Increment 1": [],
        "Increment 2": [],
        "Increment 3": []
    }

    for _ in range(num_simulations):
        # Podział danych na cztery zbiory
        train_data, incremental_data_1, incremental_data_2, incremental_data_3, classification_data = (
            split_penguin_data(data, batch_ratio=batch_ratio, incremental_ratio=incremental_ratio))

        # Inicjalizacja algorytmu
        algorithm = number_v.RuleInduction(attributes)

        # Faza wsadowa
        algorithm.batch_initialization(train_data)
        batch_accuracy = algorithm.evaluate(classification_data)['correct_rate']
        accuracies["Batch Initialization"].append(batch_accuracy)

        # Pierwsza inkrementacja
        algorithm.process_examples(incremental_data_1)
        increment_1_accuracy = algorithm.evaluate(classification_data)['correct_rate']
        accuracies["Increment 1"].append(increment_1_accuracy)

        # Druga inkrementacja
        algorithm.process_examples(incremental_data_2)
        increment_2_accuracy = algorithm.evaluate(classification_data)['correct_rate']
        accuracies["Increment 2"].append(increment_2_accuracy)

        # Trzecia inkrementacja
        algorithm.process_examples(incremental_data_3)
        increment_3_accuracy = algorithm.evaluate(classification_data)['correct_rate']
        accuracies["Increment 3"].append(increment_3_accuracy)

    # Obliczenie średnich dokładności
    average_accuracies = {phase: sum(values) / len(values) for phase, values in accuracies.items()}

    # Wizualizacja histogramów
    plt.figure(figsize=(14, 10))
    for i, (phase, values) in enumerate(accuracies.items()):
        plt.subplot(2, 2, i + 1)
        plt.hist(values, bins=10, alpha=0.7, color='blue', edgecolor='black')
        avg_accuracy = average_accuracies[phase]
        plt.axvline(avg_accuracy, color='red', linestyle='dashed', linewidth=1.5, label=f'Średnia: {avg_accuracy:.2f}%')
        plt.title(f'Dokładność: {phase}')
        plt.xlabel('Dokładność (%)')
        plt.ylabel('Liczba symulacji')
        plt.legend()
        plt.grid(axis='y', linestyle='--', alpha=0.7)

    plt.tight_layout()
    plt.show()

    # Wyświetlenie średnich dokładności
    print("\nŚrednie dokładności:")
    for phase, avg in average_accuracies.items():
        print(f"{phase}: {avg:.2f}%")    



# Główna funkcja
def main():
    file_path = "pinguins.csv"  # Upewnij się, że plik CSV znajduje się w tym samym katalogu co skrypt
    data = load_penguin_data(file_path)

    # Definicja atrybutów (bez 'species')
    attributes = {
        'island': ['Torgersen', 'Biscoe', 'Dream'],
        'bill_length_mm': {'range': (32.1, 59.6), 'step': 0.1},
        'bill_depth_mm': {'range': (13.1, 21.5), 'step': 0.1},
        'flipper_length_mm': {'range': (172, 231), 'step': 1},
        'body_mass_g': {'range': (2700, 6300), 'step': 25},
        'sex': ['male', 'female']
    }
    simulate_and_visualize(data, attributes, num_simulations=100, batch_ratio=0.05, incremental_ratio=0.2)
    # # Podział danych na zbiory
    # train_data, incremental_data_1, incremental_data_2, incremental_data_3, test_data = split_penguin_data(data)

    # # Inicjalizacja algorytmu
    # algorithm = number_v.RuleInduction(attributes)

    # # Testowanie algorytmu
    # incremental_data = [incremental_data_1, incremental_data_2, incremental_data_3]
    # test_algorithm(algorithm, train_data, incremental_data, test_data)

    # Klasyfikacja ręcznego przykładu
    #classify_manual_example(algorithm)

if __name__ == "__main__":
    main()
