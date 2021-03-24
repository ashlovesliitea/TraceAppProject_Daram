package com.example.traceappproject_daram.x9;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.example.traceappproject_daram.data.Result;

import com.example.traceappproject_daram.reprot_page.MovingFeetHeatmapActivity;

import java.util.ArrayList;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import no.nordicsemi.android.nrftoolbox.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    //viewHolder란 각 뷰들을 보관하는 Holder객체를 의미한다.
    //Viewholder는 태그 필드 안에 각 구성 요소 뷰들을 저장하므로 반복적으로 조회하지않고 즉시 액세스 가능하다.
    private ArrayList<ItemForm> localdatalist;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    //item view를 저장하는 ViewHolder 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView CardDate;
        private final TextView CardMeasure;
        private final AppCompatButton button_send;
        private final AppCompatButton button_delete;
        public Result result;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            //view 객체에 대한 참조
            CardDate = view.findViewById(R.id.CardDate);
            CardMeasure=view.findViewById(R.id.CardMeasure);
            button_send=view.findViewById(R.id.button_send);
            button_delete=view.findViewById(R.id.button_delete);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context= view.getContext();
                    Intent intent = new Intent(context, MovingFeetHeatmapActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("result",result);
                    context.startActivity(intent);
                }
            });
        }


    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param datalist ArrayList containing the data to populate views to be used
     * by RecyclerView.
     */
    //생성자에서 데이터 객체를 전달받는다. 자료형은 임의로 결정 가능.
    public MyAdapter(ArrayList<ItemForm> datalist) {
        localdatalist = datalist;
    }

    // Create new views (invoked by the layout manager)
    //아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴한다.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        //LayoutInflater = 안드로이드에서 view를 생성하는 방법
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rows, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시 (view의 내용을 localDataSet으로 교환)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

      ItemForm data=localdatalist.get(position);

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.CardDate.setText(data.getDate());
        viewHolder.CardMeasure.setText(data.getMeasure());
        viewHolder.result = data.getResult();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localdatalist.size();
    }
}
