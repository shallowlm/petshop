package model;

import service.GoodsService;
import service.OrderService;
import utils.PriceUtils;

import java.util.*;

public class Order {
    private int id;
    private float total;//总价
    private int amount;// 商品总数
    private int status;//1未付款/2已付款/3已发货/4已完成
    private int paytype;//1微信/2支付宝
    private String name;
    private String phone;
    private String address;
    private Date datetime;
    private User user;

    //将商品id和商品订单详细表组成一个对
    private Map<Integer,OrderItem> itemMap = new HashMap<Integer,OrderItem>();
    //记录该订单包含的所有的商品订单详细表
    private List<OrderItem> itemList = new ArrayList<OrderItem>();

    public void setUsername(String username) {
        user = new User();
        user.setUsername(username);
    }
    public void addGoods(Goods g) {
        //检查该订单里是否已经存在该商品了，有则在给orderitem的商品数量+1，没有则创建该商品的orderitem
        if(itemMap.containsKey(g.getId())) {
            OrderItem item = itemMap.get(g.getId());
            item.setAmount(item.getAmount()+1);
            item.setCheck(true);
        }else {
            OrderItem item = new OrderItem(g.getPrice(),1,g,this);
            itemMap.put(g.getId(), item);
        }
        amount++;
        total = PriceUtils.add(total, g.getPrice());
        OrderService oService=new OrderService();
        oService.updateOrder(this);
    }

    public List<OrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderItem> itemList) {
        this.itemList = itemList;
    }

    //删1个商品
    public void lessen(int goodsid) {
        if(itemMap.containsKey(goodsid)) {
            OrderItem item = itemMap.get(goodsid);
            item.setAmount(item.getAmount()-1);
            amount--;
            total = PriceUtils.subtract(total, item.getPrice());
            OrderService oService=new OrderService();
            //如果order里没东西了直接删了
            if(amount==0){
                oService.delete(this);
                return;
            }
            //如果删到最后一个了就把该商品的orderitem删了
            if(item.getAmount()<=0) {
                itemMap.get(goodsid).setDelete(true);
                oService.updateOrder(this);
                itemMap.remove(goodsid);
                return;
            }
            //把该项标为要改变的
            itemMap.get(goodsid).setCheck(true);
            oService.updateOrder(this);
        }
    }

    //删1类商品
    public void delete(int goodsid)
    {
        if(itemMap.containsKey(goodsid)) {
            OrderItem item = itemMap.get(goodsid);
            total = PriceUtils.subtract(total, item.getAmount()*item.getPrice());
            amount-=item.getAmount();
            OrderService oService=new OrderService();
            //如果order里没东西了直接删了
            if(amount==0){
                oService.delete(this);
                System.out.println(this.id);
                return;
            }
            //把该项标记为删除
            itemMap.get(goodsid).setDelete(true);
            oService.updateOrder(this);
            itemMap.remove(goodsid);

        }
    }

    public Map<Integer, OrderItem> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<Integer, OrderItem> itemMap) {
        this.itemMap = itemMap;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public float getTotal() {
        return total;
    }
    public void setTotal(float total) {
        this.total = total;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getPaytype() {
        return paytype;
    }
    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Date getDatetime() {
        return datetime;
    }
    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Order() {
        super();
    }
}
