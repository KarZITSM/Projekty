import pandas as pd
import math
import csv
import numpy as np
import networkx as nx
import json


G = nx.Graph()

# Dane
data_scheme = pd.DataFrame({
    'host1': ['s1', 's1', 's5', 's1', 's3', 's3', 's1', 's2', 's2', 's6', 's5', 's10', 's4', 's9', 's7', 's2', 's2'],
    'host2': ['s10', 's5', 's6', 's3', 's4', 's9', 's2', 's7', 's8', 's7', 's10', 's4', 's9', 's8', 's8', 's5', 's3'],
    'Opóźnienie': [2.489016, 4.044651, 5.218448, 3.684026, 2.764788, 2.404163, 3.570889, 4.454773, 5.791205, 7.580185, 4.384062, 5.777062, 3.846661, 5.656854, 6.936718, 6.604377, 3.945656]
})

#tworzenie grafu
for index, row in data_scheme.iterrows():
    G.add_edge(row['host1'], row['host2'], weight=row['Opóźnienie'])

#ustalanie skąd dokąd połączenie
#kalkulowanie dijsktry
print(G.edges.values())
h1 = "s6"
h2 = "s8"
ip1 = f"10.0.0.{h1[1:]}"
ip2  =f"10.0.0.{h2[1:]}"

siec =pd.read_csv("csv/siec.csv", sep=';')
steps = nx.dijkstra_path(G,h1,h2)
print(steps)
print(steps[2][1:])


bandwidth = input('Przepustowość, której potrzebuje sesja ')

#modyfikowanie pliku z przepustowością
z=0
for y in range(len(steps)-1):
    for x in range(siec.shape[0]):
        if(siec.iloc[x, 1]==steps[z] and siec.iloc[x, 2]==steps[z+1] or siec.iloc[x, 1]==steps[z+1] and siec.iloc[x, 2]==steps[z]):
            siec.iloc[x, 5] = siec.iloc[x, 5] - int(bandwidth)
    z += 1

#zapisanie pliku z przepustowością
save =pd.DataFrame(siec)
save.to_csv('siec2.csv',sep=';')



#otwarcie schematu jsona


numberOfJson = steps*2
z=0
ports =pd.read_csv("csv/ports.csv", sep=';')


#wyczyścić controller.json
with open('json/controller.json', 'w') as plik:
    # Tworzenie pustego obiektu JSON (pusty słownik)
    pusty_obiekt = {}
    # Zapisz pusty obiekt JSON do pliku
    json.dump(pusty_obiekt, plik)





with open('json/scheme.json', 'r') as file:
    data_scheme = json.load(file)
data_scheme["treatment"]["instructions"][0]["port"] = "1"
data_scheme["selector"]["criteria"][1]["ip"] = str(ip2)
data_scheme["deviceId"] = f"000000000000000{steps[0][1:]}"
# zapis do jsona wprowadzonych zmian
with open('json/empty.json', 'r') as plik1:
    data = json.load(plik1)
data['flows'].append(data_scheme)

with open('json/controller.json', 'a') as plik1:
    json.dump(data, plik1, indent=4)





with open('json/scheme.json', 'r') as file:
    data_scheme = json.load(file)
data_scheme["treatment"]["instructions"][0]["port"] = "1"
data_scheme["selector"]["criteria"][1]["ip"] = str(ip1)
data_scheme["deviceId"] = f"000000000000000{steps[-1][1:]}"
# zapis do jsona wprowadzonych zmian
with open('json/empty.json', 'r') as plik1:
    data = json.load(plik1)
data['flows'].append(data_scheme)

with open('json/controller.json', 'a') as plik1:
    json.dump(data, plik1, indent=4)




for y in range(len(steps)-1):
    for x in range(ports.shape[0]):
        if(ports.iloc[x,0]==steps[z] and ports.iloc[x, 1]==steps[z+1]):
            with open('json/scheme.json', 'r') as file:
                data_scheme = json.load(file)
            data_scheme["treatment"]["instructions"][0]["port"] = str(ports.iloc[x,2])
            data_scheme["selector"]["criteria"][1]["ip"] = str(ip2)
            data_scheme["deviceId"] = f"000000000000000{steps[z][1:]}"
            #zapis do jsona wprowadzonych zmian

            with open('json/empty.json', 'r') as plik1:
                data = json.load(plik1)
            data['flows'].append(data_scheme)

            with open('json/controller.json', 'a') as plik1:
                json.dump(data, plik1, indent=4)



        if(ports.iloc[x, 1]==steps[z+1] and ports.iloc[x, 0]==steps[z]):
            with open('json/scheme.json', 'r') as file:
                data_scheme = json.load(file)
            data_scheme["treatment"]["instructions"][0]["port"] = f"{ports.iloc[x, 2]}"
            data_scheme["selector"]["criteria"][1]["ip"] = str(ip1)
            data_scheme["deviceId"] = f"000000000000000{steps[z+1][1:]}"
            # zapis do jsona wprowadzonych zmian
            #with open('json/scheme.json', 'w') as file:
             #   json.dump('json/scheme.json', file, indent=4)
            with open('json/empty.json', 'r') as plik1:
                data = json.load(plik1)
            data['flows'].append(data_scheme)
            with open('json/controller.json', 'a') as plik1:
                json.dump(data, plik1, indent=4)


    z += 1

with open('json/empty.json', 'w') as plik1:
    json.dump(data, plik1, indent=4)


# adres ip jest ustalany na samym początku, jeżeli kolejnośc jest zgodna ze steps to jest inne dest, jeżeli kolejność jest odwrotna to ip dest
# jest inne
# działa modyfikowanie jsonów, 

