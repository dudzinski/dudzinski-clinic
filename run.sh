#!/bin/bash
docker build -t dudzinski-clinic .
docker run -p 8080:8080 dudzinski-clinic
