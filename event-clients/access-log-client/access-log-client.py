import argparse
import os
import requests

parser = argparse.ArgumentParser()
parser.add_argument('--folder', required=True, help='The folder which includes the access logs')
parser.add_argument('--source_id', required=True, help='The source id of the event entry which is inserted into the EventStudio')
args = parser.parse_args()

url = 'http://localhost:8081/api/v1/event/multiple'
headers = {'content-type': 'application/json'}

log_files = os.listdir(args.folder)

for log_file in log_files:
    with open(args.folder + '/' + log_file) as f:
        lines = f.readlines()

    lines = [x.strip() for x in lines]

    request_body = []
    request_body.append('[')

    counter = 1
    for line in lines:
        request_body.append('{"source_id": "' + args.source_id + '", "type": "access_log", "origin": "' +
                            line.replace('\\', '\\\\"').replace('\"', '\\\"') + '"}')
        request_body.append(', ')

    request_body = request_body[:-1]
    request_body.append(']')

    data = ''.join(request_body)
    print(log_file)

    response = requests.post(url, data=data, headers=headers)
    print(str(response.status_code))
