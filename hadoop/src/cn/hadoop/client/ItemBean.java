package cn.hadoop.client;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by FangYan on 2017/10/10 0010.
 */
public class ItemBean implements Writable{
    private long id;
    private float value;

    public ItemBean(long id, float value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
            dataOutput.writeLong(id);
            dataOutput.writeDouble(value);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {

    }


}
