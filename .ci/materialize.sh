#!/bin/sh

echo "$1" | base64 --decode --output "$2"
