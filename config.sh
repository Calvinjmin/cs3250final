#!/bin/sh

# Run this script to activate environment variables
# source envActivate.sh
export FLASK_ENV=development
export FLASK_DEBUG=True

# Need to make sure the virtual environment is called 'venv'
source venv/bin/activate

