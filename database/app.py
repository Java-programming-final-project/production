from flask import Flask, request
import jnius_config
jnius_config.add_classpath(".;sqlite-jdbc-3.45.3.0.jar;slf4j-api-1.7.36.jar")
from jnius import autoclass, JavaClass, MetaJavaClass

DiaryManager = autoclass("DiaryManager")
DB = DiaryManager()
String = autoclass('java.lang.String')
ArrayList = autoclass("java.util.ArrayList")
JavaArray = autoclass('java.lang.reflect.Array')



app = Flask(__name__)

@app.route("/makeDB", methods=["POST"])
def makeDB():
    DB.makeDB()
    print("makeDB")
    return ""

@app.route("/resetDB", methods=["POST"])
def resetDB():
    DB.resetDB()
    print("resetDB")
    return ""

@app.route("/resetSettings", methods=["POST"])
def resetSettings():
    DB.resetSettings()
    print("resetSettings")
    return ""

@app.route("/saveDiary", methods = ["POST"])
def saveDiary():
    DB.saveDiary(request.values["title"], request.values["content"])
    print("saved Diary {}".format(request.values["title"]))
    return ""

@app.route("/saveEvent", methods = ["POST"])
def saveEvent():
    DB.saveEvent(request.values["title"], request.values["content"], request.values["endTime"])
    print("saved Diary {}".format(request.values["title"]))
    return ""

@app.route("/listRecord", methods = ["GET"])
def listRecord():
    return list(map(lambda x:dict(x.entrySet()), DB.listRecord()))
    

@app.route("/deleteRecord", methods = ["POST"])
def deleteRecord():
    DB.deleteRecord(request.values["id"])
    return ""

@app.route("/editRecord", methods = ["POST"])
def editRecord():
    #DB.editRecord(request.values["id"], request.values["content"])
    return ""

@app.route("/searchRecord", methods=["GET"])
def searchRecord():
    return ""

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
    #print(int(request.values["recordId"]), request.form.getlist("query"))
    #print(dict(DB.getRecordCustom()))
    
    #toreturn = ArrayList()
    #for r in request.form.getlist("query"):
    #    toreturn.add(r)
    #print(DB.getRecordCustom(int(request.values["id"]), JavaArray.newInstance(String, 3)))
    return ""#dict(.entrySet())

@app.route("/tagList", methods = ["GET"])
def tagList():
    return list(map(lambda x: (x.first, x.second), list(DB.tagList())))

@app.route("/tags", methods = ["GET"])
def tags():
    return list(map(lambda x: (x.first, x.second), list(DB.tags())))

@app.route("/setSetting", methods = ["POST"])
def setSetting():
    DB.setSetting(request.values["setting"], request.values["value"])
    print("set setting {} : {}".format(request.values["setting"], request.values["value"]))
    return ""

@app.route("/getSetting", methods = ["GET"])
def getSetting():
    setting = DB.getSetting(request.values["setting"])
    print("get setting {}".format(setting))
    return setting

@app.route("/getSettings", methods = ["GET"])
def getSettings():
    settingMap = dict(DB.getSettings())
    print(settingMap)
    return settingMap