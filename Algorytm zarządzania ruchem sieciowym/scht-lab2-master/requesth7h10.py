import requests

url1 = f"http://192.168.56.105:8181/onos/v1/flows"


payload1 = open("json/h7h10.json", "rb")

headers = {
    "Content-Type": "application/json",
    "Accept": "application/json"
}
auth = ("onos", "rocks")

response = requests.post(url1, data=payload1, headers=headers, auth=auth)

print("Status Code:", response.status_code)
print("Response Content:", response.text)

