import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
public class DiaryManager{
    private Connection connection;
    private Statement statement;
    public static final int RECORD_COLUMN_COUNT = 6;
    public static final String RECORD_COLUMN[] = {"id", "type", "title", "content", "setDate", "endTime"};
    public DiaryManager(){
        try
        {
            this.connection = DriverManager.getConnection("jdbc:sqlite:diarymanager.db");
            this.statement = connection.createStatement();
            makeDB();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        return;
    }
    public void close(){
        try{
            this.statement.close();
            this.connection.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    //make a resetDB later in development
    public void makeDB(){
        try{
            //this.statement.executeUpdate("drop table DIARY");
            //this.statement.executeUpdate("drop table TAGS");
            //this.statement.executeUpdate("drop table TAGLIST");
            //this.statement.executeUpdate("DROP TABLE RECORD");

            //this.statement.executeUpdate("create table if not exists DIARY (id INTEGER, title TEXT, date DATE, content TEXT)");
            //this.statement.executeUpdate("create table if not exists TAGS (id INTEGER, tag TEXT)");
            //this.statement.executeUpdate("create table if not exists TAGLIST (tag TEXT, counter INTEGER)");
            //this.statement.executeUpdate("CREATE TABLE if not exists DIARY (id INTEGER, title TEXT, date DATE, content TEXT, PRIMARY KEY(id ASC))");
            //this.statement.executeUpdate("CREATE TABLE if not exists EVENT (id INTEGER, setTime DATETIME, endTime DATETIME, title TEXT, content TEXT, PRIMARY KEY(id ASC))");
            //type0: diary type1: event
            this.statement.executeUpdate("CREATE TABLE if not exists RECORD (id INTEGER, type INTEGER, title TEXT, content TEXT, setDate DATE, endTime DATETIME, PRIMARY KEY(id ASC))");
            this.statement.executeUpdate("CREATE TABLE if not exists TAGS (id INTEGER, tag TEXT)");
            this.statement.executeUpdate("CREATE TABLE if not exists TAGLIST (tag TEXT PRIMARY KEY, counter INTEGER)");
            this.statement.executeUpdate("CREATE TABLE IF NOT EXISTS SETTINGS (setting TEXT PRIMARY KEY, value TEXT)");
            /*String sql = "select * from " + "DIARY" + " LIMIT 0";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData mrs = rs.getMetaData();
            System.out.println();
            for(int i = 1; i <= mrs.getColumnCount(); i++) {
                System.out.print(mrs.getColumnLabel(i)+"  ");
            }
            System.out.println();*/
            //statement.close();
            //connection.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void resetDB(){
        try{
            this.statement.executeUpdate("drop table TAGS");
            this.statement.executeUpdate("drop table TAGLIST");
            this.statement.executeUpdate("DROP TABLE RECORD");

            this.statement.executeUpdate("CREATE TABLE RECORD (id INTEGER, type INTEGER, title TEXT, content TEXT, setDate DATE, endTime DATETIME, PRIMARY KEY(id ASC))");
            this.statement.executeUpdate("CREATE TABLE TAGS (id INTEGER, tag TEXT)");
            this.statement.executeUpdate("CREATE TABLE TAGLIST (tag TEXT PRIMARY KEY, counter INTEGER)");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void resetSettings(){
        try{
            this.statement.executeUpdate("DROP TABLE SETTINGS");
            this.statement.executeUpdate("CREATE TABLE SETTINGS (setting TEXT PRIMARY KEY, value TEXT)");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    //maybe make the diary unchangable after the day
    public void saveDiary(String title, String diary_text){
        try{
            ResultSet rs = this.statement.executeQuery("SELECT MAX(id) FROM RECORD");
            rs.next();
            int newID = rs.getInt(1)+1;
            //System.out.println(String.format("insert into RECORD values(%d, 0, '%s', '%s', DATE(), NULL)", newID, title.replaceAll("'", "''"), diary_text.replaceAll("'", "''")));
            this.statement.executeUpdate(String.format("insert into RECORD values(%d, 0, '%s', '%s', DATE(), NULL)", newID, title.replaceAll("'", "''"), diary_text.replaceAll("'", "''")));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void saveDiary(String title, String diaryContent, ArrayList<String> tags){
        try{
            ResultSet rs = this.statement.executeQuery("SELECT MAX(id) FROM RECORD");
            rs.next();
            int newID = rs.getInt(1)+1;
            //System.out.println(String.format("insert into RECORD values(%d, 0, '%s', '%s', DATE(), NULL)", newID, title.replaceAll("'", "''"), diaryContent.replaceAll("'", "''")));
            this.statement.executeUpdate(String.format("INSERT INTO RECORD VALUES(%d, 0, '%s', '%s', DATE(), NULL)", newID, title.replaceAll("'", "''"), diaryContent.replaceAll("'", "''")));
            //rs = this.statement.executeQuery(String.format("SELECT * FROM RECORD WHERE id = %d", newID));
            //rs.next();
            //System.out.println(String.format("%d %d %s %s %s %s", rs.getInt("id"), rs.getInt("type"), rs.getString("title"), rs.getString("content"), rs.getString("setDate"), rs.getString("endTime")));
            saveTags(newID, tags);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void saveEvent(String title, String eventContent, String endTime, ArrayList<String> tags){
        try{
            ResultSet rs = this.statement.executeQuery("SELECT MAX(id) FROM RECORD");
            rs.next();
            int newID = rs.getInt(1)+1;
            this.statement.executeUpdate(String.format("insert into RECORD values(%d, 1, '%s', '%s', DATE(), '%s')", newID, title.replaceAll("'", "''"), eventContent.replaceAll("'", "''"), endTime));
            saveTags(newID, tags);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<HashMap<String, String>> listRecord(){
        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        try{
            ResultSet rs = this.statement.executeQuery("SELECT id, title, setDate FROM RECORD");
            HashMap<String, String> aresult;
            while (rs.next()) {
                aresult = new HashMap<String, String>();
                aresult.put("id", rs.getString("id"));
                aresult.put("title", rs.getString("title"));
                aresult.put("setDate", rs.getString("setDate"));
                result.add(aresult);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    //if no input, it's not considered a filter
    //id, type, title(includes), content(includes), setBefore, setAfter, setDate, endBefore, endAfter, endDate, tag(AND logic)
    //use YYYY-MM-DD to input date
    //condition default ASC
    //the returned Arraylist always contain 6 values, but not requested indexes are NULL
    //I'll need to escape all special letters
    public ArrayList<HashMap<String, String>> searchRecord(HashMap<String, String> conditionSet, ArrayList<String> tags, ArrayList<String> request, int limit, ArrayList<Pair<String, Boolean>> conditionOrder){
        ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();
        String search = "SELECT";
        /*//int returnColumnNumber = 0;
        
        for (int i = 1, isFirst = 1;i<RECORD_COLUMN_COUNT;i++){
            if (ifReturn[i]){
                search += String.format(", %s", RECORD_COLUMN[i]);
                //returnColumnNumber++;
            }
        }*/
        for (int i = 0, isFirst = 1;i<request.size();i++){
            if (isFirst == 1){
                search += String.format(" %s", request.get(i));
                isFirst = 0;
                continue;
            }
            search += String.format(", %s", request.get(i));
        }
        if (conditionSet.size() != 0){
            search += " FROM RECORD WHERE";
            int id = Integer.parseInt(conditionSet.getOrDefault("id", "-1"));
            int type = Integer.parseInt(conditionSet.getOrDefault("type", "-1"));
            String title =  conditionSet.getOrDefault("title", "");
            String content = conditionSet.getOrDefault("content", "");
            String setBefore = conditionSet.getOrDefault("setBefore", "");
            String setAfter = conditionSet.getOrDefault("setAfter", "");
            String setDate = conditionSet.getOrDefault("setDate", "");
            String endBefore = conditionSet.getOrDefault("endBefore", "");
            String endAfter = conditionSet.getOrDefault("endAfter", "");
            String endDate = conditionSet.getOrDefault("endDate", "");

            if (id != -1) search += String.format(" id = %d", id);
            if (type != -1) search += String.format(" type = %d", type);
            if (!title.equals("")) search += " title LIKE '%" + String.format("%s", title.replaceAll("'", "''").replaceAll("%", "~%")) + "%' ESCAPE '~'";
            if (!content.equals("")) search += " content LIKE '%" + String.format("%s", content.replaceAll("'", "''").replaceAll("%", "~%")) + "%' ESCAPE '~'";
            if (!setBefore.equals("")) search += String.format(" setDate < '%s'", setBefore);
            if (!setAfter.equals("")) search += String.format(" setDate > '%s'", setAfter);
            if (!setDate.equals("")) search += String.format(" setDate = '%s'", setDate);
            if (!endBefore.equals("")) search += String.format(" endTime < '%s'", endBefore);
            if (!endAfter.equals("")) search += String.format(" endTime > '%s'", endAfter);
            if (!endDate.equals("")) search += String.format(" endTime = '%s'", endDate);
        }
        else{
            search += " FROM RECORD";
        }

        if (conditionOrder.size() != 0){
            search += " ORDER BY";
            boolean isFirst = true;
            for (Pair<String, Boolean> apair : conditionOrder){
                if (isFirst){
                    search += String.format(" %s %s", apair.first, apair.second ? "DESC" : "ASC");
                    isFirst = false;
                    continue;
                }
                search += String.format(", %s %s", apair.first, apair.second ? "DESC" : "ASC");
            }
        }

        
        
        String searchTag = "SELECT * FROM TAGS WHERE";
        Boolean isFirst = true;
        for (String tag : tags){
            if (isFirst){
                searchTag += String.format(" tag LIKE '%s' ESCAPE '~'", tag.replaceAll("'", "''").replaceAll("%", "~%"));
                isFirst = false;
                continue;
            }
            searchTag += String.format(" OR tag LIKE '%s'  ESCAPE '~'", tag.replaceAll("'", "''").replaceAll("%", "~%"));
        }

        try{
            TreeMap<Integer, Integer> tagMap = new TreeMap<Integer, Integer>();
            if (tags.size() != 0){
                //System.out.println(String.format("searchTag: %s", searchTag));
                ResultSet tagRs = this.statement.executeQuery(searchTag);
                while(tagRs.next()){
                    tagMap.merge(tagRs.getInt("id"), 1, Integer::sum);
                }
            }

            //System.out.println(String.format("search: %s", search));
            ResultSet rs = this.statement.executeQuery(search);
            int resultCount = 0;

            while(rs.next()){
                HashMap<String, String> aResult;
                //System.out.println(String.format("%d %s %d", rs.getInt("id"), rs.getString("title"), tagMap.getOrDefault(rs.getInt("id"), 0)));
                if (tagMap.getOrDefault(rs.getInt("id"), 0) == tags.size()){
                    aResult = new HashMap<String, String>();
                    for (String i : request){
                        aResult.put(i, rs.getString(i));
                    }
                    //int elementCount = 1;
                    /*for (int i = 0;i<ifReturn.length;i++){
                        if (ifReturn[i]){
                            aResult.add(rs.getString(elementCount++));
                            continue;
                        }
                        aResult.add(null);
                    }*/
                    output.add(aResult);
                    resultCount++;
                    if (limit > 0 && resultCount >= limit)
                        break;
                }
            }
        }
        catch(Exception e){
            System.out.println("Some sql stuff went wrong");
            e.printStackTrace();
        }

        return output;
    }
    
    public void deleteRecord(int id){
        try{
            this.statement.executeUpdate(String.format("DELETE FROM RECORD WHERE id = %d", id));
            removeTags(id);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    //I actually don't want diary to be editable
    /*public editDiary(int id, String title, String content, ArrayList<String> tags){
        try{
            this.statement.executeUpdate(String.format("UPDATE RECORD SET "))
        }
    }*/
    public void editRecord(int id, String endTime, String title, String content, ArrayList<String> tags){
        //there might be a bug when I edit endtime of a diary
        try{
            this.statement.executeUpdate(String.format("UPDATE RECORD SET endTime = '%s', title = '%s', content = '%s' WHERE id = %d", endTime, title.replaceAll("'", "''"), content.replaceAll("'", "''"), id));
            removeTags(id);
            saveTags(id, tags);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    //getRecord all using id to get a specific record, use All to get every column, use Content to get content, use Custom to get some specific columns
    public HashMap<String, String> getRecordAll(int id){
        HashMap<String, String> result = new HashMap<String, String>();
        try{
            ResultSet rs = this.statement.executeQuery(String.format("SELECT * FROM RECORD WHERE id = %d", id));
            rs.next();
            
            for (String i : RECORD_COLUMN){
                result.put(i, rs.getString(i));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public String getRecordContent(int id){
        try{
            ResultSet rs = this.statement.executeQuery(String.format("SELECT content FROM RECORD WHERE id = %d", id));
            rs.next();
            return rs.getString("content");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
    //[id, type, title, content, setDate, endTime]
    public HashMap<String, String> getRecordCustom(int id, ArrayList<String> request){
        HashMap<String, String> result = new HashMap<String, String>();
        String sqlQuery = "SELECT";
        boolean isFirst = true;
        for (String i : request){
            if (isFirst){
                sqlQuery += String.format(" %s", i);
                isFirst = false;
                continue;
            }
            sqlQuery += String.format(", %s", i);
        }
        try{
            ResultSet rs = this.statement.executeQuery(sqlQuery + String.format(" WHERE id = %d", id));
            rs.next();
            for (String i : request){
                result.put(i, rs.getString(i));
            }
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    private void saveTags(int id, ArrayList<String> tags){
        HashSet<String> aset = new HashSet<String>(tags);
        try{
            for (String s : aset){
                this.statement.executeUpdate(String.format("INSERT INTO TAGS VALUES(%d, '%s')", id, s.replaceAll("'", "''")));
                this.statement.executeUpdate(String.format("INSERT INTO TAGLIST VALUES('%s', 1) ON CONFLICT(tag) DO UPDATE SET counter = counter + 1", s.replaceAll("'", "''")));
                /*
                if (!rs.next())
                    this.statement.executeUpdate(String.format("INSERT INTO TAGLIST VALUES('%s', 1)", s.replaceAll("'", "''")));
                else
                    this.statement.executeUpdate(String.format("UPDATE TAGLIST SET counter = counter+1 WHERE tag = '%s'", s));*/
                
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void removeTags(int id){
        try{
            ResultSet rs = this.statement.executeQuery(String.format("SELECT tag FROM TAGS WHERE id = %d", id));
            ArrayList<String> tags = new ArrayList<String>();
            while (rs.next()){
                //note that even if a tag has count of 0, it doesn't get deleted.
                tags.add(rs.getString("tag"));
                //System.out.println("executed");
                //this.statement.executeUpdate(String.format("UPDATE TAGLIST SET counter = counter-1 WHERE tag = '%s'", rs.getString("tag").replaceAll("'", "''")));
            }
            for (String tag : tags){
                this.statement.executeUpdate(String.format("UPDATE TAGLIST SET counter = counter-1 WHERE tag = '%s'", tag.replaceAll("'", "''")));
            }
            this.statement.executeUpdate(String.format("DELETE FROM TAGS WHERE id = %d", id));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<Pair<String, Integer>> tagList(){
        try{
            ArrayList<Pair<String, Integer>> result = new ArrayList<Pair<String, Integer>>();
            ResultSet rs = this.statement.executeQuery("SELECT * FROM TAGLIST");
            while (rs.next()){
                result.add(new Pair<String, Integer>(rs.getString("tag"), rs.getInt("counter")));
            }
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ArrayList<Pair<String, Integer>>();
    }
    public ArrayList<Pair<String, Integer>> tags(){
        ArrayList<Pair<String, Integer>> result = new ArrayList<Pair<String, Integer>>();
        try{
            ResultSet rs = this.statement.executeQuery("SELECT * FROM TAGS");
            while(rs.next()){
                result.add(new Pair<String, Integer>(rs.getString("tag"), rs.getInt("id")));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void setSetting(String setting, String value){
        try{
            value = value.replaceAll("'", "''");
            this.statement.executeUpdate(String.format("INSERT INTO SETTINGS VALUES('%s', '%s') ON CONFLICT(setting) DO UPDATE SET value = '%s'", setting, value, value));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
    public String getSetting(String setting){
        try{
            ResultSet rs = this.statement.executeQuery(String.format("SELECT value FROM SETTINGS WHERE setting = '%s'", setting.replaceAll("'", "''")));
            rs.next();
            return rs.getString("value");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public HashMap<String, String> getSettings(){
        try{
            HashMap<String, String> settings = new HashMap<String, String>();
            ResultSet rs = this.statement.executeQuery("SELECT * FROM SETTINGS");
            while (rs.next()){
                settings.put(rs.getString("setting"), rs.getString("value"));
            }
            return settings;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new HashMap<String, String>();
    }
}
class idStrPair {
    int id;
    String str;
    idStrPair(int a, String b){
        this.id = a;
        this.str = b;
    }
}