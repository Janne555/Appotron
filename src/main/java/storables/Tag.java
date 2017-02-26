/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

/**
 *
 * @author Janne
 */
public class Tag {

    private int id;
    private String itemUuid;
    private String serialNumber;
    private String key;
    private String value;

    public Tag(int id, String itemUuid, String serialNumber, String key, String value) {
        this.itemUuid = "" + itemUuid;
        this.serialNumber = "" + serialNumber;
        this.key = "" + key;
        this.value = "" + value;
        this.id = id;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getItemUuid() {
        return itemUuid;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Object[] getObjs() {
        Object[] objs = new Object[4];
        objs[0] = itemUuid;
        objs[1] = serialNumber;
        objs[2] = key;
        objs[3] = value;
        return objs;
    }

    public Object[] getObjsId() {
        Object[] objs = new Object[5];
        objs[0] = itemUuid;
        objs[1] = serialNumber;
        objs[2] = key;
        objs[3] = value;
        objs[4] = id;
        return objs;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

}
