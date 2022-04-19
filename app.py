from flask import Flask
from index import idx
from auth import bp

app = Flask(__name__)
app.register_blueprint(idx)
app.register_blueprint(bp)

if __name__ == '__main__':
    # Threaded option to enable multiple instances for multiple user access support
    app.run(threaded=True, port=5000)
