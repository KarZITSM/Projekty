import csv
import random
import incremental_aq as n_version
import matplotlib.pyplot as plt

"""
Test na zbiorze irysów - dostosowany do obecnej wersji algorytmu
"""
def load_iris_data_from_csv(file_path):
    dataset = []
    with open(file_path, 'r') as file:
        reader = csv.reader(file)
        for row in reader:
            dataset.append({
                'features': {
                    'sepal_length': float(row[0]),
                    'sepal_width': float(row[1]),
                    'petal_length': float(row[2]),
                    'petal_width': float(row[3])
                },
                'class': row[4]
            })
    return dataset


def split_dataset_into_four(data, batch_ratio=0.1, incremental_ratio=0.4):
    """
    Dzieli dane na cztery zbiory: treningowy, trzy inkrementacyjne i testowy.
    """
    random.shuffle(data)
    train_size = int(len(data) * batch_ratio)
    incremental_size = int(len(data) * incremental_ratio / 3)
    train_data = data[:train_size]
    incremental_data_1 = data[train_size:train_size + incremental_size]
    incremental_data_2 = data[train_size + incremental_size:train_size + 2 * incremental_size]
    incremental_data_3 = data[train_size + 2 * incremental_size:train_size + 3 * incremental_size]
    classification_data = data[train_size + 3 * incremental_size:]
    return train_data, incremental_data_1, incremental_data_2, incremental_data_3, classification_data


def classify_manual_example(algorithm):
    """
    Klasyfikacja ręcznie wprowadzonego przykładu.
    """
    example = {
        'features': {
            'sepal_length': float(input("Podaj sepal_length: ")),
            'sepal_width': float(input("Podaj sepal_width: ")),
            'petal_length': float(input("Podaj petal_length: ")),
            'petal_width': float(input("Podaj petal_width: "))
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
            split_dataset_into_four(data, batch_ratio=batch_ratio, incremental_ratio=incremental_ratio))

        # Inicjalizacja algorytmu
        algorithm = n_version.RuleInduction(attributes)

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

def main():

    file_path = "iris.data"
    data = load_iris_data_from_csv(file_path)
    attributes = {
        'sepal_length': {'range': (4.0, 8.0), 'step': 0.1},
        'sepal_width': {'range': (2.0, 4.5), 'step': 0.1},
        'petal_length': {'range': (1.0, 7.0), 'step': 0.1},
        'petal_width': {'range': (0.1, 2.5), 'step': 0.1}
    }
    # Definicja atrybutów
    batch_ratio = 0.05 # Do testów
    incremental_ratio = 0.2 # Do testów

    train_data, incremental_data_1, incremental_data_2, incremental_data_3, classification_data = (
        split_dataset_into_four(data, batch_ratio=batch_ratio, incremental_ratio=incremental_ratio))


    algorithm = n_version.RuleInduction(attributes)

    # print("\n=== Trening wsadowy ===")
    # algorithm.batch_initialization(train_data)
    # results_after_batch_training = algorithm.evaluate(classification_data)
    # print("\nWyniki po wsadowym uczeniu:")
    # for key, value in results_after_batch_training.items():
    #     print(f"{key}: {value}")
    #
    # print("\n=== Pierwsza inkrementacja ===")
    # algorithm.process_examples(incremental_data_1)
    # results_after_first_increment = algorithm.evaluate(classification_data)
    # print("\nWyniki po pierwszej inkrementacji:")
    # for key, value in results_after_first_increment.items():
    #     print(f"{key}: {value}")
    #
    # print("\n=== Druga inkrementacja ===")
    # algorithm.process_examples(incremental_data_2)
    # results_after_second_increment = algorithm.evaluate(classification_data)
    # print("\nWyniki po drugiej inkrementacji:")
    # for key, value in results_after_second_increment.items():
    #     print(f"{key}: {value}")
    #
    # print("\n=== Trzecia inkrementacja ===")
    # algorithm.process_examples(incremental_data_3)
    # results_after_third_increment = algorithm.evaluate(classification_data)
    # print("\nWyniki po trzeciej inkrementacji:")
    # for key, value in results_after_third_increment.items():
    #     print(f"{key}: {value}")
    #
    # # Klasyfikacja ręcznego przykładu
    # print("\nAlgorytm gotowy do klasyfikacji ręcznie wprowadzonego przykładu!")
    # while True:
    #     classify_manual_example(algorithm)

    simulate_and_visualize(data, attributes, num_simulations=100, batch_ratio=0.30, incremental_ratio=0.1)

if __name__ == "__main__":
    main()
