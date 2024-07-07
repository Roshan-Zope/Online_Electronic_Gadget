package com.example.onlineelectronicgadget.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.models.Laptop;
import com.example.onlineelectronicgadget.models.Order;
import com.example.onlineelectronicgadget.models.Product;
import com.example.onlineelectronicgadget.models.SmartTv;
import com.example.onlineelectronicgadget.models.SmartWatches;
import com.example.onlineelectronicgadget.models.Tablets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersListAdapter extends RecyclerView.Adapter<OrdersListAdapter.ViewHolder> {
    private List<Map<String, Object>> list;
    private OrderCallback listener;

    public OrdersListAdapter(List<Map<String, Object>> list, OrderCallback listener) {
        this.listener = listener;
        this.list = list;
    }

    public interface OrderCallback {
        void onCall(List<Product> products);
    }

    @NonNull
    @Override
    public OrdersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordes_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersListAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView order_name;
        private TextView total_amount;
        private TextView date;
        private TextView numOfItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numOfItem = itemView.findViewById(R.id.numOfItem);
            order_name = itemView.findViewById(R.id.order_name);
            total_amount = itemView.findViewById(R.id.total_amount);
            date = itemView.findViewById(R.id.date);
        }

        public void bind(Map<String, Object> map) {
            if (map != null) {
                List<Map<String, Object>> productMapList = (List<Map<String, Object>>) map.get("product");
                List<Product> products = new ArrayList<>();
                for (Map<String, Object> productMap : productMapList) {
                    Product product = new Product();

                    String category = (String) productMap.get("category");
                    switch (category) {
                        case "laptop" :
                            product = new Laptop();
                            fillLaptop(product, productMap);
                            break;
                        case "tablet" :
                            product = new Tablets();
                            fillTablet(product, productMap);
                            break;
                        case "watch" :
                            product = new SmartWatches();
                            fillWatch(product, productMap);
                            break;
                        case "tv" :
                            product = new SmartTv();
                            fillTv(product, productMap);
                    }

                    fillProduct(product, productMap);
                    products.add(product);
                }

                Log.d("myTag", "list => " + products);
                order_name.setText(String.valueOf(products.get(0).getModel()));
                total_amount.setText(String.valueOf(getTotalAmount(products)));
                numOfItem.setText("Num of items: " + products.size());
                date.setText(String.valueOf(map.get("date")));
                itemView.setOnClickListener(v -> listener.onCall(products));
            }
        }

        private void fillTv(Product product, Map<String, Object> productMap) {
            try {
                ((SmartTv) product).setResolution((String) productMap.get("resolution"));
            } catch (NullPointerException e) {
                ((SmartTv) product).setResolution(null);
            }
            try {
                ((SmartTv) product).setScreenSize((String) productMap.get("screenSize"));
            } catch (NullPointerException e) {
                ((SmartTv) product).setScreenSize(null);
            }
            try {
                ((SmartTv) product).setSmartFeatures((String) productMap.get("smartFeatures"));
            } catch (NullPointerException e) {
                ((SmartTv) product).setSmartFeatures(null);
            }
            try {
                ((SmartTv) product).setDisplayTechnology((String) productMap.get("displayTechnology"));
            } catch (NullPointerException e) {
                ((SmartTv) product).setDisplayTechnology(null);
            }
            try {
                ((SmartTv) product).setOs((String) productMap.get("os"));
            } catch (NullPointerException e) {
                ((SmartTv) product).setOs(null);
            }
            try {
                ((SmartTv) product).setSound(((String) productMap.get("sound")));
            } catch (NullPointerException e) {
                ((SmartTv) product).setSound(null);
            }
            try {
                ((SmartTv) product).setDimension((String) productMap.get("dimension"));
            } catch (NullPointerException e) {
                ((SmartTv) product).setDimension(null);
            }
            try {
                ((SmartTv) product).setColor((String) productMap.get("color"));
            } catch (NullPointerException e) {
                ((SmartTv) product).setColor(null);
            }
            try {
                ((SmartTv) product).setWarranty((String) productMap.get("warranty"));
            } catch (NullPointerException e) {
                ((SmartTv) product).setWarranty(null);
            }
            try {
                ((SmartTv) product).setPorts((String) productMap.get("ports"));
            } catch (NullPointerException e) {
                ((SmartTv) product).setPorts(null);
            }
            try {
                ((SmartTv) product).setWeight(((Number) productMap.get("weight")).doubleValue());
            } catch (NullPointerException|ClassCastException|NumberFormatException e) {
                ((SmartTv) product).setWeight(0);
            }
        }

        private void fillWatch(Product product, Map<String, Object> productMap) {
            try {
                ((SmartWatches) product).setProcessor((String) productMap.get("processor"));
            } catch (NullPointerException e) {
                ((SmartWatches) product).setProcessor(null);
            }
            try {
                ((SmartWatches) product).setSensor((String) productMap.get("sensor"));
            } catch (NullPointerException e) {
                ((SmartWatches) product).setSensor(null);
            }
            try {
                ((SmartWatches) product).setConnectivity((String) productMap.get("connectivity"));
            } catch (NullPointerException e) {
                ((SmartWatches) product).setConnectivity(null);
            }
            try {
                ((SmartWatches) product).setDisplay((String) productMap.get("display"));
            } catch (NullPointerException e) {
                ((SmartWatches) product).setDisplay(null);
            }
            try {
                ((SmartWatches) product).setWaterResistance((String) productMap.get("waterResistance"));
            } catch (NullPointerException e) {
                ((SmartWatches) product).setWaterResistance(null);
            }
            try {
                ((SmartWatches) product).setBatteryLife((String) productMap.get("batteryLife"));
            } catch (NullPointerException e) {
                ((SmartWatches) product).setBatteryLife(null);
            }
            try {
                ((SmartWatches) product).setColor((String) productMap.get("color"));
            } catch (NullPointerException e) {
                ((SmartWatches) product).setColor(null);
            }
            try {
                ((SmartWatches) product).setWarranty((String) productMap.get("warranty"));
            } catch (NullPointerException e) {
                ((SmartWatches) product).setWarranty(null);
            }
            try {
                ((SmartWatches) product).setWeight(((Number) productMap.get("weight")).doubleValue());
            } catch (NullPointerException|NumberFormatException|ClassCastException e) {
                ((SmartWatches) product).setWeight(0);
            }
        }

        private void fillTablet(Product product, Map<String, Object> productMap) {
            try {
                ((Tablets) product).setProcessor((String) productMap.get("processor"));
            } catch (NullPointerException e) {
                ((Tablets) product).setProcessor(null);
            }
            try {
                ((Tablets) product).setRam((String) productMap.get("ram"));
            } catch (NullPointerException e) {
                ((Tablets) product).setRam(null);
            }
            try {
                ((Tablets) product).setStorage((String) productMap.get("storage"));
            } catch (NullPointerException e) {
                ((Tablets) product).setStorage(null);
            }
            try {
                ((Tablets) product).setConnectivity((String) productMap.get("connectivity"));
            } catch (NullPointerException e) {
                ((Tablets) product).setConnectivity(null);
            }
            try {
                ((Tablets) product).setDisplay((String) productMap.get("display"));
            } catch (NullPointerException e) {
                ((Tablets) product).setDisplay(null);
            }
            try {
                ((Tablets) product).setOs((String) productMap.get("os"));
            } catch (NullPointerException e) {
                ((Tablets) product).setOs(null);
            }
            try {
                ((Tablets) product).setBatteryLife((String) productMap.get("batteryLife"));
            } catch (NullPointerException e) {
                ((Tablets) product).setBatteryLife(null);
            }
            try {
                ((Tablets) product).setDimension((String) productMap.get("dimension"));
            } catch (NullPointerException e) {
                ((Tablets) product).setDimension(null);
            }
            try {
                ((Tablets) product).setColor((String) productMap.get("color"));
            } catch (NullPointerException e) {
                ((Tablets) product).setColor(null);
            }
            try {
                ((Tablets) product).setWarranty((String) productMap.get("warranty"));
            } catch (NullPointerException e) {
                ((Tablets) product).setWarranty(null);
            }
            try {
                ((Tablets) product).setPorts((String) productMap.get("ports"));
            } catch (NullPointerException e) {
                ((Tablets) product).setPorts(null);
            }
            try {
                ((Tablets) product).setWeight(((Number) productMap.get("weight")).doubleValue());
            } catch (NullPointerException|ClassCastException|NumberFormatException e) {
                ((Tablets) product).setProcessor(null);
            }
        }

        private void fillLaptop(Product product, Map<String, Object> productMap) {
            try {
                ((Laptop) product).setProcessor((String) productMap.get("processor"));
            } catch (NullPointerException e) {
                ((Laptop) product).setProcessor(null);
            }
            try {
                ((Laptop) product).setRam((String) productMap.get("ram"));
            } catch (NullPointerException e) {
                ((Laptop) product).setRam(null);
            }
            try {
                ((Laptop) product).setStorage((String) productMap.get("storage"));
            } catch (NullPointerException e) {
                ((Laptop) product).setStorage(null);
            }
            try {
                ((Laptop) product).setGraphics((String) productMap.get("graphics"));
            } catch (NullPointerException e) {
                ((Laptop) product).setGraphics(null);
            }
            try {
                ((Laptop) product).setDisplay((String) productMap.get("display"));
            } catch (NullPointerException e) {
                ((Laptop) product).setDisplay(null);
            }
            try {
                ((Laptop) product).setOs((String) productMap.get("os"));
            } catch (NullPointerException e) {
                ((Laptop) product).setOs(null);
            }
            try {
                ((Laptop) product).setBatteryLife((String) productMap.get("batteryLife"));
            } catch (NullPointerException e) {
                ((Laptop) product).setBatteryLife(null);
            }
            try {
                ((Laptop) product).setDimension((String) productMap.get("dimension"));
            } catch (NullPointerException e) {
                ((Laptop) product).setDimension(null);
            }
            try {
                ((Laptop) product).setColor((String) productMap.get("color"));
            } catch (NullPointerException e) {
                ((Laptop) product).setColor(null);
            }
            try {
                ((Laptop) product).setWarranty((String) productMap.get("warranty"));
            } catch (NullPointerException e) {
                ((Laptop) product).setWarranty(null);
            }
            try {
                ((Laptop) product).setPorts((String) productMap.get("ports"));
            } catch (NullPointerException e) {
                ((Laptop) product).setPorts(null);
            }
            try {
                ((Laptop) product).setWeight(((Number) productMap.get("weight")).doubleValue());
            } catch (NullPointerException|ClassCastException|NumberFormatException e) {
                ((Laptop) product).setWeight(0);
            }
        }

        private void fillProduct(Product product, Map<String, Object> productMap) {
            try {
                product.setModel((String) productMap.get("model"));
            } catch (NullPointerException e) {
                product.setModel(null);
            }
            try {
                product.setImagesId((List<String>) productMap.get("imagesId"));
            } catch (NullPointerException|ClassCastException e) {
                product.setImagesId(null);
            }
            try {
                product.setReviews((List<String>) productMap.get("reviews"));
            } catch (NullPointerException|ClassCastException e) {
                product.setReviews(null);
            }
            try {
                product.setPrice(((Number) productMap.get("price")).doubleValue());
            } catch (NullPointerException|ClassCastException|NumberFormatException e) {
                product.setPrice(0);
            }
            try {
                product.setRating(((Number) productMap.get("rating")).doubleValue());
            } catch (NullPointerException|ClassCastException|NumberFormatException e) {
                product.setRating(0);
            }
            try {
                product.setDescription((String) productMap.get("description"));
            } catch (NullPointerException e) {
                product.setDescription(null);
            }
            try {
                product.setCategory((String) productMap.get("category"));
            } catch (NullPointerException e) {
                product.setCategory(null);
            }
            try {
                product.setBrand((String) productMap.get("brand"));
            } catch (NullPointerException e) {
                product.setBrand(null);
            }
            try {
                product.setStocks(((Number) productMap.get("stocks")).intValue());
            } catch (NullPointerException|ClassCastException|NumberFormatException e) {
                product.setStocks(0);
            }
        }

        private double getTotalAmount(List<Product> products) {
            double total = 0;
            for (Product product : products) {
                total += product.getPrice();
            }
            Log.d("myTag", total +"");
            return total;
        }
    }
}
