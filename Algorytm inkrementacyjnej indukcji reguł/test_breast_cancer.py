import csv
import random
import incremental_aq as n_version
import matplotlib.pyplot as plt

from sklearn.metrics import confusion_matrix, roc_curve, auc, ConfusionMatrixDisplay
import numpy as np

"""
Test na zbiorze raka - DOSTOSOWANY do obecnej wersji algorytmu (brak inkrementacji)
"""
def load_cancer_data_from_csv(file_path):
    dataset = []
    with open(file_path, 'r') as file:
        reader = csv.reader(file, delimiter = ',')
        for row in reader:
            if '?' in row:  # Pomijanie brakujących danych
                continue
            if len(row) < 11:
                continue
            dataset.append({
                'features': {
                    "Clump Thickness": int(row[1]),
                    "Uniformity of Cell Size": int(row[2]),
                    "Uniformity of Cell Shape": int(row[3]),
                    "Marginal Adhesion": int(row[4]),
                    "Single Epithelial Cell Size": int(row[5]),
                    "Bare Nuclei": int(row[6]),
                    "Bland Chromatin": int(row[7]),
                    "Normal Nucleoli": int(row[8]),
                    "Mitoses": int(row[9])
                },
                'class': int(row[10])  # Klasa: 2 lub 4
            })
    return dataset

# Funkcja do losowego podziału danych na zbiory
def split_dataset(data, train_ratio=0.10):
    random.shuffle(data)  # Losowe przemieszanie danych
    train_size = int(len(data) * train_ratio)
    train_data = data[:train_size]
    test_data = data[4*train_size:]
    incremental_data = data[train_size:4*train_size]
    return train_data, test_data, incremental_data


def split_dataset_2_incremental(data, batch_ratio = 0.1, incremental_ratio = 0.40):
    random.shuffle(data)
    train_size = int(len(data) * batch_ratio)
    train_data = data[:train_size]

    incremental_size = int(len(data) * incremental_ratio / 2)
    incremental_data_1 = data[train_size:train_size + incremental_size]
    incremental_data_2 = data[train_size + incremental_size: train_size + 2*incremental_size]

    test_data = data[train_size + 2*incremental_size:]
    
    return train_data, incremental_data_1, incremental_data_2, test_data


def simulate_and_visualize(data, attributes, num_simulations=50):
    """
    Przeprowadza symulacje i wizualizuje wyniki dokładności dla wsadowej inicjalizacji oraz kolejnych inkrementacji.
    """
    # Zmienna do przechowywania dokładności dla każdej fazy w każdej symulacji
    accuracies = {
        "Batch Initialization": [],
        "Increment 1": []
    }
    x = 0
    for _ in range(num_simulations):
        x = x + 1
        print(str(x) + "-owa iteracja")
        # Podział danych na zbiór inkrementacyjny, testowy i treningowy
        train_data, test_data, incremental_data = split_dataset(data)

        # Inicjalizacja algorytmu
        algorithm = n_version.RuleInduction(attributes)

        # Faza wsadowa
        algorithm.batch_initialization(train_data)
        batch_accuracy = algorithm.evaluate(test_data)['correct_rate']
        accuracies["Batch Initialization"].append(batch_accuracy)

        # Pierwsza inkrementacja
        algorithm.process_examples(incremental_data)
        increment_1_accuracy = algorithm.evaluate(test_data)['correct_rate']
        accuracies["Increment 1"].append(increment_1_accuracy)

    


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

def simulation_and_creating_confusion_matrix_and_ROC(data, attributes, train_ratio = 0.20):
    return -1



def main():
# Definicja zakresów dla danych liczbowych
    attributes = {
        "Clump Thickness": {'range': (1, 10), 'step': 1},
        "Uniformity of Cell Size": {'range': (1, 10), 'step': 1},
        "Uniformity of Cell Shape": {'range': (1, 10), 'step': 1},
        "Marginal Adhesion": {'range': (1, 10), 'step': 1},
        "Single Epithelial Cell Size": {'range': (1, 10), 'step': 1},
        "Bare Nuclei": {'range': (1, 10), 'step': 1},
        "Bland Chromatin": {'range': (1, 10), 'step': 1},
        "Normal Nucleoli": {'range': (1, 10), 'step': 1},
        "Mitoses": {'range': (1, 10), 'step': 1},
    }

    # Ścieżka do pliku CSV (zastąp 'breast_cancer.csv' właściwą nazwą pliku)
    file_path = "breast-cancer-wisconsin.data"

    # Wczytanie danych z CSV
    data = load_cancer_data_from_csv(file_path)
    simulate_and_visualize(data, attributes, num_simulations=20)
    #
    # train_data, incremental_data_1, incremental_data_2, test_data = split_dataset_2_incremental(data)

    # algorithm = n_version.RuleInduction(attributes)

    # #Zbiór treningowy 
    # print("Trening algorytmu na zbiorze nowotworów...")
    
    # algorithm.batch_initialization(train_data)
    # results_after_batch = algorithm.evaluate(test_data)
    

    # print("\nPierwsza inkrementacja:")
    # algorithm.process_examples(incremental_data_1)
    # results_after_1_incr = algorithm.evaluate(test_data)
    # results_after_1_incr_matrix = algorithm.evaluate_for_matrix_ROC(test_data)
    # print("\nDruga inkrementacja")
    # algorithm.process_examples(incremental_data_2)
    # results_after_2_incr = algorithm.evaluate(test_data)
    # results_after_2_incr_matrix = algorithm.evaluate_for_matrix_ROC(test_data)

    # print("\nWyniki po inicjalizacji wsadowej:")
    # print(f"Dokładność: {results_after_batch['correct_rate']}%")
    # print(f"Błędna klasyfikacja: {results_after_batch['error_rate']}%")
    # print(f"Poprawne klasyfikacje: {results_after_batch['correct']}")
    # print(f"Błędne klasyfikacje: {results_after_batch['incorrect']}")
    # print(f"Całkowita liczba przykładów: {results_after_batch['total']}")

    # print("")
    # print("\nWyniki po pierwszej inkrementaci:")
    # print(f"Dokładność: {results_after_1_incr['correct_rate']}%")
    # print(f"Błędna klasyfikacja: {results_after_1_incr['error_rate']}%")
    # print(f"Poprawne klasyfikacje: {results_after_1_incr['correct']}")
    # print(f"Błędne klasyfikacje: {results_after_1_incr['incorrect']}")
    # print(f"Całkowita liczba przykładów: {results_after_1_incr['total']}")
    

    # true_labels = [example['class'] for example in test_data]
    # predicted_labels = results_after_1_incr_matrix  # Ensure `evaluate` provides these

    # # Confusion Matrix
    # conf_matrix = confusion_matrix(true_labels, predicted_labels, labels=[2, 4])
    # disp = ConfusionMatrixDisplay(confusion_matrix=conf_matrix, display_labels=['Benign (2)', 'Malignant (4)'])
    # disp.plot(cmap='Blues')
    # plt.title("Confusion Matrix")
    # plt.show()

    # true_binary = [0 if label == 2 else 1 for label in true_labels]
    # predicted_binary_scores = [0 if label == 2 else 1 for label in predicted_labels]

    # fpr, tpr, thresholds = roc_curve(true_binary, predicted_binary_scores)
    # roc_auc = auc(fpr, tpr)

    # # Plot ROC Curve
    # plt.figure()
    # plt.plot(fpr, tpr, color='darkorange', lw=2, label=f"ROC curve (area = {roc_auc:.2f})")
    # plt.plot([0, 1], [0, 1], color='navy', lw=2, linestyle='--')
    # plt.xlabel("False Positive Rate")
    # plt.ylabel("True Positive Rate")
    # plt.title("Receiver Operating Characteristic (ROC) Curve")
    # plt.legend(loc="lower right")
    # plt.show()

    

    # print("")
    # print("\nWyniki po drugiej inkrementaci:")
    # print(f"Dokładność: {results_after_2_incr['correct_rate']}%")
    # print(f"Błędna klasyfikacja: {results_after_2_incr['error_rate']}%")
    # print(f"Poprawne klasyfikacje: {results_after_2_incr['correct']}")
    # print(f"Błędne klasyfikacje: {results_after_2_incr['incorrect']}")
    # print(f"Całkowita liczba przykładów: {results_after_2_incr['total']}")

    # true_labels = [example['class'] for example in test_data]
    # predicted_labels = results_after_2_incr_matrix  # Ensure `evaluate` provides these

    # # Confusion Matrix
    # conf_matrix = confusion_matrix(true_labels, predicted_labels, labels=[2, 4])
    # disp = ConfusionMatrixDisplay(confusion_matrix=conf_matrix, display_labels=['Benign (2)', 'Malignant (4)'])
    # disp.plot(cmap='Blues')
    # plt.title("Confusion Matrix")
    # plt.show()

    # true_binary = [0 if label == 2 else 1 for label in true_labels]
    # predicted_binary_scores = [0 if label == 2 else 1 for label in predicted_labels]

    # fpr, tpr, thresholds = roc_curve(true_binary, predicted_binary_scores)
    # roc_auc = auc(fpr, tpr)

    # # Plot ROC Curve
    # plt.figure()
    # plt.plot(fpr, tpr, color='darkorange', lw=2, label=f"ROC curve (area = {roc_auc:.2f})")
    # plt.plot([0, 1], [0, 1], color='navy', lw=2, linestyle='--')
    # plt.xlabel("False Positive Rate")
    # plt.ylabel("True Positive Rate")
    # plt.title("Receiver Operating Characteristic (ROC) Curve")
    # plt.legend(loc="lower right")
    # plt.show()


if __name__ == "__main__":
    main()