#!/bin/bash

server_port="lsof -t -i:5000"
port=$(eval "$server_port")
kill -9 $port