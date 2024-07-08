import copy
import networkx as nx
import pandas as pd
import requests
from matplotlib import pyplot as plt

import json


class Controller:
    def __init__(self):
        self.G = nx.Graph() #instancja grafu
        self.siec = pd.read_csv("csv/siec.csv", sep=";") # wczytany plik z parametram sieci
        self.sessions = [] # przyjmuje parametry sesji
        self.numberOfSessions = 0; # liczba sesji, zmienna pomocnicza, w celu prostszego odwoływania się w innych funkcjach
        self.ports = pd.read_csv("csv/ports.csv", sep=';') #plik z listą portów na bazie komendy links w mininecie
        self.enumerate = 0 #zmienna pomocnicza iterowanie po sesjach
        with open('json/onlyflow.json', 'r') as file2:
            self.data = json.load(file2)
        self.ip1 = "" #adres ip zmieniany dla każdej iteracji po sesjach , klient
        self.ip2 = "" #^^^ , serwer
        self.steps = [] #lista switchy generowana przez dijkstre
        self.colour = ['red','blue','green','yellow'] #kolory wykresów
        self.colour_nr = 0

    def priority(bandwidth, delay):
        '''
        parametry bandiwdth oraz delay wczytywane są z pliku csv sieci
        zwracana wartość to waga w grafie
        '''
        priority = (1 / bandwidth) * delay
        return priority

    def getSessions(self):
        '''
        funkcja przyjmująca argumenty dotyczące liczby sesji oraz parametrów każdej z nich:
        rodzaj sesji (tcp, udp, ping)
        klient (sender, format "s{nr}")
        server (receiver, format "s{nr})
        przepustowość (int)
        '''
        self.numberOfSessions = int(input("give nr of sessions:"))
        for i in range(self.numberOfSessions):
            current_session = []
            current_session.append(input("session type")) # 0 mtype") # 0 miejsce to session type
            current_session.append(input("sender"))
            current_session.append(input("receiver"))
            current_session.append(input("bandwidth"))
            self.sessions.append(current_session)


    def createNetwork(self):
        '''
        generuje graf z pliku csv, wykorzystujemy biblliotekę pandas do operowania na plikach csv
        '''
        for index, row in self.siec.iterrows():
            self.G.add_edge(row['host1'], row['host2'], weight=row['priority'])
        print(self.G.edges)



    def udpateNetwork(self):
        '''
        aktualizuje
        '''
        z = 0
        print(self.steps)
        for y in range(len(self.steps[self.enumerate])-1):
            for x in range(self.siec.shape[0]):
                if (self.siec.at[x, "host1"] == self.steps[self.enumerate][z] and self.siec.at[x, "host2"] == self.steps[self.enumerate][z + 1]):
                    self.siec.at[x, "bw"] = self.siec.at[x, "bw"] - int(self.sessions[self.enumerate][3]) # zmiana przepustowości
                    self.G[self.siec.at[x, "host1"]][self.siec.at[x, "host2"]]["priority"] = (1/self.siec.at[x, "bw"]) * self.siec.at[x, "opóźnienie"]
                    self.siec.at[x,"priority"] = (1/self.siec.at[x, "bw"]) * self.siec.at[x, "opóźnienie"]
                if ( self.siec.at[x, "host1"] == self.steps[self.enumerate][z + 1] and self.siec.at[x, "host2"] == self.steps[self.enumerate][z]):
                    self.siec.at[x, "bw"] = self.siec.at[x, "bw"] - int(
                        self.sessions[self.enumerate][3])  # zmiana przepustowości
                    self.G[self.siec.at[x, "host1"]][self.siec.at[x, "host2"]]["priority"] = (1/self.siec.at[x, "bw"]) * self.siec.at[x, "opóźnienie"]
                    self.siec.at[x, "priority"] = (1 / self.siec.at[x, "bw"]) * self.siec.at[x, "opóźnienie"]
            z += 1
        self.siec.to_csv(f'csv/siecmain.csv', sep=';', index=False)

    def createJSON(self):
        print(self.steps)
        with open('json/onlyflow.json', 'r') as file2:
            self.data = json.load(file2)
        with open('json/scheme.json', 'r') as file:
            data_scheme = json.load(file)

        data_scheme_copy = copy.deepcopy(data_scheme)
        match self.sessions[self.enumerate][0]:
            case "tcp":
                data_scheme["selector"]["criteria"][3]["protocol"] = "6"
                data_scheme_copy["selector"]["criteria"][3]["protocol"] = "6"

            case "udp":
                data_scheme["selector"]["criteria"][3]["protocol"] = "17"
                data_scheme_copy["selector"]["criteria"][3]["protocol"] = "17"

            case "ping":
                data_scheme["selector"]["criteria"].pop(3)
                data_scheme_copy["selector"]["criteria"].pop(3)
            case default:
                print("the protocol is not supported")



# 6 tcp lub 17 udp
        data_scheme["treatment"]["instructions"][0]["port"] = "1"
        data_scheme["selector"]["criteria"][1]["ip"] = str(self.ip1)
        data_scheme["selector"]["criteria"][2]["ip"] = str(self.ip2)
        data_scheme["deviceId"] = f"of:000000000000000{hex(int(self.steps[self.enumerate][0][1:]))[2:]}"
        self.data['flows'].append(data_scheme)

        data_scheme_copy["treatment"]["instructions"][0]["port"] = "1"
        data_scheme_copy["selector"]["criteria"][1]["ip"] = str(self.ip2)
        data_scheme_copy["selector"]["criteria"][2]["ip"] = str(self.ip1)
        data_scheme_copy["deviceId"] = f"of:000000000000000{hex(int(self.steps[self.enumerate][-1][1:]))[2:]}"
        self.data['flows'].append(data_scheme_copy)

        z = 0
        for y in range(len(self.steps)):
            for x in range(self.ports.shape[0]):
                if (self.ports.iloc[x, 0] == self.steps[self.enumerate][z] and self.ports.iloc[x, 1] == self.steps[self.enumerate][z + 1]):
                    data_scheme_copy = copy.deepcopy(data_scheme)
                    data_scheme_copy["treatment"]["instructions"][0]["port"] = str(self.ports.iloc[x, 2])
                    data_scheme_copy["selector"]["criteria"][1]["ip"] = str(self.ip2)
                    data_scheme_copy["selector"]["criteria"][2]["ip"] = str(self.ip1)
                    data_scheme_copy["deviceId"] = f"of:000000000000000{hex(int(self.steps[self.enumerate][z][1:]))[2:]}"

                    self.data['flows'].append(data_scheme_copy)

                if (self.ports.iloc[x, 1] == self.steps[self.enumerate][z] and self.ports.iloc[x, 0] == self.steps[self.enumerate][z+1]):
                    data_scheme_copy = copy.deepcopy(data_scheme)
                    data_scheme_copy["treatment"]["instructions"][0]["port"] = f"{self.ports.iloc[x, 2]}"
                    data_scheme_copy["selector"]["criteria"][1]["ip"] = str(self.ip1)
                    data_scheme_copy["selector"]["criteria"][2]["ip"] = str(self.ip2)
                    data_scheme_copy["deviceId"] = f"of:000000000000000{hex(int(self.steps[self.enumerate][z+1][1:]))[2:]}"

                    self.data['flows'].append(data_scheme_copy)
            z += 1

        with open(f'json/{self.steps[self.enumerate][0]}{self.steps[self.enumerate][-1]}.json', 'w') as plik1:
            json.dump(self.data, plik1, indent=4)
        self.enumerate = self.enumerate + 1

    def sendJSON(self):
        payload = open("json/empty.json", "rb")
        response = requests.post("http://192.168.56.105:8181/onos/v1/flows", data=self.data, headers={ "Content-Type": "application/json", "Accept": "application/json"}, auth=("onos", "rocks"))
        print("Status Code:", response.status_code)
        print("Response Content:", response.text)


    def StartSession(self):
        print(self.sessions)
        current_steps = (nx.dijkstra_path(self.G, self.sessions[self.enumerate][1], self.sessions[self.enumerate][2]))
        self.ip1 = f"10.0.0.{self.sessions[self.enumerate][1][1:]}/32"
        self.ip2 = f"10.0.0.{self.sessions[self.enumerate][2][1:]}/32"
        self.steps.append(current_steps)

    def paintGraphWithStream(self):
        pos = nx.shell_layout(self.G)
        nx.draw(self.G, pos, with_labels=True, font_weight='bold', node_size=700, node_color='skyblue',
                font_color='black',
                font_size=10)
        stream_path=[]
        for i in range(len(self.steps[self.enumerate])-1):
            stream_path.append([self.steps[self.enumerate][i],self.steps[self.enumerate][i+1]])
        print(stream_path)

        # Rysuj krawędzie
        nx.draw_networkx_edges(self.G, pos, edgelist=stream_path, edge_color=self.colour[self.colour_nr], width=2)


        plt.title("Sample Graph with Stream Path")
        plt.show()



def main():
    controller = Controller()
    controller.createNetwork()
    controller.getSessions()
    for i in range(controller.numberOfSessions):
        controller.StartSession()
        controller.paintGraphWithStream()
        controller.udpateNetwork()
        controller.createJSON()
        #controller.sendJSON()

def paint():
    controller = Controller()
    controller.createNetwork()
    controller.paintGraph()

main()

