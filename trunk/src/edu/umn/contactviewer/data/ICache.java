package edu.umn.contactviewer.data;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dalcantara
 * Date: 2/25/12
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ICache<Item> {
    void Add(Item item);
    Item Get(String key);
    List<Item> GetAll();
}
