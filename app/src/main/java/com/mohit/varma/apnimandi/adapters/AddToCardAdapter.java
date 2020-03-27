package com.mohit.varma.apnimandi.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohit.varma.apnimandi.R;
import com.mohit.varma.apnimandi.database.SQLiteDataBaseConnect;
import com.mohit.varma.apnimandi.model.Item;

import java.util.List;

public class AddToCardAdapter extends RecyclerView.Adapter<AddToCardAdapter.AddToCartViewHolder>{

    private Context context;
    private List<Item> arrayList;



    public AddToCardAdapter(Context context, List<Item> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public AddToCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.singleforaddto,parent,false);

        AddToCartViewHolder addToCartViewHolder = new AddToCartViewHolder(view);

        return addToCartViewHolder;

    }

    @Override
    public void onBindViewHolder(final AddToCartViewHolder holder, final int position) {

        final Item item = arrayList.get(position);
        holder.cutoffprice.setText(item.getItemCutOffPrice());
        holder.imageView.setImageBitmap(item.getBitmap());
        holder.nameofthefood.setText(item.getItemName());
        holder.priceofthefood.setText(item.getItemPrice());
        holder.deleteitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SQLiteDataBaseConnect(context).deleteItemfromDatabase(item.getKeyindex());
                arrayList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });

        holder.actual.setText(""+item.getActual_price());
        holder.increment.setText(""+item.getIncre_decre_price());
        holder.finalprice.setText(" = "+item.getFinal_price());
        holder.minusprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrese(Integer.valueOf(holder.increment.getText().toString()),item.getActual_price(),holder.finalprice,holder.increment,item.getKeyindex());
            }
        });
        holder.plusprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increse(Integer.valueOf(holder.increment.getText().toString()),item.getActual_price(),holder.finalprice,holder.increment,item.getKeyindex());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AddToCartViewHolder extends RecyclerView.ViewHolder{


        private Button minusprice,plusprice,deleteitem;
        private TextView increment,actual,finalprice,nameofthefood,priceofthefood,cutoffprice;
        private ImageView imageView;


        public AddToCartViewHolder(@NonNull View itemView) {
            super(itemView);

            minusprice = itemView.findViewById(R.id.minusprice);

            increment = itemView.findViewById(R.id.incrementbyone);

            actual = itemView.findViewById(R.id.actualprice);

            finalprice = itemView.findViewById(R.id.finalprice);

            plusprice = itemView.findViewById(R.id.plusprice);

            imageView = itemView.findViewById(R.id.imagetoBefilled);

            nameofthefood = itemView.findViewById(R.id.databasefoodname);

            priceofthefood = itemView.findViewById(R.id.databasefoodprice);

            cutoffprice = itemView.findViewById(R.id.cutoffprice);

            deleteitem = itemView.findViewById(R.id.deletefromDB);

        }
    }

    public Bitmap createBitmapfrombyteArray(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    public void increse(int takesValue,int multiplyValue,TextView textView,TextView incre,String id){
        int plusresult = ++takesValue;
        int multiresult = multiplyValue * plusresult;
        textView.setText(" = "+multiresult);
        incre.setText(""+plusresult);
        new SQLiteDataBaseConnect(context).updateincrease(id,plusresult);
        new SQLiteDataBaseConnect(context).updateFinalPrice(id,multiresult);
        int totalPriceofallitem = new SQLiteDataBaseConnect(context).getTotalResesult();
        Toast.makeText(context, "total = "+totalPriceofallitem, Toast.LENGTH_SHORT).show();
    }

    public void decrese(int takesValue,int multiplyValue, TextView textView,TextView decre,String id){
        if(takesValue>1){
            int minusresult = --takesValue;
            int multireset = multiplyValue * minusresult;
            textView.setText(" = "+multireset);
            decre.setText(""+minusresult);
            new SQLiteDataBaseConnect(context).updateincrease(id,minusresult);
            new SQLiteDataBaseConnect(context).updateFinalPrice(id,multireset);
            int totalPriceofallitem = new SQLiteDataBaseConnect(context).getTotalResesult();
            Toast.makeText(context, "total = "+totalPriceofallitem, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Can't be decrease value by one", Toast.LENGTH_SHORT).show();
        }
    }
}