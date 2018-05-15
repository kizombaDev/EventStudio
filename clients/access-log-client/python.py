import requests
import os

url = 'http://localhost:8081/api/v1/log/multiple'
headers = {'content-type': 'application/json'}

files = os.listdir("logs")

for file in files:
    with open("logs/" + file) as f:
        content = f.readlines()

    content = [x.strip() for x in content]

    multi_data = '['

    counter = 1
    for line in content:
        data = '{"id": "test", "type": "access_log", "origin": "' + \
            line.replace('\"', '\\\"') + '"}'

        print(str(file) + ' line: ' + str(counter))
        counter = counter + 1
        multi_data = multi_data + data + ', '

    multi_data = multi_data[:-1]
    multi_data = multi_data[:-1]
    multi_data = multi_data + ']'

    print(multi_data)

    response = requests.post(url, data=multi_data, headers=headers)
    print(str(response.status_code))
