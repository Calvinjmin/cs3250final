import os
from flask import Flask
from index import idx
from auth import bp
import db

app = Flask(__name__)
app.config.from_mapping(
    SECRET_KEY='dev',
    DATABASE=os.path.join(app.instance_path, 'flaskr.sqlite'),
)
app.register_blueprint(idx)
app.register_blueprint(bp)
db.init_app(app)

if __name__ == '__main__':
    # Threaded option to enable multiple instances for multiple user access support
    app.run(threaded=True, port=5000)
