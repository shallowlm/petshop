package service;

import dao.*;
import model.*;
import utils.*;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    private OrderDao oDao = new OrderDao();
    public void addOrder(Order order) {
        Connection con = null;
        try {
            con = DataSourceUtils.getConnection();
            con.setAutoCommit(false);

            oDao.insertOrder(con, order);


            int id = oDao.getLastInsertId(con);
            order.setId(id);
            for(OrderItem item : order.getItemMap().values()) {
                oDao.insertOrderItem(con, item);
            }
            con.commit();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if(con!=null)
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
        }
    }
    public void updateOrder(Order order) {
        Connection con = null;
        try {
            con = DataSourceUtils.getConnection();
            con.setAutoCommit(false);

            oDao.updateOrder(con, order);


            int id = oDao.getLastInsertId(con);
            //order.setId(id);
            for(OrderItem item : order.getItemMap().values()) {
                //检查该项是否要被删除
                if(item.getDelete()){
                    oDao.deleteOrderItem(con,item.getId());
                    continue;
                }
                //没有在数据库里的orderitem的id都是0
                if(item.getId()==0) {
                    oDao.insertOrderItem(con, item);
                    //把数据库里的id读入赋给orderitem
                    order.getItemMap().get(item.getGoods_id()).setId(oDao.getOrderItemId(con,item));
                    continue;
                }
                //如果该item被改变了要更新数据库
                if(item.getCheck()){
                    item.setCheck(false);
                    oDao.updateOrderItem(con,item);
                }
                //把数据库里的id读入赋给orderitem
                order.getItemMap().get(item.getGoods_id()).setId(oDao.getOrderItemId(con,item));
            }
            con.commit();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if(con!=null)
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
        }
    }

    //选取一个用户的所以order
    public List<Order> selectAll(int userid){
        List<Order> list=null;
        try {
            list = oDao.selectAll(userid);
            for(Order o :list) {
                List<OrderItem> l = oDao.selectAllItem(o.getId());
                o.setItemList(l);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    //根据订单的支付状态选取订单
    public List<Order> selectByStatus(int userid, int status){
        List<Order> list=null;
        try {
            list = oDao.selectByStatus(status, userid);
            int i=0;
            for(Order o :list) {
                List<OrderItem> l = oDao.selectAllItem(o.getId());
                list.get(i).setItemList(l);
                Map<Integer,OrderItem> itemMap = new HashMap<Integer,OrderItem>();
                for(OrderItem oi:l){
                    itemMap.put(oi.getGoods_id(), oi);
                }
                list.get(i).setItemMap(itemMap);
                i++;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
    public Order selectById(int id){
        Order order=null;
        try {
            order = oDao.selectById(id);
            if(order==null){
                return order;
            }
            List<OrderItem> l = oDao.selectAllItem(order.getId());
            order.setItemList(l);
            Map<Integer,OrderItem> itemMap = new HashMap<Integer,OrderItem>();
            for(OrderItem oi:l){
                itemMap.put(oi.getGoods_id(), oi);
            }
            order.setItemMap(itemMap);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return order;
    }

    //获取订单的页面
    public Page getOrderPage(int status,int pageNumber) {
        Page p = new Page();
        p.setPageNumber(pageNumber);
        int pageSize = 10;
        int totalCount = 0;
        try {
            totalCount = oDao.getOrderCount(status);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        p.SetPageSizeAndTotalCount(pageSize, totalCount);
        List list=null;
        try {
            list = oDao.selectOrderList(status, pageNumber, pageSize);
            for(Order o :(List<Order>)list) {
                List<OrderItem> l = oDao.selectAllItem(o.getId());
                o.setItemList(l);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        p.setList(list);
        return p;
    }
    public void updateStatus(int id,int status) {
        try {
            oDao.updateStatus(id, status);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void delete(Order order){
        Connection con = null;
        try {
            con = DataSourceUtils.getDataSource().getConnection();
            con.setAutoCommit(false);

            for(OrderItem item : order.getItemMap().values()){
                oDao.deleteOrderItem(con,item.getId());
            }
            oDao.deleteOrder(con,order.getId());
            con.commit();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if(con!=null)
                try {
                    con.rollback();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
        }
    }
}
