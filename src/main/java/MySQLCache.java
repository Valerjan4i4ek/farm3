import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MySQLCache
{
    private Map<Integer, Field> cache;
    MySQLClass2 sql;
    List<Integer> listCash;

    public MySQLCache(int cash, int fieldSize){
        this.cache = new ConcurrentHashMap<>();
        sql = new MySQLClass2();
        this.listCash = new CopyOnWriteArrayList<>();
        init(fieldSize);
        initCash(cash);
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
}
