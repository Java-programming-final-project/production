from flask import Flask, request, make_response
import jnius_config
jnius_config.add_classpath(".;sqlite-jdbc-3.45.3.0.jar;slf4j-api-1.7.36.jar")
from jnius import autoclass, JavaClass, MetaJavaClass

DiaryManager = autoclass("DiaryManager")
Pair = autoclass("Pair")
DB = DiaryManager()
String = autoclass('java.lang.String')
ArrayList = autoclass("java.util.ArrayList")
JavaArray = autoclass('java.lang.reflect.Array')
HashMap = autoclass("java.util.HashMap")



app = Flask(__name__)

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
def saveDiary():
    tags = ArrayList()
    for i in request.values["tags"].split(" "):
        tags.add(i)
    DB.saveDiary(request.values["title"], request.values["content"], tags)
    print("saved Diary {}".format(request.values["title"]))
    return "diary saved"

@app.route("/saveEvent", methods = ["POST"])
def saveEvent():
    tags = ArrayList()
    for i in request.values["tags"].split(" "):
        tags.add(i)
    DB.saveEvent(request.values["title"], request.values["content"], request.values["endTime"], tags)
    print("saved event {}".format(request.values["title"]))
    return "event saved"

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
def deleteRecord():
    DB.deleteRecord(int(request.values["id"]))
    return "record is deleted"

@app.route("/editRecord", methods = ["POST"])
#maybe make one that doesn't require everything
def editRecord():
    tags = ArrayList()
    for i in request.values["tags"].split(" "):
        tags.add(i)
    DB.editRecord(int(request.values["id"]), request.values["endTime"], request.values["title"], request.values["content"], tags)
    return "record is edited"

@app.route("/searchRecord", methods=["GET"])

def searchRecord():
    conditions = HashMap()
    for condition, value in request.values["conditions"]:
        conditions.put(condition, value)
    
    tags = ArrayList()
    for i in request.values["tags"].split(" "):
        tags.add(i)
    
    toreturn = ArrayList()
    for r in request.form.getlist("query"):
        toreturn.add(r)

    orders = ArrayList()
    for order, value in request.values["orders"]:
        orders.put(Pair(order, value))

    return list(map(lambda x:dict(x.entrySet()), DB.searchRecord(conditions, tags, toreturn, request.values["limit"], orders)))

@app.route("/getRecordAll", methods=["GET"])
def getRecordAll():
    #print(request.values["id"])
    return dict(DB.getRecordAll(int(request.values["id"])).entrySet())

@app.route("/getRecordContent", methods=["GET"])
def getRecordContent():
    #print(request.values["id"])
    return str(DB.getRecordContent(int(request.values["id"])))

@app.route("/getRecordCustom", methods = ["POST"])
def getRecordCustom():

    toreturn = ArrayList()
    for r in request.form.getlist("query"):
        toreturn.add(r)
    print(int(request.values["recordId"]), list(toreturn))
    return dict(DB.getRecordCustom(int(request.values["recordId"]), toreturn).entrySet())

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
def setSetting():
    DB.setSetting(request.values["setting"], request.values["value"])
    print("set setting {} : {}".format(request.values["setting"], request.values["value"]))
    return "{} is set to {}".format(request.values["setting"], request.values["value"])

@app.route("/getSetting", methods = ["POST"])
def getSetting():
    #print(request)
    print(request.values["setting"])
    resp = make_response(DB.getSetting(request.values["setting"]))
    resp.headers['Access-Control-Allow-Origin'] = '*'
    #print("get setting {}".format(setting))
    return resp

@app.route("/getSettings", methods = ["GET"])
def getSettings():
    resp = make_response(dict(DB.getSettings().entrySet()))
    resp.headers['Access-Control-Allow-Origin'] = '*'
    #print(settingMap)
    return resp