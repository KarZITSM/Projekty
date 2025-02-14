"""
Obecnie do rozwiązania:
    Handle misclassification: Usuwaj reguły o dużym wskaźniku błędnych klasyfikacji (Wartość do badań)
    Czy prune rules nie robi za szerokich zbiorów reguł - za rzadko przycina
    Negative examples covered - ile procent akceptować
"""


class RuleInduction:
    def __init__(self, attributes):
        """
        Inicjalizuje obiekt RuleInduction.
        """
        self.rules = []  # Zbiór reguł
        self.processed_examples = []  # Zbiór przetworzonych przykładów
        self.attributes = {}  # Atrybuty i ich zakres (dla liczbowych)
        self.attribute_steps = {}  # Kroki kwantyzacji dla liczbowych atrybutów
        self.processed = 0  # Licznik przetworzonych przykładów (do kontrolowania postępów)

        # Przetwarzanie wejściowych atrybutów
        for key, value in attributes.items():
            # Obsługa atrybutów liczbowych - przypisanie zakresów i kwantów
            if isinstance(value, dict) and 'range' in value and 'step' in value:
                # Atrybut liczbowy z zakresem i krokiem
                self.attributes[key] = value['range']
                self.attribute_steps[key] = value['step']
            else:
                # Atrybut dyskretny (lista wartości)
                self.attributes[key] = value

    def batch_initialization(self, examples):
        """Inicjalizacja wsadowa."""
        self.processed_examples.extend(examples)
        for example in examples:
            if not self.is_covered_by_rules(example):  # Tworzenie nowej reguły dla niepokrytego przykładu
                new_rule = self.create_new_rule(example)
                self.update_rule_set(new_rule)
            self.processed += 1  # Aktualizacja testowego licznika przetworzonych przykładów
            print(f'Przetworzone przykłady: {self.processed}')
        batch_rules = '\n'.join([str(rule) for rule in self.rules])
        print(f'Reguły po wsadowej inicjalizacji:\n{batch_rules}')  # Drukowanie reguł po wsadowej inicjalizacji
        # (potrzebne przy testowaniu)

    def process_examples(self, new_examples):
        """Inkrementacyjne przyjmowanie nowych przykładów."""
        self.processed_examples.extend(new_examples)
        for example in new_examples:
            if self.is_misclassified(example):  # Sprawdzenie czy przykład jest niepoprawnie klasyfikowany przez regułę
                # w zbiorze reguł i przetworzenie tego faktu
                self.handle_misclassification()
            elif not self.is_covered_by_rules(example):  # Nowa reguła dla przykładu niepokrytego przez żadne
                # reguły
                new_rule = self.create_new_rule(example)
                self.update_rule_set(new_rule)
            self.processed += 1
            print(f'Przetworzone przykłady: {self.processed}')
        incrementation_rules = '\n'.join([str(rule) for rule in self.rules])
        print(f'Reguły po inkrementacji:\n{incrementation_rules}')  # Drukowanie reguł po inkrementacyjnym
        # przetworzeniu przykładu (potrzebne przy testowaniu)

    def create_new_rule(self, example):
        """Tworzenie nowej reguły na podstawie przykładu."""
        G_s = [{
            'conditions': {
                key: self.attributes[key] if isinstance(self.attributes[key], list) else (
                    self.attributes[key][0], self.attributes[key][1])
                for key in self.attributes
            },
            'class': example['class']
        }]  # Kompleks maksymalnie ogólny - punkt wyjścia do specjalizacji.
        # Dla atrybutów dyskretnych: wszystkie opcje; dla liczbowych: pełny zakres wartości.

        R0 = [x for x in self.processed_examples if x['class'] != example['class']] # Przykłady negatywne

        prev_best_quality = None  # Przechowuje jakość najlepszego kompleksu
        no_change_counter = 0  # Licznik iteracji bez poprawy jakości
        max_no_change_iterations = 5  # Maksymalna liczba iteracji bez poprawy jakości

        max_iterations = 100  # Globalny limit iteracji, potrzebny w awaryjnych sytuacjach
        iteration = 0  # Licznik iteracji

        while self.negative_examples_covered(G_s, R0):
            if iteration >= max_iterations:
                # Przerywanie długich iteracji specjalizacji
                print("Przekroczono maksymalną liczbę iteracji. Przerywanie.")
                break

            G_s = self.specialize(G_s, example, R0)  # Specjalizacja na ziarnie z R0
            G_s = self.select_best_complexes(G_s, n=2)  # Wybranie dwóch najlepszych kompleksów do dalszej specjalizacji

            # Aktualizacja R0 - usuń przykłady negatywne, które już nie są pokrywane
            R0 = [x for x in R0 if any(self.is_covered_by_rule(complex_, x) for complex_ in G_s)]

            best_quality = self.complex_quality(G_s[0])  # Sprawdzanie jakości najlepszego kompleksu
            if prev_best_quality is not None and best_quality == prev_best_quality:
                # Monitorowanie, czy specjalizacja przynosi zmianę jakości kompleksu
                no_change_counter += 1
                if no_change_counter >= max_no_change_iterations:
                    print("Jakość kompleksów nie poprawia się. Przerywanie.")
                    break
            else:
                no_change_counter = 0

            prev_best_quality = best_quality
            iteration += 1

        return self.select_best_complexes(G_s, n=1)[0]  # Zwróć jedną najlepsza regułę

    def update_rule_set(self, new_rule):
        """Aktualizacja zbioru reguł o nową regułę."""
        self.rules.append(new_rule)
        self.prune_rules()  # Przytnij zbiór reguł

    def handle_misclassification(self):
        """
        Obsługa reguł błędnie klasyfikujących przykład.
        """
        self.rules = self.remove_duplicate_rules(self.rules)  # Usuń duplikaty na początku
        rules_to_remove = []

        # Identyfikacja reguł z wysokim wskaźnikiem błędów
        for rule in self.rules:
            if self.error_rate(rule) > 0.1:  # Usuwanie reguły o błędach powyżej 10%
                rules_to_remove.append(rule)

        # Usunięcie reguł po zakończeniu iteracji
        for rule in rules_to_remove:
            if rule in self.rules:  # Sprawdzenie, czy reguła wciąż istnieje w liście
                self.rules.remove(rule)

        # Obsługa przykładów, które nie są pokrywane przez żadną regułę
        uncovered_examples = [x for x in self.processed_examples if not self.is_covered_by_rules(x)]
        for ex in uncovered_examples:
            new_rule = self.create_new_rule(ex)
            self.update_rule_set(new_rule)

    def classify(self, example):
        """
        Klasyfikacja przykładów na podstawie istniejących reguł.
        """

        # Znalezienie reguł, które pokrywają przykład.
        covering_rules = [rule for rule in self.rules if self.is_covered_by_rule(rule, example)]

        # Jeśli żadna reguła nie pokrywa przykładu, wybierz klasę najlepszej dostępnej reguły.
        if not covering_rules:
            return self.select_best_rules(self.rules, n=1)[0]['class']

        # Wyciągnięcie klasy z reguł, które pokrywają przykład.
        classifications = {rule['class'] for rule in covering_rules}

        # Jeśli tylko jedna klasa jest dostępna, zwróć ją.
        if len(classifications) == 1:
            return classifications.pop()

        # Jeśli istnieje więcej niż jedna klasa, wybierz klasę najlepszej reguły spośród pokrywających.
        return self.select_best_rules(covering_rules, n=1)[0]['class']

    def prune_rules(self):
        """
        Przycinanie reguł poprzez usunięcie duplikatów reguł i ograniczenie liczby reguł dla każdej klasy na podstawie
        maksymalnego progu.
        """
        # Usunięcie duplikatów reguł na podstawie ich warunków i klas
        self.rules = self.remove_duplicate_rules(self.rules)

        # Grupowanie reguł według klasy
        class_rules = {}
        for rule in self.rules:
            class_rules.setdefault(rule['class'], []).append(rule)

        pruned_rules = []

        # Iteracja po grupach reguł według klas
        for cls, rules in class_rules.items():
            # Maksymalna liczba reguł dla klasy: co najmniej 3, maksymalnie 1/4 liczby przykładów tej klasy - do badań
            max_rules = max(3, len([x for x in self.processed_examples if x['class'] == cls]) // 4)

            # Jeśli liczba reguł dla klasy przekracza limit, wybierz najlepsze reguły
            if len(rules) > max_rules:
                pruned_rules.extend(self.select_best_complexes(rules, n=max_rules))
            else:
                pruned_rules.extend(rules)

        self.rules = pruned_rules

    def remove_duplicate_rules(self, rules):
        """Usuwa duplikaty z listy reguł."""
        seen = set()  # Zbiór przechowujący unikalne reguły w formie tuples
        # Tuples wykorzystane aby zapewnić porównywanie na podstawie hasha
        unique_rules = []  # Lista przechowująca unikalne reguły
        for rule in rules:
            # Tworzenie reprezentacji reguły jako tuple
            rule_tuple = tuple((attr, tuple(values)) for attr, values in rule['conditions'].items())
            # Sprawdzenie, czy reguła już istnieje w zbiorze
            if (rule['class'], rule_tuple) not in seen:
                seen.add((rule['class'], rule_tuple))
                unique_rules.append(rule)
        return unique_rules

    def is_covered_by_rules(self, example):
        """Sprawdza, czy przykład jest pokrywany przez jakąkolowiek regułę, wykorzystując funkcję any w celu
        przyśpieszenia"""
        return any(self.is_covered_by_rule(rule, example) for rule in self.rules)

    def is_covered_by_rule(self, rule, example):
        """Sprawdza, czy przykład jest pokrywany przez daną regułę."""
        for attr, values in rule['conditions'].items():
            if isinstance(values, tuple):  # Atrybuty, które są zakresami liczbowymi reprezentowane są poprzez tuple
                if not (values[0] <= example['features'][attr] <= values[1]):  # Czy wartość w przykładzie znajduje się
                    # w zakresie, który obejmuje reguła
                    return False
            elif example['features'][attr] not in values:  # Obsługa reguł dyskretnych, reprezentowanych jako listy
                return False
        return True

    def is_misclassified(self, example):
        """Sprawdza, czy przykład jest niepoprawnie klasyfikowany przez reguły."""
        covering_rules = [rule for rule in self.rules if self.is_covered_by_rule(rule, example)]  # Wszystkie reguły
        # pokrywające przykład
        for rule in covering_rules:
            # Błędna klasyfikacja - reguła pokrywa przykład ale przypisuje złą klasę
            if rule['class'] != example['class']:
                return True
        return False

    def negative_examples_covered(self, G_s, R0):
        """Sprawdza, czy negatywne przykłady są pokrywane przez kompleksy."""

        total_negative = len(R0)
        covered_negative = 0

        for complex_ in G_s:
            for example in R0:
                if self.is_covered_by_rule(complex_, example):
                    covered_negative += 1  # Zwiększenie licznika dla każdego pokrytego przykładu

        # Obsługa przypadku, gdy nie ma negatywnych przykładów
        if total_negative == 0:
            return 

        # Sprawdzanie, czy pokrycie negatywnych przykładów jest poniżej progu (5% - do badań), max użyty do zapobiegania
        # dzielenia przez 0
        if covered_negative / total_negative < 0.05:
            return False  # Akceptujemy pokrycie negatywnego poniżej progu

        return covered_negative > 0

    def specialize(self, G_s, example, R0):
        """
        Specjalizacja kompleksów - wykluczenie negatywnych przykładów z R0,
        przy zachowaniu zgodności z pozytywnym przykładem (example).
        """
        new_complexes = []

        for complex_ in G_s:  # Iteracja przez kompleksy w G_s
            for negative_example in R0:  # Iteracja przez przykłady negatywne
                specialized_conditions = []

                for attr in complex_['conditions']:
                    # Obsługa atrybutów liczbowych - reprezentowanych jako tuple
                    if isinstance(complex_['conditions'][attr], tuple):
                        low, high = complex_['conditions'][attr] # Pobranie przedziału atrybutu
                        if low <= negative_example['features'][attr] <= high:
                            # Pobranie kroku (kwantu) dla atrybutu
                            step = self.attribute_steps[attr]

                            if example['features'][attr] > negative_example['features'][attr]:
                                # Zawężenie przedziału od dołu o kwant
                                new_condition = {
                                    'conditions': {
                                        **complex_['conditions'],
                                        attr: (negative_example['features'][attr] + step, high)
                                    },
                                    'class': complex_['class']
                                }
                            else:
                                # Zawężenie przedziału od góry o kwant
                                new_condition = {
                                    'conditions': {
                                        **complex_['conditions'],
                                        attr: (low, negative_example['features'][attr] - step)
                                    },
                                    'class': complex_['class']
                                }
                            specialized_conditions.append(new_condition)

                    # Obsługa atrybutów dyskretnych
                    elif example['features'][attr] == negative_example['features'][attr]:
                        continue  # Jeśli wartość pozytywna i negatywna są takie same, brak możliwości specjalizacji
                    elif negative_example['features'][attr] in complex_['conditions'][attr]:
                            new_condition = {
                                'conditions': {
                                    **complex_['conditions'],
                                    attr: [val for val in complex_['conditions'][attr]
                                           if val != negative_example['features'][attr]]
                                },
                                'class': complex_['class']
                            }
                            specialized_conditions.append(new_condition)

                new_complexes.extend(specialized_conditions)

        # Usuń kompleksy, które nie są najbardziej ogólne
        return [complex_ for complex_ in new_complexes if self.is_general(complex_, new_complexes)]

    def is_general(self, complex_, complexes):
        """Sprawdza, czy dany kompleks jest najbardziej ogólny w porównaniu z innymi kompleksami."""
        for other in complexes:
            # Pomijanie samego siebie w porównaniu
            if other == complex_:
                continue

            # Sprawdzanie, czy inny kompleks jest bardziej ogólny
            for attr in other['conditions']:
                if isinstance(other['conditions'][attr], tuple):  # Liczbowe (przedziały - tuple)
                    # Pobranie zakresów dla other i complex_
                    other_low, other_high = other['conditions'][attr]
                    complex_low, complex_high = complex_['conditions'][attr]
                    # Sprawdzamy, czy zakres other obejmuje zakres `omplex_
                    if not (other_low <= complex_low and other_high >= complex_high):
                        break  # Atrybut nie jest bardziej ogólny
                else:  # Kategoryczne (typ - lista)
                    if not set(other['conditions'][attr]).issubset(set(complex_['conditions'][attr])):
                        break  # Atrybut nie jest bardziej ogólny
            else:
                # Jeśli wszystkie warunki w other są bardziej ogólne niż w complex_
                return False

        # Jeśli żaden inny kompleks nie jest bardziej ogólny
        return True

    def select_best_complexes(self, G_s, n):
        """Wybór n najlepszych kompleksów na podstawie jakości."""
        return sorted(G_s, key=self.complex_quality, reverse=True)[:n]

    def select_best_rules(self, rules, n):
        """Wybór n najlepszych reguł na podstawie jakości."""
        return sorted(rules, key=self.quality, reverse=True)[:n]

    def error_rate(self, rule):
        """
        Obliczanie wskaźnika błędów dla danej reguły - definiowany jako stosunek liczby przykładów pokrywanych
        przez regułę, które mają inną klasę niż przypisana regule, do całkowitej liczby przykładów pokrywanych
        przez regułę.

        """
        # Przykłady, które są pokrywane przez daną regułę
        covered = [x for x in self.processed_examples if self.is_covered_by_rule(rule, x)]
        # Pokrywane, których klasa różni się od klasy przypisanej regule
        incorrect = [x for x in covered if x['class'] != rule['class']]
        return len(incorrect) / len(covered) if covered else 0

    def complex_quality(self, complex_):
        """
        Ocena jakości kompleksu - bazuje na jakości pokrywanych przykładów
        z dodatkowym uwzględnieniem liczby przykładów pokrywanych wyłącznie przez kompleks na potrzeby specjalizacji.
        """

        # Obliczenie podstawowej jakości kompleksu, wykorzystując funkcję quality
        base_quality = self.quality(complex_)

        # Obliczenie liczby przykładów pokrywanych wyłącznie przez complex_
        covered = [x for x in self.processed_examples if self.is_covered_by_rule(complex_, x)]
        exclusive = [
            x for x in covered
            if sum(self.is_covered_by_rule(other, x) for other in self.rules) == 1
        ]

        return base_quality + len(exclusive)

    def quality(self, rule):
        """
        Ocena jakości reguły - definiowana jako suma:
        - liczby przykładów pokrywanych przez regułę, które mają tę samą klasę co przypisana do reguły
        - liczby przykładów niepokrywanych przez regułę, które należą do innych klas
        """
        covered = [x for x in self.processed_examples if self.is_covered_by_rule(rule, x)]

        # Pokrywane przykłady, które mają tą samą klasę co klasa reguły
        positive = [x for x in covered if x['class'] == rule['class']]

        # Przykłady, które nie są pokrywane przez regułę i należą do innych klas
        negative = [x for x in self.processed_examples if x not in covered and x['class'] != rule['class']]
        return len(positive) + len(negative)

    def evaluate(self, test_data):
        """
        Ocena skuteczności algorytmu na zbiorze testowym.
        """
        correct = 0  # Licznik poprawnych klasyfikacji
        incorrect = 0  # Licznik błędnych klasyfikacji

        for example in test_data:
            # Klasyfikacja przykładu
            predicted_class = self.classify(example)

            # Porównanie przewidywanej klasy z rzeczywistą
            if predicted_class == example['class']:
                correct += 1
            else:
                incorrect += 1

        total = correct + incorrect

        # Obliczanie dokładności i wskaźnika błędów
        correct_rate = (correct / total) * 100 if total > 0 else 0
        error_rate = (incorrect / total) * 100 if total > 0 else 0

        # Zwracanie wyników w postaci słownika
        return {
            'correct_rate': correct_rate,  # W procentach
            'error_rate': error_rate,  # W procentach
            'correct': correct,
            'incorrect': incorrect,
            'total': total
        }
    def evaluate_for_matrix_ROC(self, test_data):
        """
        Ocena skuteczności algorytmu na zbiorze testowym.
        """
        correct = 0  # Licznik poprawnych klasyfikacji
        incorrect = 0  # Licznik błędnych klasyfikacji
        predicted_class = []
        for example in test_data:
            # Klasyfikacja przykładu
            predicted_class.append(self.classify(example))
        return predicted_class
