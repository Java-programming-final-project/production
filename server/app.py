from flask import Flask
app = Flask(__name__)

@app.route("/makeDB", method=["POST"])
def makeDB():

@app.route("/resetDB", method=["POST"])
def resetDB():

@app.route("/resetSettings", method=["POST"])
def resetSettings():

@app.route("/resetDB", method=["POST"])
def resetDB():

@app.route("/saveDiary", method = ["POST"])
def saveDiary():

@app.route("/saveEvent", method = ["POST"])
def saveEvent():

@app.route("/deleteRecord", method = ["POST"])
def deleteRecord():

@app.route("/editRecord", method = ["POST"])
def editRecord():

@app.route("/searchRecord", method=["GET"])
def searchRecord():

@app.route("/getRecordAll", method=["GET"])
def getRecordAll():

@app.route("/getRecordContent", method=["GET"])
def getRecordContent():

@app.route("/getRecordCustom", method = ["GET"])
def getRecordCustom():

@app.route("/tagList", method = ["GET"])
def tagList():

@app.route("/tags", method = ["GET"])
def tags():

@app.route("/setSetting", method = ["POST"])
def setSetting():

@app.route("/getSetting", method = ["GET"])
def getSetting():

@app.route("/getSettings", method = ["GET"])
def getSettings():