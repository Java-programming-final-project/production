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
public class DBManagerTerminal{
    public static void main(String[] args){
        try{
            DiaryManager db = new DiaryManager();
            boolean keepRunning = true;
            
            while (keepRunning){
                Scanner scanner = new Scanner(System.in);
                System.out.println("1.write diary | 2.view record | 3.note event | 4.edit record | 5.view tags | 6.search | 7.reset | 8.delete | 9.view all tags | 10.reset | other.exit");
                String act = scanner.nextLine();
                if (act.equals(""))
                    break;
                int selection = Integer.parseInt(act), id;
                String title, content, line, endTime;
                ArrayList<String> tags;
                ArrayList<Pair<String, Integer>> allTags;
                switch(selection){
                    case 1:
                        //Maybe set a default title if no input
                        System.out.println("Input title");
                        title = scanner.nextLine();
                        content = "";
                        tags = new ArrayList<String>();
                        System.out.println("type nothing to quit");
                        while (true){
                            line = scanner.nextLine();
                            if (line.equals(""))
                                break;
                            content += line+"\n";
                        }
                        System.out.println("input tags below");
                        while (true){
                            String tag = scanner.nextLine();
                            if (tag.equals(""))
                                break;
                            tags.add(tag);
                        }
                        db.saveDiary(title, content, tags);
                        break;
                    case 2:
                        //add select by tag or title and change order later in development
                        //ResultSet rs = db.statement.executeQuery("SELECT id, setdate, title FROM RECORD ORDER BY setdate DESC");
                        ArrayList<Pair<Integer, String>> resultList = new ArrayList<Pair<Integer, String>>();
                        ArrayList<String> dates = new ArrayList<String>();
                        //HashMap<String, String> a = new HashMap<String, String>();
                        //ArrayList<String> b = new ArrayList<String>(Arrays.asList("id", "title", "setDate"));
                        ArrayList<HashMap<String, String>> rs = db.listRecord();
                        for (HashMap<String, String> d : rs){
                            dates.add(d.get("setTime"));
                            resultList.add(new Pair<Integer, String>(Integer.parseInt(d.get("id")), d.get("title")));
                        }
                        int pageNumber = (rs.size()+4)/5, currentPage = 1;
                        while (true){
                            System.out.println("============================");
                            System.out.println(String.format("page: %d/%d", currentPage, pageNumber));
                            Pair<Integer, String> row;
                            for (int i = -5;i<0;i++){
                                if (currentPage*5+i < rs.size())
                                    row = resultList.get(currentPage*5+i);
                                else
                                    break;
                                System.out.println(String.format("id:%d %s title:%s", row.first, dates.get(currentPage*5+i), row.second));
                            }
                            System.out.print("Enter id to view or prev/next:");
                            String action = scanner.nextLine();
                            if (action.equals(""))
                                break;
                            if (action.chars().allMatch( Character::isDigit )){
                                //rs = db.statement.executeQuery(String.format("select content FROM RECORD where id = %s", ));
                                String returnedContent = db.getRecordContent(Integer.parseInt(action));
                                if (returnedContent.equals(""))
                                    System.out.println("No diary of this id found");
                                else{
                                    System.out.print(returnedContent);
                                    action = scanner.nextLine();
                                }
                                    
                            }
                            if (action.equals("next")){
                                if (currentPage != pageNumber) currentPage++;
                                continue;
                            }
                            if (action.equals("prev")){
                                if (currentPage != 1) currentPage--;
                                continue;
                            }
                            break;
                        }
                        break;
                    case 3:
                        //Maybe set a default title if no input
                        System.out.println("Input title");
                        title = scanner.nextLine();
                        content = "";
                        tags = new ArrayList<String>();
                        System.out.println("type nothing to quit");
                        while (true){
                            line = scanner.nextLine();
                            if (line.equals(""))
                                break;
                            content += line+"\n";
                        }
                        System.out.println("input tags below");
                        while (true){
                            String tag = scanner.nextLine();
                            if (tag.equals(""))
                                break;
                            tags.add(tag);
                        }
                        System.out.println("input endTime below");
                        endTime = scanner.nextLine();
                        db.saveEvent(title, content, endTime, tags);
                        break;
                    case 4:
                        System.out.println("Input id of record to change");
                        id = Integer.parseInt(scanner.nextLine());
                        System.out.println("Input new title");
                        title = scanner.nextLine();
                        content = "";
                        tags = new ArrayList<String>();
                        System.out.println("type nothing to quit");
                        while (true){
                            line = scanner.nextLine();
                            if (line.equals(""))
                                break;
                            content += line+"\n";
                        }
                        System.out.println("input new tags below");
                        while (true){
                            String tag = scanner.nextLine();
                            if (tag.equals(""))
                                break;
                            tags.add(tag);
                        }
                        System.out.println("input new endTime below");
                        endTime = scanner.nextLine();
                        db.editRecord(id, endTime, title, content, tags);
                        break;
                    case 5:
                        ArrayList<Pair<String, Integer>> taglist = db.tagList();
                        for (Pair<String, Integer> pair : taglist){
                            System.out.println(String.format("%s: %d", pair.first, pair.second));
                        }
                        break;
                    case 6:
                        System.out.println("input conditions");
                        HashMap<String, String> conditionSet = new HashMap<String, String>();
                        while(true){
                            String condition = scanner.nextLine();
                            if(condition.equals(""))
                                break;
                            String[] conditionSplit = condition.split(" ");
                            conditionSet.put(conditionSplit[0], conditionSplit[1]);
                        }
                        ArrayList<String> tagsToPass = new ArrayList<String>();
                        System.out.println("input tags");
                        while(true){
                            String tag = scanner.nextLine();
                            if (tag.equals(""))
                                break;
                            tagsToPass.add(tag);
                        }
                        System.out.println("input the return request");
                        ArrayList<String> request = new ArrayList<>();
                        while (true){
                            String r = scanner.nextLine();
                            if (r.equals(""))
                                break;
                            request.add(r);
                        }
                        System.out.println("input return limit");
                        int limit = Integer.parseInt(scanner.nextLine());
                        System.out.println("input order");
                        ArrayList<Pair<String, Boolean>> order = new ArrayList<Pair<String, Boolean>>();
                        while (true){
                            String condition = scanner.nextLine();
                            if (condition.equals(""))
                                break;
                            String[] conditionSplit = condition.split(" ");
                            order.add(new Pair<String, Boolean>(conditionSplit[0], conditionSplit[1].equals("1") ? true : false));
                        }
                        ArrayList<HashMap<String, String>> results = db.searchRecord(conditionSet, tagsToPass, request, limit, order);
                        for (HashMap<String, String> result : results){
                            for (Map.Entry<String, String> i : result.entrySet()){
                                System.out.print(String.format(" %s", i.getValue()));
                            }
                            System.out.print("\n");
                        }
                        break;
                    case 7:
                        db.resetDB();
                        break;
                    case 8:
                        id = Integer.parseInt(scanner.nextLine());
                        db.deleteRecord(id);
                        break;
                    case 9:
                        allTags = db.tags();
                        for (Pair<String, Integer> pair : allTags){
                            System.out.println(String.format("%s %d", pair.first, pair.second));
                        }
                        break;
                    case 10:
                        db.resetDB();
                        break;
                    default:
                        keepRunning = false;
                        break;
                }
            }
            db.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}