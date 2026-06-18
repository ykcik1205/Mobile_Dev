package com.example.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DataWareHouse {
    public static ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        Category c1=new Category("c1","Trái cây","Trái cây organic");
        Category c2=new Category("c2","Rau củ","Rau củ organic");
        Category c3=new Category("c3","Thịt","Thịt tươi ủ mát");
        Category c4=new Category("c4","Hải sản","Hải sản tươi sống");
        categories.add(c1);
        categories.add(c2);
        categories.add(c3);
        categories.add(c4);
        return categories;
    }
    public static ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();
        ArrayList<Category> categories = getCategories();

        // Nhóm Trái cây (c1)
        Product p1=new Product("p1","Dâu tây Đà Lạt 250g",20,80000,0,0.08,categories.get(0).getCateId());
        Product p2=new Product("p2","Việt quất New Zealand 125g",15,150000,0,0.08,categories.get(0).getCateId());
        Product p3=new Product("p3","Táo Gala 1kg",10,120000,0,0.08);
        Product p8=new Product("p8","Cam sành 1kg",30,35000,0,0.08);
        Product p9=new Product("p9","Nho mẫu đơn 500g",5,450000,0.08,0.08);
        p3.setCateId(categories.get(0).getCateId());
        p8.setCateId(categories.get(0).getCateId());
        p9.setCateId(categories.get(0).getCateId());

        // Nhóm Rau củ (c2)
        Product p4=new Product("p4","Cải thìa 500g",20,20000,0,0.08);
        Product p5=new Product("p5","Bắp cải 500g",20,30000,0,0.08);
        Product p10=new Product("p10","Khoai tây Đà Lạt 1kg",25,45000,0,0.08);
        Product p11=new Product("p11","Cà rốt organic 500g",15,25000,0,0.08);
        p4.setCateId(categories.get(1).getCateId());
        p5.setCateId(categories.get(1).getCateId());
        p10.setCateId(categories.get(1).getCateId());
        p11.setCateId(categories.get(1).getCateId());

        // Nhóm Thịt (c3)
        Product p6=new Product("p6","Ức gà 500g",20,60000,0,0.08);
        Product p12=new Product("p12","Ba rọi heo 500g",10,110000,0,0.08);
        Product p13=new Product("p13","Thịt bò Úc 250g",8,250000,0.1,0.08);
        p6.setCateId(categories.get(2).getCateId());
        p12.setCateId(categories.get(2).getCateId());
        p13.setCateId(categories.get(2).getCateId());

        // Nhóm Hải sản (c4)
        Product p7=new Product("p7","Tôm thẻ lột đông lạnh 500g",20,150000,0,0.08);
        Product p15=new Product("p15","Cá hồi Nauy 200g",12,220000,0,0.08);
        Product p16=new Product("p16","Mực ống tươi 500g",15,180000,0,0.08);
        Product p17=new Product("p17","Ốc móng tay tươi 500g",10,100000,0,0.08);
        p7.setCateId(categories.get(3).getCateId());
        p15.setCateId(categories.get(3).getCateId());
        p16.setCateId(categories.get(3).getCateId());
        p17.setCateId(categories.get(3).getCateId());

        // Thêm tất cả vào danh sách
        products.add(p1); products.add(p2); products.add(p3); products.add(p8); products.add(p9);
        products.add(p4); products.add(p5); products.add(p10); products.add(p11);
        products.add(p6); products.add(p12); products.add(p13); products.add(p17);
        products.add(p7); products.add(p15); products.add(p16);

        return products;
    }
    public static Product downloadProduct(int i)
    {
        ArrayList<Product> products=getProducts();
        if(i<0 || i>products.size())
            return null;
        return products.get(i);
    }

    public static ArrayList<Employee> getEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(new Employee("e1", "Nguyễn Văn Tèo", "09134579685"));
        employees.add(new Employee("e2", "Trần Thị Tí", "0987654321"));
        employees.add(new Employee("e3", "Lê Văn Sửu", "0901234567"));
        employees.add(new Employee("e4", "Phạm Hồng Dần", "09123456978"));
        employees.add(new Employee("e5", "Hoàng Minh Mão", "0923456789"));
        employees.add(new Employee("e6", "Võ Văn Thìn", "0934567890"));
        employees.add(new Employee("e7", "Đặng Thanh Tỵ", "0945678901"));
        employees.add(new Employee("e8", "Bùi Công Ngọ", "0956789012"));
        employees.add(new Employee("e9", "Ngô Bảo Mùi", "0967890123"));
        employees.add(new Employee("e10", "Lý Hữu Thân", "0978901234"));

        return employees;
    }
    public static ArrayList<Customer> getCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        cal.set(1985, Calendar.MAY, 15);
        customers.add(new Customer("c1", "Nguyễn Văn Tèo", "1, Bến Thành, Q1", "09134579685", "teo@gmail.com", cal.getTime()));

        cal.set(1990, Calendar.AUGUST, 20);
        customers.add(new Customer("c2", "Trần Thị Hoa", "12 Lê Lợi, Q1", "0987654321", "hoa.tran@gmail.com", cal.getTime()));

        cal.set(1975, Calendar.DECEMBER, 30);
        customers.add(new Customer("c3", "Lê Văn Nam", "45 Nguyễn Huệ, Q1", "0901234567", "namle@yahoo.com", cal.getTime()));

        cal.set(1995, Calendar.MARCH, 10);
        customers.add(new Customer("c4", "Phạm Thị Mai", "78 CMT8, Q3", "0912345678", "maipham@outlook.com", cal.getTime()));

        cal.set(1968, Calendar.NOVEMBER, 25);
        customers.add(new Customer("c5", "Hoàng Văn Dũng", "102 Võ Văn Tần, Q3", "0923456789", "dunghoang@gmail.com", cal.getTime()));

        cal.set(2000, Calendar.JANUARY, 5);
        customers.add(new Customer("c6", "Đặng Thu Thảo", "23 Phan Xích Long, PN", "0934567890", "thaodang@gmail.com", cal.getTime()));

        cal.set(1982, Calendar.JULY, 18);
        customers.add(new Customer("c7", "Bùi Minh Tuấn", "56 Quang Trung, GV", "0945678901", "tuanbui@gmail.com", cal.getTime()));

        cal.set(2010, Calendar.SEPTEMBER, 12);
        customers.add(new Customer("c8", "Ngô Thanh Vân", "89 Cộng Hòa, TB", "0956789012", "vanngo@gmail.com", cal.getTime()));

        cal.set(1965, Calendar.FEBRUARY, 28);
        customers.add(new Customer("c9", "Lý Hải", "120 Hùng Vương, Q5", "0967890123", "haily@gmail.com", cal.getTime()));

        cal.set(1970, Calendar.JUNE, 15);
        customers.add(new Customer("c10", "Võ Hoài Linh", "34 Trần Hưng Đạo, Q1", "0978901234", "linhvo@gmail.com", cal.getTime()));

        return customers;
    }

    public static ArrayList<Order> getOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<Employee> employees = getEmployees();
        ArrayList<Customer> customers = getCustomers();
        OrderStatus[] statuses = OrderStatus.values();
        Calendar cal = Calendar.getInstance();

        int orderCount = 1;

        // Tạo hóa đơn cho năm 2024 (40 hóa đơn)
        for (int i = 0; i < 40; i++) {
            int month = i % 12;
            int day = (i % 28) + 1;
            cal.set(2024, month, day, 8 + (i % 10), (i * 7) % 60);
            orders.add(new Order("o" + orderCount,
                    employees.get(i % 10).getId(),
                    customers.get((i + 1) % 10).getCustomerId(),
                    cal.getTime(),
                    statuses[i % statuses.length]));
            orderCount++;
        }

        // Tạo hóa đơn cho năm 2025 (40 hóa đơn)
        for (int i = 0; i < 40; i++) {
            int month = i % 12;
            int day = (i % 28) + 1;
            cal.set(2025, month, day, 9 + (i % 8), (i * 13) % 60);
            orders.add(new Order("o" + orderCount,
                    employees.get((i + 2) % 10).getId(),
                    customers.get((i + 3) % 10).getCustomerId(),
                    cal.getTime(),
                    statuses[(i + 1) % statuses.length]));
            orderCount++;
        }

        // Tạo hóa đơn cho Quý I 2026 (20 hóa đơn)
        for (int i = 0; i < 20; i++) {
            int month = i % 3; // Tháng 0, 1, 2 (Jan, Feb, Mar)
            int day = (i % 28) + 1;
            cal.set(2026, month, day, 10 + (i % 7), (i * 17) % 60);
            orders.add(new Order("o" + orderCount,
                    employees.get((i + 4) % 10).getId(),
                    customers.get((i + 5) % 10).getCustomerId(),
                    cal.getTime(),
                    statuses[(i + 2) % statuses.length]));
            orderCount++;
        }

        return orders;
    }
    public static ArrayList<OrderDetail> getOrderDetails() 
    {
        ArrayList<Order> orders = getOrders();
        ArrayList<Product> products = getProducts();
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        Random rd = new Random();
        int count = 1;
        for (Order order : orders) {
            int n = rd.nextInt(10) + 1; // Mỗi Order có từ 1 tới 10 OrderDetail
            for (int i = 0; i < n; i++) {
                Product p = products.get(rd.nextInt(products.size()));
                int quantity = rd.nextInt(5) + 1;
                double price = p.getPrice();
                double coupon = rd.nextInt(16) / 100.0; // Coupon từ 0% đến 15%
                double vat = (rd.nextInt(6) + 5) / 100.0; // VAT từ 5% đến 10%
                
                OrderDetail detail = new OrderDetail(
                        "od" + count++,
                        order.getOrderId(),
                        p.getProductId(),
                        quantity,
                        price,
                        coupon,
                        vat
                );
                orderDetails.add(detail);
            }
        }
        return orderDetails;
    }
    public static double sumOfMoney(Order od) 
    {
        double sum = 0;
        ArrayList<OrderDetail> orderDetails = getOrderDetails();
        for (OrderDetail detail : orderDetails) {
            if (detail.getOrderId().equals(od.getOrderId())) {
                // Trị giá = (Số lượng * Đơn giá) * (1 - Coupon) * (1 + VAT)
                double lineTotal = detail.getQuantity() * detail.getPrice();
                lineTotal = lineTotal * (1 - detail.getCoupon());
                lineTotal = lineTotal * (1 + detail.getVAT());
                sum += lineTotal;
            }
        }
        return sum;
    }
    public static ArrayList<Order> filterOrdersByDate(Date fromDate, Date toDate) 
    {
        ArrayList<Order> orders = getOrders();
        ArrayList<Order> result_filter = new ArrayList<>();

        // Chuẩn hoá fromDate về 0h0p0s
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(fromDate);
        calFrom.set(Calendar.HOUR_OF_DAY, 0);
        calFrom.set(Calendar.MINUTE, 0);
        calFrom.set(Calendar.SECOND, 0);
        calFrom.set(Calendar.MILLISECOND, 0);

        // Chuẩn hoá toDate về 0h0p0s
        Calendar calTo = Calendar.getInstance();
        calTo.setTime(toDate);
        calTo.set(Calendar.HOUR_OF_DAY, 0);
        calTo.set(Calendar.MINUTE, 0);
        calTo.set(Calendar.SECOND, 0);
        calTo.set(Calendar.MILLISECOND, 0);

        for (Order order : orders) {
            // Chuẩn hoá ngày hoá đơn về 0h0p0s để so sánh
            Calendar calOrder = Calendar.getInstance();
            calOrder.setTime(order.getOrderDate());
            calOrder.set(Calendar.HOUR_OF_DAY, 0);
            calOrder.set(Calendar.MINUTE, 0);
            calOrder.set(Calendar.SECOND, 0);
            calOrder.set(Calendar.MILLISECOND, 0);

            // Kiểm tra trong khoảng [fromDate, toDate]
            if (calOrder.compareTo(calFrom) >= 0 && calOrder.compareTo(calTo) <= 0) {
                result_filter.add(order);
            }
        }
        
        return result_filter;
    }
        public static ArrayList<Order> filterOrdersByStatus(OrderStatus status) {
        ArrayList<Order> orders = getOrders();
        if (status == OrderStatus.ALL) {
            return orders;
        }
        ArrayList<Order> result_filter = new ArrayList<>();
        for (Order order : orders) {
            if (order.getOrderStatus() == status) {
                result_filter.add(order);
            }
        }
        return result_filter;
    }
}
