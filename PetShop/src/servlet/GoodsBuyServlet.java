package servlet;

import model.Goods;
import model.Order;
import model.User;
import service.GoodsService;
import service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//将商品放入购物车中
@WebServlet(name = "goods_buy",urlPatterns = "/goods_buy")
public class GoodsBuyServlet extends HttpServlet {
    private GoodsService gService = new GoodsService();
    private OrderService oService=new OrderService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Order o = null;
        User u = (User) request.getSession().getAttribute("user");
        //获取用户购物车状态的订单
        List<Order> list = oService.selectByStatus(u.getId(),1);
        if(!list.isEmpty()){
            o=list.get(0);
            o.setUser(u);
        }
        else{
            o=new Order();
            o.setStatus(1);
            o.setUser(u);
            oService.addOrder(o);
        }
        /*//检查服务器的session有没有订单信息，有则 读取，没有则创建一个新的
        if(request.getSession().getAttribute("order") != null) {
            o = (Order) request.getSession().getAttribute("order");
        }else {
            o = new Order();
            request.getSession().setAttribute("order", o);
        }*/
        int goodsid = Integer.parseInt(request.getParameter("goodsid"));
        Goods goods = gService.getGoodsById(goodsid);
        //如果该商品没有余量了则报错
        if(goods.getStock()==0){
            response.getWriter().print("fail");
            return;
        }
        if(goods.getStock()>0) {
            o.addGoods(goods);
            request.getSession().setAttribute("order", o);
            Order order = (Order) request.getSession().getAttribute("order");
            response.getWriter().print("ok");
        }else {
            response.getWriter().print("fail");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
