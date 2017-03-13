///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package sql.daos;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import sql.db.Database;
//import sql.db.DatabaseCreator;
//import sql.db.Testdata;
//import storables.Item;
//import util.Param;
//import util.Type;
//
///**
// *
// * @author Janne
// */
//public class ItemDaoTest {
//
//    public static ItemDao iDao;
//    public static Database db;
//
//    public ItemDaoTest() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
//        InputStream input = new FileInputStream("application.configuration");
//        System.getProperties().load(input);
//        db = new Database("org.postgresql.Driver",
//                System.getProperties().getProperty("postgre_address"),
//                System.getProperties().getProperty("postgre_user"),
//                System.getProperties().getProperty("postgre_password"));
//
//        String[] strings = {"item", "users", "listitem", "loan", "nutritionalinfo", "serving", "shoppinglist", "tag"};
//        for (String s : strings) {
//            try {
//                db.update("DROP TABLE " + s + " CASCADE");
//            } catch (SQLException e) {
//            }
//        }
//
//        new DatabaseCreator("dbcommands.json", db);
//        Testdata td = new Testdata("data.json");
//        for (String sql : td.getInserts()) {
//            System.out.println(sql);
//            db.update(sql);
//        }
//
//        iDao = new ItemDao(db);
//    }
//
//    @AfterClass
//    public static void tearDownClass() {
//        String[] strings = {"item", "users", "listitem", "loan", "nutritionalinfo", "serving", "shoppinglist", "tag"};
//        for (String s : strings) {
//            try {
//                db.update("DROP TABLE " + s + " CASCADE");
//            } catch (SQLException e) {
//            }
//        }
//    }
//
//    @Before
//    public void setUp() {
//    }
//
//    @After
//    public void tearDown() {
//    }
//
//    /**
//     * Test of findOne method, of class ItemDao.
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testFindOne() throws Exception {
//        System.out.println("findOne");
//        String uuid = "3793471d-8943-4f0d-b0a1-f6bfe6b16603";
//        Item expResult = new Item("3793471d-8943-4f0d-b0a1-f6bfe6b16603", "kirja", "k1rj4", "poyta", null, null, null, Type.ITEM);
//        Item result = iDao.findOne(uuid);
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of findOneBySerial method, of class ItemDao.
//     */
//    @Test
//    public void testFindOneBySerial() throws Exception {
//        System.out.println("findOneBySerial");
//        String serial = "k1rj4";
//        Item expResult = new Item("3793471d-8943-4f0d-b0a1-f6bfe6b16603", "kirja", "k1rj4", "poyta", null, null, null, Type.ITEM);
//        Item result = iDao.findOneBySerial(serial);
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of findAll method, of class ItemDao.
//     */
//    @Test
//    public void testFindAll() throws Exception {
//        System.out.println("findAll");
//        int result = iDao.findAll().size();
//        assertEquals(15, result);
//    }
//
//    /**
//     * Test of findBy method, of class ItemDao.
//     */
//    @Test
//    public void testFindBy() throws Exception {
//        System.out.println("findBy");
//        Map<Param, String> terms = null;
//        ItemDao instance = null;
//        List<Item> expResult = null;
//        List<Item> result = instance.findBy(terms);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of tagSearch method, of class ItemDao.
//     */
//    @Test
//    public void testTagSearch() throws Exception {
//        System.out.println("tagSearch");
//        String key = "";
//        String value = "";
//        ItemDao instance = null;
//        List<Item> expResult = null;
//        List<Item> result = instance.tagSearch(key, value);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getLocations method, of class ItemDao.
//     */
//    @Test
//    public void testGetLocations() throws Exception {
//        System.out.println("getLocations");
//        String[] list = {"poyta", "kaappi", "kirjoituspoyta", "laatikosto", "kaytavan hylly", "kirjahylly", "jaakaappi", "kuivaruokahylly", "mikrokaappi"};
//        boolean result = iDao.getLocations().containsAll(Arrays.asList(list));
//        assertEquals(true, result);
//    }
//
//    /**
//     * Test of create method, of class ItemDao.
//     */
//    @Test
//    public void testCreate() throws Exception {
//        System.out.println("create");
//        Item t = new Item("testiuuid", "testinimi", "testiserial", "testisijainti", new Timestamp(System.currentTimeMillis()), null, null, Type.ITEM);
//        iDao.create(t);
//        Item findOne = iDao.findOne("testiuuid");
//        assertEquals(findOne, t);
//    }
//
//    /**
//     * Test of update method, of class ItemDao.
//     */
//    @Test
//    public void testUpdate() throws Exception {
//        System.out.println("update");
//        Item t = null;
//        ItemDao instance = null;
//        instance.update(t);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of delete method, of class ItemDao.
//     */
//    @Test
//    public void testDelete() throws Exception {
//        System.out.println("delete");
//        iDao.delete("b1708bb4-bae3-42ca-bb55-a6365f87df68");
//        int size = iDao.findAll().size();
//        assertEquals(14, size);
//    }
//
//    /**
//     * Test of getExpiring method, of class ItemDao.
//     */
//    @Test
//    public void testGetExpiring() throws Exception {
//        System.out.println("getExpiring");
//        int number = 0;
//        ItemDao instance = null;
//        boolean expResult = true;
//        boolean result = true;
//        List<Item> list = iDao.getExpiring(5);
//        System.out.println(list.size());
//        if (list.size() != 5) result = false;
//        for (Item it : list) {
//            if (it.getExpiration() == null) result = false;
//        }
//        
//        assertEquals(expResult, result);
//    }
//
//}
