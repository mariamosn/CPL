#!/bin/bash

cat sources/*.cl > combined.cl
./cool combined.cl
