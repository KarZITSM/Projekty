import requests

url = "http://192.168.1.50:8181/onos/v1/flows?appId=0000000000000001"

payload = open("onos.json", "rb")
headers = {
    "Content-Type": "application/json",
    "Accept": "application/json"
}
auth = ("onos", "rocks")

response = requests.post(url, data=payload, headers=headers, auth=auth)

print("Status Code:", response.status_code)
print("Response Content:", response.text)