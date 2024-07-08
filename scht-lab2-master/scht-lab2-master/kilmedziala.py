import copy
import requests
import pandas as pd
import networkx as nx
import json
import shutil

G = nx.Graph()
steps = []
siec = pd.read_csv("csv/siec.csv", sep=';')
#bandwidth = 0
ip1 = ""
ip2 = ""
bandwidth = 0


def priority(bandwidth, delay):
    priority = (1 / bandwidth) * delay
    return priority
def calculatePriority():
    myData = pd.read_csv("csv/siec2.csv", sep=';')

    myData['priority'] = priority(myData["bw"],myData["opóźnienie"])
    df = pd.DataFrame(myData)

    df.to_csv('csv/siec2.csv', sep=';')

def calculateBandwidth(session,parameters):
    #opoznienie w ms , bandwidth = w bitach ,
    global bandwidth
    match session:
        case "-u":#udp
            bandwidth = (parameters[0]*parameters[1]*8)/(1024*1024)
            print(f"bandwidth:{bandwidth}")
        case "-c":#tcp
            rtt=0
            z = 0
            for y in range(len(steps) - 1):
                for x in range(siec.shape[0]):
                    if (siec.iloc[x, 1] == steps[z] and siec.iloc[x, 2] == steps[z + 1] or siec.iloc[x, 1] == steps[z + 1] and siec.iloc[x, 2] == steps[z]):
                        rtt += siec.iloc[x,4]*2
                z += 1
            print(rtt)
            bandwidth = parameters[0]*8*1000/(rtt*1024*1024*2.35)
            print(f"bandwidth:{bandwidth}")
        case default:
            print("wrong type of session!")



def createNetwork():
    file_path = 'csv/siec2.csv'
    with open(file_path, 'w') as file:
        file.write('')
    plik_zrodlowy = 'csv/siec.csv'
    plik_docelowy = 'csv/siec2.csv'
    shutil.copyfile(plik_zrodlowy, plik_docelowy)

    siec = pd.read_csv("csv/siec2.csv", sep=';')
    for index, row in siec.iterrows():
        G.add_edge(row['host1'], row['host2'], weight=row['priority'])

    print(G.edges)

def runSessionsFromFile():
    file_path ="csv/session.csv"
    sessions = pd.read_csv(file_path, sep=';')
    print(sessions.shape[0])
    tab_length = sessions.shape[0]
    for i in range(tab_length): ## session_type;h1;h2;tcp-ws_or_udp_blockrate;blocksize
        print("hi")
        type_session = sessions.iloc[i,0]
        h1 = sessions.iloc[i,1]
        h2 = sessions.iloc[i,2]
        parameters = []
        match type_session:
            case "-u":
                parameters.append(sessions.iloc[i,3])
                parameters.append(sessions.iloc[i,4])
            case "-c":
                parameters.append(sessions.iloc[i, 3])
            case default:
                print("wrong type of session!")
        startSessions(type_session,h1,h2,parameters)
        udpateNetwork()
        data = createJSON()
        sendJSON(data)




def startSessions(type,h1,h2,parameters):
    global ip1
    global ip2
    global steps
    ip1 = f"10.0.0.{int(h1[-1:],base = 16)}/32"
    ip2 = f"10.0.0.{int(h2[-1:],base = 16)}/32"
    steps = nx.dijkstra_path(G, h1, h2)
    print(steps)
    print(steps[2][1:])
    calculateBandwidth(type,parameters)



def udpateNetwork():
    global bandwidth
    z = 0
    for y in range(len(steps) - 1):
        for x in range(siec.shape[0]):
            if (siec.iloc[x, 1] == steps[z] and siec.iloc[x, 2] == steps[z + 1] or siec.iloc[x, 1] == steps[z + 1] and
                    siec.iloc[x, 2] == steps[z]):
                siec.iloc[x, 5] = siec.iloc[x, 5] - int(bandwidth)
        z += 1
    save = pd.DataFrame(siec)
    save.to_csv('csv/siec2.csv', sep=';')
    calculatePriority()


def createJSON():
    global ip1
    global ip2
    global steps
    ports = pd.read_csv("csv/ports.csv", sep=';')

    with open('json/scheme.json', 'r') as file:
        data_scheme = json.load(file)

    with open('json/empty.json', 'r') as file2:
        data = json.load(file2)

    data_scheme_copy = copy.deepcopy(data_scheme)


    with open('json/onlyflow.json', 'r') as file:
        onlyflow = json.load(file)

    with open('json/empty.json', 'w') as file2:
        json.dump(onlyflow, file2, indent=4)


    #with open('json/empty.json', 'w') as file:
     #   file.write('')
    #plik_zrodlowy = 'json/onlyflow.json'
    #plik_docelowy = 'json/empty.json'
    #shutil.copyfile(plik_zrodlowy, plik_docelowy)

    data_scheme["treatment"]["instructions"][0]["port"] = "1"
    data_scheme["selector"]["criteria"][1]["ip"] = str(ip1)
    data_scheme["deviceId"] = f"000000000000000{steps[0][1:]}"
    data['flows'].append(data_scheme)

    data_scheme_copy["treatment"]["instructions"][0]["port"] = "1"
    data_scheme_copy["selector"]["criteria"][1]["ip"] = str(ip2)
    data_scheme_copy["deviceId"] = f"000000000000000{steps[-1][1:]}"
    data['flows'].append(data_scheme_copy)

    z = 0
    for y in range(len(steps) - 1):
        for x in range(ports.shape[0]):
            if (ports.iloc[x, 0] == steps[z] and ports.iloc[x, 1] == steps[z + 1]):
                data_scheme_copy = copy.deepcopy(data_scheme)
                data_scheme_copy["treatment"]["instructions"][0]["port"] = str(ports.iloc[x, 2])
                data_scheme_copy["selector"]["criteria"][1]["ip"] = str(ip2)
                data_scheme_copy["deviceId"] = f"000000000000000{steps[z][1:]}"

                data['flows'].append(data_scheme_copy)

            if (ports.iloc[x, 1] == steps[z + 1] and ports.iloc[x, 0] == steps[z]):
                data_scheme_copy = copy.deepcopy(data_scheme)
                data_scheme_copy["treatment"]["instructions"][0]["port"] = f"{ports.iloc[x, 2]}"
                data_scheme_copy["selector"]["criteria"][1]["ip"] = str(ip1)
                data_scheme_copy["deviceId"] = f"000000000000000{steps[z + 1][1:]}"

                data['flows'].append(data_scheme_copy)
        z += 1

    with open('json/empty.json', 'w') as plik1:
        json.dump(data, plik1, indent=4)
    return data


def sendJSON(data):
    url1 = f"http://192.168.56.105:8181/onos/v1/flows"
    with open("json/empty.json", 'r') as plik:
        payload = json.load(plik)
    payload1 = open("json/empty.json", "rb")
    headers = {
        "Content-Type": "application/json",
        "Accept": "application/json"
    }
    auth = ("onos", "rocks")
    response = requests.post(url1, data=payload1, headers=headers, auth=auth)

    print("Status Code:", response.status_code)
    print("Response Content:", response.text)


createNetwork()
runSessionsFromFile()
# startSessions("-c","s6",'s8',[200000])
# udpateNetwork()
# data = createJSON()
# sendJSON(data)
