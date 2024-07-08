import copy
import datetime
import random
import csv
import networkx as nx
import pandas as pd
import requests
import json


class Controller:
    def __init__(self):
        self.G = nx.Graph()
        self.siec = pd.DataFrame(pd.read_csv("csv/siec.csv",sep=';'))
        self.sessions = [] # przyjmuje parametry sesji
        self.numberOfSessions = 0;
        self.filexec = pd.read_csv("csv/session.csv", sep=";")
        self.ports = pd.read_csv("csv/ports.csv", sep=';')
        self.enumerate = 0
        with open('json/onlyflow.json', 'r') as file2:
            self.data = json.load(file2)
        self.ip1 = ""
        self.ip2 = ""
        self.steps = []

    def getSessions(self):
        self.numberOfSessions = int(input("give nr of sessions:"))
        for i in range(self.numberOfSessions):
            current_session = []
            current_session.append(input("session type")) # 0 mtype") # 0 miejsce to session type
            current_session.append(input("sender"))
            current_session.append(input("receiver"))
            current_session.append(input("bandwidth"))
            self.sessions.append(current_session)


    def createNetwork(self):
        for index, row in self.siec.iterrows():
            self.G.add_edge(row['host1'], row['host2'], weight=row['priority'])
        print(self.G.edges)



    def udpateNetwork(self):
        z = 0
        for y in range(len(self.steps) - 1):
            for x in range(self.siec.shape[0]):
                if (self.siec.at[x, 1] == self.steps[z] and self.siec.at[x, 2] == self.steps[z + 1] or self.siec.at[x, 1] == self.steps[z + 1] and
                        self.siec.at[x, 2] == self.steps[z]):
                    self.siec.at[x, 5] = self.siec.at[x, 5] - int(self.sessions[self.enumerate][3])
            z += 1
        self.siec.to_csv('csv/siecnazwa2.csv', sep=';', index=False)

    def createJSON(self):
        print(self.steps)
        with open('json/onlyflow.json', 'r') as file2:
            self.data = json.load(file2)
        with open('json/scheme.json', 'r') as file:
            data_scheme = json.load(file)

        data_scheme_copy = copy.deepcopy(data_scheme)

        data_scheme["treatment"]["instructions"][0]["port"] = "1"
        data_scheme["selector"]["criteria"][1]["ip"] = str(self.ip1)
        data_scheme["deviceId"] = f"of:000000000000000{self.steps[self.enumerate][0][1:]}"
        self.data['flows'].append(data_scheme)

        data_scheme_copy["treatment"]["instructions"][0]["port"] = "1"
        data_scheme_copy["selector"]["criteria"][1]["ip"] = str(self.ip2)
        data_scheme_copy["deviceId"] = f"of:000000000000000{self.steps[self.enumerate][-1][1:]}"
        self.data['flows'].append(data_scheme_copy)

        z = 0
        for y in range(len(self.steps)-1):
            for x in range(self.ports.shape[0]):
                if (self.ports.iloc[x, 0] == self.steps[self.enumerate][z] and self.ports.iloc[x, 1] == self.steps[self.enumerate][z + 1]):
                    data_scheme_copy = copy.deepcopy(data_scheme)
                    data_scheme_copy["treatment"]["instructions"][0]["port"] = str(self.ports.iloc[x, 2])
                    data_scheme_copy["selector"]["criteria"][1]["ip"] = str(self.ip2)
                    data_scheme_copy["deviceId"] = f"of:000000000000000{self.steps[self.enumerate][z][1:]}"

                    self.data['flows'].append(data_scheme_copy)

                if (self.ports.iloc[x, 1] == self.steps[self.enumerate][z] and self.ports.iloc[x, 0] == self.steps[self.enumerate][z+1]):
                    data_scheme_copy = copy.deepcopy(data_scheme)
                    data_scheme_copy["treatment"]["instructions"][0]["port"] = f"{self.ports.iloc[x, 2]}"
                    data_scheme_copy["selector"]["criteria"][1]["ip"] = str(self.ip1)
                    data_scheme_copy["deviceId"] = f"of:000000000000000{self.steps[self.enumerate][z + 1][1:]}"

                    self.data['flows'].append(data_scheme_copy)
            z += 1

        with open(f'json/{self.steps[self.enumerate][0]}{self.steps[self.enumerate][-1]}.json', 'w') as plik1:
            json.dump(self.data, plik1, indent=4)
        self.enumerate = self.enumerate + 1

    def sendJSON(self):
        payload = open("json/empty.json", "rb")
        response = requests.post("http://192.168.56.105:8181/onos/v1/flows", data=payload, headers={ "Content-Type": "application/json", "Accept": "application/json"}, auth=("onos", "rocks"))
        print("Status Code:", response.status_code)
        print("Response Content:", response.text)


    def StartSession(self):
        print(self.sessions)
        current_steps = (nx.dijkstra_path(self.G, self.sessions[self.enumerate][1], self.sessions[self.enumerate][2]))
        self.ip1 = f"10.0.0.{self.sessions[self.enumerate][1][1:]}/32"
        self.ip2 = f"10.0.0.{self.sessions[self.enumerate][2][1:]}/32"
        self.steps.append(current_steps)



def main():
    controller = Controller()
    controller.createNetwork()
    controller.getSessions()
    for i in range(controller.numberOfSessions):
        controller.udpateNetwork()
        controller.StartSession()
        controller.createJSON()
        #controller.sendJSON()

main()
