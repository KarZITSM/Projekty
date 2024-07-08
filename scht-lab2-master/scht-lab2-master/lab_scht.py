import pandas as pd
import math
import csv
import numpy as np
import networkx as nx

myData = pd.read_csv("csv/lab1.csv")

def opóźnienie(odległość):
    return odległość*math.sqrt(2)/200

myData['opóźnienie'] = opóźnienie(myData['Odległość'])
df =pd.DataFrame(myData)

df.to_csv('lab2.csv',sep=';')

print(myData)















