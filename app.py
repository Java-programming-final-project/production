from flask import Flask, request, make_response, Response, render_template
import jnius_config
jnius_config.add_classpath("./database;./database/sqlite-jdbc-3.45.3.0.jar;./database/slf4j-api-1.7.36.jar;")
from jnius import autoclass, JavaClass, MetaJavaClass
from flask_cors import CORS, cross_origin
import webview
import re
import threading

DiaryManager = autoclass("DiaryManager")
Pair = autoclass("Pair")
DB = DiaryManager()
String = autoclass('java.lang.String')
Boolean = autoclass("java.lang.Boolean")
ArrayList = autoclass("java.util.ArrayList")
JavaArray = autoclass('java.lang.reflect.Array')
HashMap = autoclass("java.util.HashMap")



app = Flask(__name__, static_folder="./static", template_folder="./template")
#CORS(app)

@app.route("/", methods=["GET"])
def home():
    return render_template("cover.html")
@app.route("/cover", methods=["GET"])
def cover():
    return render_template("cover.html")
@app.route("/calender", methods=["GET"])
def calender():
    return render_template("calender2.html")
@app.route("/calenderadd", methods=["GET"])
def calenderadd():
    return render_template("calenderadd.html")
@app.route("/diaryadd", methods=["GET"])
def diaryadd():
    return render_template("diaryadd.html")
@app.route("/diarypage", methods=["GET"])
def diarypage():
    return render_template("diarypage.html")
@app.route("/lottery", methods=["GET"])
def lottery():
    return render_template("lottery.html")
@app.route("/noteadd", methods=["GET"])
def noteadd():
    return render_template("noteadd.html")
@app.route("/notepage", methods=["GET"])
def notepage():
    return render_template("notepage2.html")


@app.route("/makeDB", methods=["POST"])
def makeDB():
    DB.makeDB()
    print("makeDB")
    resp = make_response("database is created or already created")
    resp.headers['Access-Control-Allow-Origin'] = '*'
    return resp

@app.route("/resetDB", methods=["POST"])
def resetDB():
    DB.resetDB()
    print("resetDB")
    resp = make_response("database is reset")
    resp.headers['Access-Control-Allow-Origin'] = '*'
    return resp

@app.route("/resetSettings", methods=["POST"])
def resetSettings():
    DB.resetSettings()
    print("resetSettings")
    resp = make_response("settings are reset")
    resp.headers['Access-Control-Allow-Origin'] = '*'
    return resp

@app.route("/saveDiary", methods = ["POST"])
@cross_origin()
def saveDiary():
    tags = ArrayList()
    for i in request.json["tags"].split(" "):
        tags.add(i)
    newID = DB.saveDiary(request.json["title"], request.json["content"], tags)
    print("saved Diary {}".format(request.json["title"]))
    return str(newID)

@app.route("/saveEvent", methods = ["POST"])
@cross_origin()
def saveEvent():
    tags = ArrayList()
    for i in request.json["tags"].split(" "):
        tags.add(i)
    newID = DB.saveEvent(request.json["title"], request.json["content"], request.json["endTime"], tags)
    print("saved event {}".format(request.json["title"]))
    return str(newID)

""" 
@app.route("/saveSchedule", methods=["POST"])
def saveSchedule():
    tags = ArrayList()
    for i in request.values["tags"].split(" "):
        tags.add(i)
    DB.saveSchedule(request.values["title"], request.values["content"], request.values["endTime"], tags)
    print("saved schedule {}".format(request.values["title"]))
    return "Schedule saved" """

@app.route("/listRecord", methods = ["GET"])
def listRecord():
    resp = make_response(list(map(lambda x:dict(x.entrySet()), DB.listRecord())))
    resp.headers['Access-Control-Allow-Origin'] = '*'
    return resp
    

@app.route("/deleteRecord", methods = ["POST"])
@cross_origin()
def deleteRecord():
    DB.deleteRecord(int(request.json["id"]))
    return "record is deleted"

#maybe make one that doesn't require everything
@app.route("/editRecord", methods = ["POST"])
@cross_origin()
def editRecord():
    tags = ArrayList()
    for i in request.json["tags"].split(" "):
        tags.add(i)
    DB.editRecord(int(request.json["id"]), request.json["endTime"], request.json["title"], request.json["content"], tags)
    return "record is edited"

@app.route("/searchRecord", methods=["POST"])
@cross_origin()
def searchRecord():
    print(request.json)
    conditions = HashMap()
    for condition, value in request.json["conditions"].items():
        conditions.put(condition, value)
    
    tags = ArrayList()
    tagString = re.sub(r"\s+", " ", request.json["tags"].strip())
    if (tagString) != "":
        for i in tagString.split(" "):
            tags.add(i)

    toreturn = ArrayList()
    for r in request.json["query"]:
        toreturn.add(r)

    orders = ArrayList()
    for order in request.json["orders"]:
        if (order[1] == "true"):
            orders.add(Pair(order[0], Boolean(True)))
        else:
            orders.add(Pair(order[0], Boolean(False)))
    
    return list(map(lambda x:dict(x.entrySet()), DB.searchRecord(conditions, tags, toreturn, int(request.json["limit"]), orders)))

@app.route("/getRecordAll", methods=["POST"])
@cross_origin()
def getRecordAll():
    return dict(DB.getRecordAll(int(request.json["id"])).entrySet()) #resp

@app.route("/getRecordContent", methods=["POST"])
@cross_origin()
def getRecordContent():
    #print(request.values["id"])
    return str(DB.getRecordContent(int(request.json["id"])))

@app.route("/getRecordCustom", methods = ["POST"])
@cross_origin()
def getRecordCustom():
    #print(request.get_json())
    toreturn = ArrayList()
    for r in request.json["query"]:
        toreturn.add(r)
    #print(int(request.json["recordId"]), list(toreturn))
    return dict(DB.getRecordCustom(int(request.json["id"]), toreturn).entrySet())

@app.route("/tagList", methods = ["GET"])
def tagList():
    resp = make_response(list(map(lambda x: (x.first, x.second), list(DB.tagList()))))
    resp.headers['Access-Control-Allow-Origin'] = '*'
    return resp

@app.route("/tags", methods = ["GET"])
def tags():
    resp = make_response(list(map(lambda x: (x.first, x.second), list(DB.tags()))))
    resp.headers['Access-Control-Allow-Origin'] = '*'
    return resp

@app.route("/setSetting", methods = ["POST"])
@cross_origin()
def setSetting():
    DB.setSetting(request.json["setting"], request.json["value"])
    print("set setting {} : {}".format(request.json["setting"], request.json["value"]))
    return "{} is set to {}".format(request.json["setting"], request.json["value"])


@app.route("/getSetting", methods = ["POST"])
@cross_origin()
def getSetting():
    print(request.json)
    return DB.getSetting(request.json["setting"])

@app.route("/getSettings", methods = ["GET"])
def getSettings():
    resp = make_response(dict(DB.getSettings().entrySet()))
    resp.headers['Access-Control-Allow-Origin'] = '*'
    #print(settingMap)
    return resp

@app.route("/getSchedule", methods = ["POST"])
@cross_origin()
def getSchedule():
    return dict(DB.getSchedule(request.json["date"]).entrySet())

def startServer():
    import waitress
    waitress.serve(app, port=5000)

if __name__ == "__main__":
    #app.run()
    serverThread = threading.Thread(target=startServer)
    serverThread.daemon = True
    serverThread.start()

    
    webview.create_window('manager', app, width=540, height=960)
    webview.start()
    