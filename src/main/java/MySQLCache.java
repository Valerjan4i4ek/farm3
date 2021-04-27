import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MySQLCache
{
    private Map<Integer, Field> cache;
    MySQLClass2 sql;
    List<Integer> listCash;
    Map<Integer, Long> mapTime;

    public MySQLCache(int cash, int fieldSize){
        this.cache = new ConcurrentHashMap<>();
        sql = new MySQLClass2();
        this.listCash = new CopyOnWriteArrayList<>();
        this.mapTime = new ConcurrentHashMap<>();
        init(fieldSize);
        initCash(cash);
        initTime();
    }

    public void addPlant(Integer idField, Field field){
        cache.put(idField, field);
        if(field.getPlant() != null){
            sql.addPlant(idField, field);
        }
    }

    public void addCash(Integer cash){
        listCash.add(cash);
        sql.addCash(cash);
    }

    public void addTime(Integer fieldId, Long time){
        mapTime.put(fieldId, time);
        sql.addTime(fieldId, time);
    }

    public void initTime(){
        try{
            Map<Integer, Long> tmp = sql.cacheTime();
            if(tmp != null && !tmp.isEmpty()){
                for (int id : tmp.keySet()){
                    long time = tmp.get(id);
                    mapTime.put(id, time);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initCash(int cash){
        try{
            listCash.add(cash);
            List<Integer> list = sql.cacheCash();
            if(list != null && !list.isEmpty()){
                for(int i : list){
                    listCash.add(i);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void init(int fieldSize){
        try{
            for (int i = 0; i < fieldSize; i++)
            {
                cache.put(i, new Field());
            }
            Map<Integer, Field> tmp = sql.cache();

            if (tmp != null && !tmp.isEmpty()){
                for(int id : tmp.keySet()){
                    Field field = tmp.get(id);
                    if (field != null){
                        cache.put(id, field);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Map<Integer, Field> getFields()
    {
        return cache;
    }

    public Integer getCash(){
        return listCash.get(listCash.size()-1);
    }


    public Map<Integer, Long> getTime() {
        return mapTime;
    }

    public void deleteTime(int i){
        sql.deleteTime(i);
//        mapTime.remove(i);
    }
}
