#!/bin/sh

echo "$1" | base64 -D -o "$2"
