import requests

url1 = f"http://192.168.56.105:8181/onos/v1/flows?appId=0000000000000006"
url2 = "http://192.168.56.105:8181/onos/v1/flows?appId=0000000000000005"
url3 = "http://192.168.56.105:8181/onos/v1/flows?appId=0000000000000001"
url4 = "http://192.168.56.105:8181/onos/v1/flows?appId=0000000000000002"
url5 = "http://192.168.56.105:8181/onos/v1/flows?appId=0000000000000008"


payload1 = open("switch6.json", "rb")
payload2 = open("switch5.json", "rb")
payload3 = open("switch1.json", "rb")
payload4 = open("switch2.json", "rb")
payload5 = open("switch8.json", "rb")
headers = {
    "Content-Type": "application/json",
    "Accept": "application/json"
}
auth = ("onos", "rocks")

response = requests.post(url1, data=payload1, headers=headers, auth=auth)
response2 = requests.post(url2, data=payload2, headers=headers, auth=auth)
response3 = requests.post(url3, data=payload3, headers=headers, auth=auth)
response4 = requests.post(url4, data=payload4, headers=headers, auth=auth)
response5 = requests.post(url5, data=payload5, headers=headers, auth=auth)

print("Status Code:", response.status_code)
print("Response Content:", response.text)
print("Status Code:", response2.status_code)
print("Response Content:", response2.text)
print("Status Code:", response3.status_code)
print("Response Content:", response3.text)
print("Status Code:", response4.status_code)
print("Response Content:", response4.text)
print("Status Code:", response5.status_code)
print("Response Content:", response5.text)

