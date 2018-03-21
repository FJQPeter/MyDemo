package cn.hadoop.client;

import java.io.Serializable;

/**
 * Created by FangYan on 2017/10/10 0010.
 */
public class ItemBeanSer implements Serializable{
    private long id;
    private float value;

    public ItemBeanSer(long id, float value) {
        this.id = id;
        this.value = value;
    }


}
