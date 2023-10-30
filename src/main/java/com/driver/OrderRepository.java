package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    private HashMap<String, Order> ordersMap;

    private HashMap<String, DeliveryPartner> partnersMap;

    private HashMap<String, List<String>> partnerVsOrders;

    private HashMap<String, String> orderVsPartner;

    public OrderRepository(){
        this.ordersMap = new HashMap<>();
        this.partnersMap = new HashMap<>();
        this.partnerVsOrders = new HashMap<>();
        this.orderVsPartner = new HashMap<>();
    }

    public HashMap<String, Order> getOrdersMap() {
        return ordersMap;
    }

    public HashMap<String, DeliveryPartner> getPartnersMap() {
        return partnersMap;
    }

    public HashMap<String, List<String>> partnerVsOrders() {
        return partnerVsOrders;
    }

    public HashMap<String, String> getOrderVsPartner() {
        return orderVsPartner;
    }

    public void addOrder(Order order){
        String oId = order.getId();
        ordersMap.put(oId, order);
    }

    public void addPartner(DeliveryPartner partner){
        String pId = partner.getId();
        partnersMap.put(pId, partner);
    }

    public void assignOrder(String pId, String oId){
        if(!partnerVsOrders.containsKey(pId)){
            partnerVsOrders.put(pId, new ArrayList<String>());
        }
        partnerVsOrders.get(pId).add(oId);

        partnersMap.get(pId).setNumberOfOrders(partnerVsOrders.get(pId).size());

        // updating order vs partner map also
        orderVsPartner.put(oId, pId);
    }

    public Order getOrderById(String oId){
        return ordersMap.get(oId);
    }

    public DeliveryPartner getPartnerById(String pId){
        return partnersMap.get(pId);
    }

    public int numOfOrdersAssignedToPartner(String pId){
        return partnersMap.get(pId).getNumberOfOrders();
    }

    public List<String> getListOfOrdersByPartner(String pId){
        return partnerVsOrders.get(pId);
    }

    public List<String> getAllOrders(){
        List<String> orders = new ArrayList(ordersMap.keySet());
        return orders;
    }

    public int getCountOfUnassignedOrders(){
        return ordersMap.size() - orderVsPartner.size();
    }

    public void deletePartner(String pId){
        // remove from partners map
        partnersMap.remove(pId);

        // need to un assign this partner orders
        if(partnerVsOrders.containsKey(pId)){
            List<String> orders = partnerVsOrders.get(pId);
            partnerVsOrders.remove(pId);

            for(String oId: orders){
                // this order should be removed from order vs partner map
                orderVsPartner.remove(oId);
            }
        }
    }

    public void deleteOrder(String oId){
        // remove from orders map
        ordersMap.remove(oId);

        // need to remove from partner's orders list also
        // get assigned partner
        if(orderVsPartner.containsKey(oId)){
            String pId = orderVsPartner.get(oId);
            orderVsPartner.remove(oId);

            List<String> orders = partnerVsOrders.get(pId);

            for(int i=0; i<orders.size(); i++){
                if(orders.get(i).equals(oId)){
                    orders.remove(i);
                    break;
                }
            }
            partnersMap.get(pId).setNumberOfOrders(partnerVsOrders.get(pId).size());
        }
    }
}
