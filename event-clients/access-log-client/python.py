import requests
import os

url = 'http://localhost:8081/api/v1/log/multiple'
headers = {'content-type': 'application/json'}

files = os.listdir(".logs")

for file in files:
    with open(".logs/" + file) as f:
        content = f.readlines()

    content = [x.strip() for x in content]

    data_list = []
    data_list.append('[')

    counter = 1
    for line in content:
        data_list.append('{"id": "access_log_fc_schnaittach", "type": "access_log", "origin": "' +
                         line.replace('\\', '\\\\"').replace('\"', '\\\"') + '"}')

        data_list.append(', ')

    data_list = data_list[:-1]
    data_list.append(']')

    data = ''.join(data_list)
    # print(data)
    print(file)

    response = requests.post(url, data=data, headers=headers)
    print(str(response.status_code))
