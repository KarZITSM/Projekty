import pandas as pd
siec = pd.read_csv("csv/siec.csv",sep=';')
df = pd.DataFrame(pd.read_csv("csv/siec.csv",sep=';'))
print(df.at[2,'host1'])