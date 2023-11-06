package model;

import service.GoodsService;

public class OrderItem {
    private int id;
    private float price;
    private int amount;
    private String goodsName;
    private Goods goods;
    private Order order;// order_id
    private int order_id;
    private int goods_id;
    private boolean check=false;//检查是否被改变了成员的值
    private boolean delete=false;//检查该项是否要被删除
    private GoodsService gService=new GoodsService();

    public void setName(String name) {
        this.goodsName=name;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public Goods getGoods() {
        return goods;
    }
    public void setGoods(Goods goods) {
        this.goods = goods;
        this.goods_id=goods.getId();
    }
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
        this.order_id=order.getId();
    }
    public int getOrder_id(){return order_id;}
    public void setOrder_id(int orderId){ this.order_id=orderId; }
    public int getGoods_id(){return goods_id;}
    public void setGoods_id(int goods_id){
        this.goods_id=goods_id;
        this.goods=gService.getGoodsById(goods_id);
    }
    public boolean getCheck(){return check;}
    public void setCheck(boolean check){this.check=check;}
    public boolean getDelete(){return delete;}
    public void setDelete(boolean delete){this.delete=delete;}
    public OrderItem() {
        super();
    }
    public OrderItem(float price, int amount, Goods goods, Order order) {
        super();
        this.price = price;
        this.amount = amount;
        this.goods = goods;
        this.order = order;
        this.order_id=order.getId();
        goods_id=goods.getId();
    }
}
